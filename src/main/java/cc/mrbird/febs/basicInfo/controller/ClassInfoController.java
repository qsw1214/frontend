package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
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
 * @date 2019-08-22 17:35:44
 */
@Slf4j
@Validated
@Controller
public class ClassInfoController extends BaseController {

    @Autowired
    private IClassInfoService classInfoService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade")
    private String classIndex(){
        return FebsUtil.view("basicInfo/grade/grade");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/gradeAdd")
    private String classAdd(){
        return FebsUtil.view("basicInfo/grade/gradeAdd");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/detail/{classInfoId}")
//  @RequiresPermissions("classInfo:view")
    public String systemUserDetail(@PathVariable Integer classInfoId, Model model) {
    	resolveClassrModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/grade/gradeDetail");
    }
    
//    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/update/{gradeId}")
//  //@RequiresPermissions("classInfo:update")
//    public String systemUserUpdate(@PathVariable Integer classInfoId, Model model) {
//    	resolveClassrModel(classInfoId,model, true);
//        return FebsUtil.view("basicInfo/grade/gradeUpdate");
//    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/update/{classInfoId}")
    //@RequiresPermissions("classInfo:update")
    public String systemUserUpdate(@PathVariable Integer classInfoId, Model model) {
        resolveClassrModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/grade/gradeUpdate");
    }

//    @GetMapping(FebsConstant.VIEW_PREFIX + "classInfo")
//    private String classInfoIndex(){
//        return FebsUtil.view("classInfo/classInfo");
//    }
//    
//    @GetMapping(FebsConstant.VIEW_PREFIX + "classInfo")
//    private String classInfoIndex(){
//        return FebsUtil.view("classInfo/classInfo");
//    }

    @GetMapping("classInfo")
    @ResponseBody
    //@RequiresPermissions("classInfo:list")
    public FebsResponse getAllClassInfos(ClassInfo classInfo) {
        return new FebsResponse().success().data(classInfoService.findClassInfos(classInfo));
    }

    @GetMapping("classInfo/list")
    @ResponseBody
    //@RequiresPermissions("classInfo:list")
    public FebsResponse classInfoList(QueryRequest request, ClassInfo classInfo) {
        Map<String, Object> dataTable = getDataTable(this.classInfoService.findClassInfos(request, classInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增ClassInfo")
    @PostMapping("classInfo")
    @ResponseBody
    //@RequiresPermissions("classInfo:add")
    public FebsResponse addClassInfo(@Valid ClassInfo classInfo) throws FebsException {
    	System.out.println(classInfo);
        try {
            this.classInfoService.createClassInfo(classInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增ClassInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除ClassInfo")
    @GetMapping("classInfo/delete/{classIds}")
    @ResponseBody
    //@RequiresPermissions("classInfo:delete")
    public FebsResponse deleteClassInfo(@NotBlank(message = "{required}") @PathVariable String classIds) throws FebsException {
        try {
            this.classInfoService.deleteClassInfo(classIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除ClassInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改ClassInfo")
    @PostMapping("classInfo/update")
    @ResponseBody
    //@RequiresPermissions("classInfo:update")
    public FebsResponse updateClassInfo(ClassInfo classInfo) throws FebsException {
        try {
            this.classInfoService.updateClassInfo(classInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改ClassInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("classInfo/excel")
    @ResponseBody
    //@RequiresPermissions("classInfo:export")
    public void export(QueryRequest queryRequest, ClassInfo classInfo, HttpServletResponse response) throws FebsException {
        try {
            List<ClassInfo> classInfos = this.classInfoService.findClassInfos(queryRequest, classInfo).getRecords();
            ExcelKit.$Export(ClassInfo.class, response).downXlsx(classInfos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    private void resolveClassrModel(Integer classInfoId, Model model, Boolean transform) {
        ClassInfo classInfo = this.classInfoService.getById(classInfoId);
        model.addAttribute("classInfo", classInfo);
        
    }
}
