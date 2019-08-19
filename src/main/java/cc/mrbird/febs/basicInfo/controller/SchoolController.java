package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.entity.SchoolInfo;
import cc.mrbird.febs.basicInfo.service.ISchoolService;

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
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

/**
 * 学校表 Controller
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
@Slf4j
@Validated
@Controller
public class SchoolController extends BaseController {

    @Autowired
    private ISchoolService schoolService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "school")
    private String schoolIndex(){
        return FebsUtil.view("school/school");
    }
    
    
    @GetMapping(FebsConstant.VIEW_PREFIX +  "school/schoolAdd")
//  @RequiresPermissions("school:add")
    public String schoolAdd() {
        return FebsUtil.view("school/schoolAdd");
    }

  
    @GetMapping(FebsConstant.VIEW_PREFIX + "school/detail/{schoolId}")
//  @RequiresPermissions("schoolInfo:view")
    public String systemUserDetail(@PathVariable Long schoolId, Model model) {
        resolveSchoolrModel(schoolId,model, true);
        return FebsUtil.view("school/schoolDetail");
    }

    //根据id查询
	@GetMapping("school/{schoolId}")
	@ResponseBody
	public School schoolById(@NotNull(message = "{required}") @PathVariable Long schoolId) {
		return this.schoolService.getById(schoolId);
	}
  
    @GetMapping(FebsConstant.VIEW_PREFIX + "school/update/{schoolId}")
  //@RequiresPermissions("schoolInfo:update")
    public String systemUserUpdate(@PathVariable Long schoolId, Model model) {
        resolveSchoolrModel(schoolId,model, true);
        return FebsUtil.view("school/schoolUpdate");
    }

    @GetMapping("school")
    @ResponseBody
//    @RequiresPermissions("school:list")
    public FebsResponse getAllSchools(School school) {
        return new FebsResponse().success().data(schoolService.findSchools(school));
    }

    @GetMapping("school/list")
//    @PostMapping("school/list")
    @ResponseBody
//    @RequiresPermissions("school:list")
    public FebsResponse schoolList(QueryRequest request, School school) {
        Map<String, Object> dataTable = getDataTable(this.schoolService.findSchools(request, school));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增School")
    @PostMapping("school")
    @ResponseBody
//    @RequiresPermissions("school:add")
    public FebsResponse addSchool(@Valid School school) throws FebsException {
        try {
            this.schoolService.createSchool(school);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增School失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除School")
    @GetMapping("school/delete/{schoolIds}")    
    @ResponseBody
//    @RequiresPermissions("school:delete")
    public FebsResponse deleteSchool(@NotBlank(message = "{required}") @PathVariable String schoolIds) throws FebsException {
        try {
        	String[] ids = schoolIds.split(StringPool.COMMA);
            this.schoolService.deleteSchool(ids);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除School失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改School")
    @PostMapping("school/update")
    @ResponseBody
//    @RequiresPermissions("school:update")
    public FebsResponse updateSchool(School school) throws FebsException {
        try {
            this.schoolService.updateSchool(school);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改School失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("school/excel")
    @ResponseBody
//    @RequiresPermissions("school:export")
    public void export(QueryRequest queryRequest, School school, HttpServletResponse response) throws FebsException {
        try {
            List<School> schools = this.schoolService.findSchools(queryRequest, school).getRecords();
            ExcelKit.$Export(School.class, response).downXlsx(schools, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    private void resolveSchoolrModel(Long schoolId, Model model, Boolean transform) {
        School school = this.schoolService.getById(schoolId);
        System.out.println("school="+school);
        model.addAttribute("school", school);
        
    }
}
