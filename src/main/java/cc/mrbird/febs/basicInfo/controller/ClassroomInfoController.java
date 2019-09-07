package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.ClassroomInfo;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author psy
 * @date 2019-08-23 15:45:24
 */
@Slf4j
@Validated
@Controller
public class ClassroomInfoController extends BaseController {

    @Autowired
    private IClassroomInfoService classroomInfoService;

    @GetMapping("classroomInfo")
    @ResponseBody
    @RequiresPermissions("classroomInfo:view")
    public FebsResponse getAllClassroomInfos(ClassroomInfo classroomInfo) {
        return new FebsResponse().success().data(classroomInfoService.findClassroomInfos(classroomInfo));
    }

    @GetMapping("classroomInfo/list")
    @ResponseBody
    @RequiresPermissions("classroomInfo:view")
    public FebsResponse classroomInfoList(QueryRequest request, ClassroomInfo classroomInfo) {
        Map<String, Object> dataTable = getDataTable(this.classroomInfoService.findClassroomInfos(request, classroomInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增ClassroomInfo")
    @PostMapping("classroomInfo")
    @ResponseBody
    @RequiresPermissions("classroomInfo:add")
    public FebsResponse addClassroomInfo(@Valid ClassroomInfo classroomInfo) throws FebsException {
        try {
            this.classroomInfoService.createClassroomInfo(classroomInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除ClassroomInfo")
    @GetMapping("classroomInfo/delete/{classroomIds}")
    @ResponseBody
    @RequiresPermissions("classroomInfo:delete")
    public FebsResponse deleteClassroomInfo(@NotBlank(message = "{required}") @PathVariable String classroomIds) throws FebsException {
        try {
        	this.classroomInfoService.deleteClassroomInfo(classroomIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改ClassroomInfo")
    @PostMapping("classroomInfo/update")
    @ResponseBody
    @RequiresPermissions("classroomInfo:update")
    public FebsResponse updateClassroomInfo(ClassroomInfo classroomInfo) throws FebsException {
        try {
            this.classroomInfoService.updateClassroomInfo(classroomInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("classroomInfo/excel")
    @ResponseBody
    @RequiresPermissions("classroomInfo:export")
    public void export(QueryRequest queryRequest, ClassroomInfo classroomInfo, HttpServletResponse response) throws FebsException {
        try {
            List<ClassroomInfo> classroomInfos = this.classroomInfoService.findClassroomInfos(queryRequest, classroomInfo).getRecords();
            ExcelKit.$Export(ClassroomInfo.class, response).downXlsx(classroomInfos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    private void resolveClassrModel(Integer classroomInfoId, Model model, Boolean transform) {
        ClassroomInfo classroomInfo = this.classroomInfoService.getById(classroomInfoId);
        model.addAttribute("classroomInfo", classroomInfo);

    }
}
