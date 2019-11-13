package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.ImgResult;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.CommonConstant;
import cc.mrbird.febs.common.utils.Tools;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IUserDeptService;

import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IUserDeptService userDeptService;

    @Autowired
    private IUserService userService;

    @GetMapping("school")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse getAllSchools(School school) {
        List<School>  schoolList = this.schoolService.findSchools(school);
        for (int i = 0 ; i < schoolList.size() ; i++){
            School schoolTemp = schoolList.get(i);
            Integer teacherCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_TEACHER);
            Integer studentCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_STUDENT);
            Integer classroomCount = this.classroomInfoService.findClassroomByMainSchoolId(schoolTemp.getSchoolId()).size();
            schoolTemp.setTeacherCount(teacherCount);
            schoolTemp.setStudentCount(studentCount);
            schoolTemp.setClassroomCount(classroomCount);
        }
        return new FebsResponse().success().data(schoolList);
    }

    /**
     * 前端接口
     * @param request
     * @param school
     * @return
     */
    @GetMapping("school/list")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse schoolList(QueryRequest request, School school) {
        if(school.getCountryDeptId().equals(Constant.COUNTRY_ALL_SELECT_DEPT_ID)){
            school.setCountryDeptId(null);
        }

        if(school.getCityDeptId().equals(Constant.CITY_ALL_SELECT_DEPT_ID)){
            school.setCityDeptId(null);
        }

        IPage<School>  schoolPages = this.schoolService.findSchools(request, school);
        List<School> schoolList = schoolPages.getRecords();
        for (int i = 0 ; i < schoolList.size() ; i++){
            School schoolTemp = schoolList.get(i);
            Integer teacherCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_TEACHER);
            Integer studentCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_STUDENT);
            Integer classroomCount = this.classroomInfoService.findClassroomByMainSchoolId(schoolTemp.getSchoolId()).size();
            schoolTemp.setTeacherCount(teacherCount);
            schoolTemp.setStudentCount(studentCount);
            schoolTemp.setClassroomCount(classroomCount);
        }
        Map<String, Object> dataTable = getDataTable(schoolPages);
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 后端系统使用
     * @param request
     * @param school
     * @return
     */
    @GetMapping("school/web/list")
    @ResponseBody
    @RequiresPermissions("school:view")
    public FebsResponse schoolWebList(QueryRequest request, School school) {
        User currentUser = getCurrentUser();
        List<Dept> depts = this.userDeptService.getDeptByUserId(currentUser.getUserId().toString());
        List<String> cityList = new ArrayList<String>();
        List<String> countryList = new ArrayList<String>();
        List<String> schoolTemList = new ArrayList<String>();
        for (int i = 0 ; i < depts.size(); i++){
            Dept dept = depts.get(i);
            if(dept.getDeptGrade() == 1){
                school.setProvince(dept.getDeptName());
                break;
            }else if(dept.getDeptGrade() == 2){
                cityList.add(dept.getDeptId());
            }else if(dept.getDeptGrade() == 3){
                countryList.add(dept.getDeptId());
            }else if(dept.getDeptGrade() == 4){
                schoolTemList.add(dept.getDeptId());
            }
        }
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("param1", cityList.size() > 0 ? cityList : null);
        params.put("param2", countryList.size() > 0 ? countryList : null);
        params.put("param3", schoolTemList.size() > 0 ? schoolTemList : null);
        IPage<School>  schoolPages = this.schoolService.findSchoolByMap(request, school,params);
        /*List<School> schoolList = schoolPages.getRecords();
        for (int i = 0 ; i < schoolList.size() ; i++){
            School schoolTemp = schoolList.get(i);
            Integer teacherCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_TEACHER);
            Integer studentCount = this.userService.getUserCountOfSchool(schoolTemp.getSchoolId(), CommonConstant.ROLE_NAME_STUDENT);
            Integer classroomCount = this.classroomInfoService.findClassroomByMainSchoolId(schoolTemp.getSchoolId()).size();
            schoolTemp.setTeacherCount(teacherCount);
            schoolTemp.setStudentCount(studentCount);
            schoolTemp.setClassroomCount(classroomCount);
        }*/
        Map<String, Object> dataTable = getDataTable(schoolPages);
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增School")
    @PostMapping("school")
    @ResponseBody
    @RequiresPermissions("school:add")
    public FebsResponse addSchool(@Valid School school) throws FebsException {
        try {
            String provinceName = school.getProvince();
            String cityName = school.getCity();
            String countryName = school.getCountry();
            LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
            if(StringUtils.isNotEmpty(provinceName)){
                queryWrapper.eq(Dept::getDeptName, provinceName);
            }
            school.setProvinceDeptId(this.deptService.getOne(queryWrapper).getDeptId());

            queryWrapper = new LambdaQueryWrapper<>();
            if(StringUtils.isNotEmpty(cityName)){
                queryWrapper.eq(Dept::getDeptName, cityName);
            }
            school.setCityDeptId(this.deptService.getOne(queryWrapper).getDeptId());

            queryWrapper = new LambdaQueryWrapper<>();
            if(StringUtils.isNotEmpty(countryName)){
                queryWrapper.eq(Dept::getDeptName, countryName);
            }
            school.setCountryDeptId(this.deptService.getOne(queryWrapper).getDeptId());
            School s = this.schoolService.createSchool(school);
            if(s.getBelongId() == null){
                s.setBelongId(s.getSchoolId());
                this.schoolService.updateSchool(s);
            }
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增School失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 删除学校，应该还添加一个逻辑，若删除的是主校，则其所有的分校应该同步删除；
     * @param schoolIds
     * @return
     * @throws FebsException
     */
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
    public FebsResponse updateSchool(School school) throws FebsException {
        try {
            School old = schoolService.getById(school.getSchoolId());
            if (old == null)
                return new FebsResponse().fail().data("未找到");
            // 判断有无权限
            User user = getCurrentUser();
            if (school != null && !userDeptService.isPermission(user.getUserId(), old.getDeptId())) {
                return new FebsResponse().fail().data("无权限");
            }
            School oldSchool = schoolService.getById(school.getSchoolId());
            if(oldSchool == null)
                return new FebsResponse().fail().data("没有该学校");
            if(oldSchool.getDeptId() != null){ // 如果设置了部门id，则判断用户有无修改权限
                List<String> parendDeptIds = deptService.getParentDeptIds(school.getDeptId());
                List<Dept> depts = userDeptService.getDeptByUserId(user.getUserId());
                boolean flag = false;
                for(Dept dept: depts){
                    if(parendDeptIds.contains(dept.getDeptId())){
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    return new FebsResponse().fail().data("无权限");
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
    public FebsResponse schoolListByDept(QueryRequest request, School school, String deptId) {
        // 判断有无部门权限
        User user = getCurrentUser();
        if(!userDeptService.isPermission(user.getUserId(), deptId)){
            return new FebsResponse().specialFail().data("无权限");
        }
        IPage<School> p = this.schoolService.findSchoolsByDept(request, school, deptId);
        Map<String, Object> dataTable = getDataTable(p);
        return new FebsResponse().success().data(dataTable);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ImgResult uplpad(MultipartFile file, HttpServletRequest request){
        String desFilePath = "";
        String oriName = "";
        ImgResult result = new ImgResult();
        Map<String, String> dataMap = new HashMap<>();
        ImgResult imgResult = new ImgResult();
        try {
            // 1.保存图片
            desFilePath = Tools.saveFile(file, "school");
            // 2.返回保存结果信息
            result.setCode(0);
            dataMap = new HashMap<>();
            dataMap.put("src", desFilePath);
            result.setData(dataMap);
            result.setMsg(oriName + "上传成功！");
            return result;
        } catch (IllegalStateException e) {
            imgResult.setCode(1);
            System.out.println(desFilePath + "图片保存失败");
            return imgResult;
        } catch (Exception e) {
            imgResult.setCode(1);
            System.out.println(desFilePath + "图片保存失败--IO异常");
            return imgResult;
        }
    }

}
