package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.Tools;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.system.service.IRoleService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校表 Controller
 *
 * @author Jck
 * @date 2019-08-18 01:39:43
 */
@Slf4j
@Validated
@Controller
public class SchoolController extends BaseController {

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private IClassroomInfoService classroomInfoService;

    @Autowired
    private IClassInfoService classInfoService;

    @GetMapping("school")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse getAllSchools(School school) {
        return new FebsResponse().success().data(schoolService.findSchools(school));
    }

    @GetMapping("school/list")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse schoolList(QueryRequest request, School school) {
//    	User currentUser = getCurrentUser();
//    	school.setSchoolId(currentUser.getSchoolId());
        Map<String, Object> dataTable = getDataTable(this.schoolService.findSchools(request, school));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增School")
    @PostMapping("school")
    @ResponseBody
    @RequiresPermissions("school:add")
    public FebsResponse addSchool(@Valid School school, @RequestParam(required=false,value="file") MultipartFile file) throws FebsException {
        try {
			if (file != null) {
				String path = Tools.saveFile(file, "school");
				school.setPicture(path);
			}
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
    @RequiresPermissions("school:delete")
    public FebsResponse deleteSchool(@NotBlank(message = "{required}") @PathVariable String schoolIds) throws FebsException {
        try {
            List<String> list = new ArrayList<>();
            String[] schools = schoolIds.split(",");
            for (String schoolId:schools) {
                list.add(schoolId);
            }
            this.deviceInfoService.deleteDeviceInfoByschoolId(list);
            this.classInfoService.deleteClassInfosByschoolId(list);
            this.classroomInfoService.deleteClassroomInfosByschoolId(list);
            this.schoolService.deleteSchool(schoolIds);
            //删除本地学校部门数据，应该使用钉钉回调函数来进行处理；
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
    @RequiresPermissions("school:update")
    public FebsResponse updateSchool(School school, @RequestParam(required=false,value="file") MultipartFile file) throws FebsException {
        try {
			if (file != null) {
				String path = Tools.saveFile(file, "school");
				school.setPicture(path);
			}
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
    @RequiresPermissions("school:export")
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

    @GetMapping("school/bydept/list")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse schoolListByDept(QueryRequest request, School school, Long deptId) {
    	IPage<School> p = this.schoolService.findSchoolsByDept(request, school, deptId);
        Map<String, Object> dataTable = getDataTable(p);
        return new FebsResponse().success().data(dataTable);
    }

}
