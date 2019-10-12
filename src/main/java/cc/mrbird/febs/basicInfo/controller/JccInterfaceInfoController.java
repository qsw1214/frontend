package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.JccInterfaceInfo;
import cc.mrbird.febs.basicInfo.service.IJccInterfaceInfoService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Category;
import cc.mrbird.febs.resource.service.ICategoryService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author lb
 * @date 2019-10-11 09:27:33
 */
@Slf4j
@Validated
@RestController
@RequestMapping("jccInterfaceInfo")
public class JccInterfaceInfoController extends BaseController {

    @Autowired
    private IJccInterfaceInfoService jccInterfaceInfoService;
   /* @Autowired
    private ICategoryService categoryService;*/

    @GetMapping("tree")
    public FebsResponse getMenuTree(JccInterfaceInfo jccInterfaceInfo) throws FebsException {
        try {
            /*MenuTree<Category> categorys = this.categoryService.findCategorys(category);
            return new FebsResponse().success().data(categorys.getChildren());*/
            MenuTree<JccInterfaceInfo> jccInterfaceInfos = this.jccInterfaceInfoService.findJccInterfaceInfosTree(jccInterfaceInfo);
            return new FebsResponse().success().data(jccInterfaceInfos.getChildren());
        } catch (Exception e) {
            String message = "获取菜单树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("select/tree")
    public List<MenuTree<JccInterfaceInfo>> getDeptTree() throws FebsException {
        try {
            /*return this.categoryService.findCategorys();*/
            return this.jccInterfaceInfoService.findJccInterfaceInfosTree();
        } catch (Exception e) {
            String message = "获取类别树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "jccInterfaceInfo")
    private String jccInterfaceInfoIndex(){
        return FebsUtil.view("jccInterfaceInfo/jccInterfaceInfo");
    }

    @GetMapping("jccInterfaceInfo")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:list")
    public FebsResponse getAllJccInterfaceInfos(JccInterfaceInfo jccInterfaceInfo) {
        return new FebsResponse().success().data(jccInterfaceInfoService.findJccInterfaceInfos(jccInterfaceInfo));
    }

    @GetMapping("jccInterfaceInfo/list")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:list")
    public FebsResponse jccInterfaceInfoList(QueryRequest request, JccInterfaceInfo jccInterfaceInfo) {
        Map<String, Object> dataTable = getDataTable(this.jccInterfaceInfoService.findJccInterfaceInfos(request, jccInterfaceInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增JccInterfaceInfo")
    @PostMapping("jccInterfaceInfo")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:add")
    public FebsResponse addJccInterfaceInfo(@Valid JccInterfaceInfo jccInterfaceInfo) throws FebsException {
        try {
            this.jccInterfaceInfoService.createJccInterfaceInfo(jccInterfaceInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增JccInterfaceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除JccInterfaceInfo")
    @GetMapping("jccInterfaceInfo/delete/{interfaces}")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:delete")
    public FebsResponse deleteJccInterfaceInfo(@NotBlank(message = "{required}") @PathVariable String interfaces) throws FebsException {
        try {
            this.jccInterfaceInfoService.deleteJccInterfaceInfos(interfaces);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除JccInterfaceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改JccInterfaceInfo")
    @PostMapping("jccInterfaceInfo/update")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:update")
    public FebsResponse updateJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo) throws FebsException {
        try {
            this.jccInterfaceInfoService.updateJccInterfaceInfo(jccInterfaceInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改JccInterfaceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("jccInterfaceInfo/excel")
    @ResponseBody
    @RequiresPermissions("jccInterfaceInfo:export")
    public void export(QueryRequest queryRequest, JccInterfaceInfo jccInterfaceInfo, HttpServletResponse response) throws FebsException {
        try {
            List<JccInterfaceInfo> jccInterfaceInfos = this.jccInterfaceInfoService.findJccInterfaceInfos(queryRequest, jccInterfaceInfo).getRecords();
            ExcelKit.$Export(JccInterfaceInfo.class, response).downXlsx(jccInterfaceInfos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
