package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.entity.Role;
import cc.mrbird.febs.system.service.IDictService;

import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author lb
 * @date 2019-08-17 19:44:52
 */
@Slf4j
@Validated
@RestController
public class DictController extends BaseController {

    @Autowired
    private IDictService dictService;

    @GetMapping("dict")
    public FebsResponse getAllDicts(Dict dict) {
        return new FebsResponse().success().data(dictService.findDicts(dict));
    }
    
    @GetMapping("dict/list")
    @RequiresPermissions("dict:view")
    public FebsResponse dictList(QueryRequest request, Dict dict) {
        Map<String, Object> dataTable = getDataTable(this.dictService.findDicts(request, dict));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Dict")
    @PostMapping("dict")
    @RequiresPermissions("dict:add")
    public FebsResponse addDict(@Valid Dict dict) throws FebsException {
        try {
            this.dictService.createDict(dict);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Dict失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Dict")
    @GetMapping("dict/delete{dictIds}")
    @RequiresPermissions("dict:delete")
    public FebsResponse deleteDict(@NotBlank(message = "{required}") @PathVariable String dictIds) throws FebsException {
        try {
            this.dictService.deleteDicts(dictIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Dict失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Dict")
    @PostMapping("dict/update")
    @RequiresPermissions("dict:update")
    public FebsResponse updateDict(Dict dict) throws FebsException {
        try {
            this.dictService.updateDict(dict);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Dict失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("dict/excel")
    @RequiresPermissions("dict:export")
    public void export(QueryRequest queryRequest, Dict dict, HttpServletResponse response) throws FebsException {
        try {
            List<Dict> dicts = this.dictService.findDicts(queryRequest, dict).getRecords();
            ExcelKit.$Export(Dict.class, response).downXlsx(dicts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
