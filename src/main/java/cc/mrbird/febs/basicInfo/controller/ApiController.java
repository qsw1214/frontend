package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.*;
import cc.mrbird.febs.basicInfo.service.*;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.Tools;
import cc.mrbird.febs.resource.entity.Comment;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.resource.service.ISubjectService;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IDictService;
import cc.mrbird.febs.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 前端对接API接口
 */
@Slf4j
@RestController
@RequestMapping("api")
public class ApiController extends BaseController {

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private IOnlineClassService onlineClassService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IClassInfoService classInfoService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private ISchoolTimetableService schoolTimetableService;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private IClassroomInfoService classroomInfoService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IDeptService deptService;

    /**
     * 根据查询条件获取设备列表
     * @param deviceInfo
     * @param request
     * @return
     */
    @GetMapping("device/list")
    @RequiresPermissions("device:list")
    public FebsResponse getDeviceInfoList(DeviceInfo deviceInfo, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.deviceInfoService.findDeviceInfos(request,deviceInfo));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 根据学校编号或学校名称获取巡课数据
     * @param onlineClass
     * @param request
     * @return
     */
    @GetMapping("onlineclass/list")
    @RequiresPermissions("onlineclass:list")
    public FebsResponse getOnlineClassList(OnlineClass onlineClass, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.onlineClassService.findOnlineClass(request,onlineClass));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 获取用户信息列表
     * @param user
     * @param request
     * @return
     */
    @GetMapping("user/list")
    @RequiresPermissions("user:list")
    public FebsResponse getUserInfoList(User user, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.userService.findUserDetail(user,request));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 新增学校
     * @param school
     * @param file
     * @return
     * @throws FebsException
     */
    @PostMapping("school/add")
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

    /**
     * 获取班级信息列表
     * @param request
     * @param classInfo
     * @return
     */
    @GetMapping("classInfo/list")
    @RequiresPermissions("classInfo:list")
    public FebsResponse classInfoList(QueryRequest request, ClassInfo classInfo) {
        Map<String, Object> dataTable = getDataTable(this.classInfoService.findClassInfos(request, classInfo));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 获取字典数据列表
     * @param request
     * @param dict
     * @return
     */
    @GetMapping("dict/list")
    @RequiresPermissions("dict:list")
    public FebsResponse dictList(QueryRequest request, Dict dict) {
        Map<String, Object> dataTable = getDataTable(this.dictService.findDicts(request, dict));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 查询课程表列表信息
     * @param schoolTimetable
     * @return
     */
    @GetMapping("curriculum/list")
    @RequiresPermissions("schoolTimetable:list")
    public FebsResponse getAllDeviceInfos(QueryRequest request, SchoolTimetable schoolTimetable) {
        return new FebsResponse().success().data(
                schoolTimetableService.findSchoolTimetables(schoolTimetable));
    }

    /**
     * 获取资源列表信息
     * @param request
     * @param resource
     * @return
     */
    @GetMapping("resource/list")
    @RequiresPermissions("resource:list")
    public FebsResponse resourceList(QueryRequest request, Resource resource) {
        Map<String, Object> dataTable = getDataTable(this.resourceService.findDetails(resource, request));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 获取专题列表
     * @param request
     * @param subject
     * @return
     */
    @GetMapping("subject/list")
    @ResponseBody
    @RequiresPermissions("subject:list")
    public FebsResponse subjectList(QueryRequest request, Subject subject) {
        Map<String, Object> dataTable = getDataTable(this.subjectService.findSubjects(request, subject));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 获取教室列表
     * @param request
     * @param classroomInfo
     * @return
     */
    @GetMapping("classroom/list")
    @RequiresPermissions("classroomInfo:list")
    public FebsResponse classroomInfoList(QueryRequest request, ClassroomInfo classroomInfo) {
        Map<String, Object> dataTable = getDataTable(this.classroomInfoService.findClassroomInfos(request, classroomInfo));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 新增资源评论
     * @param comment
     * @return
     * @throws FebsException
     */
    @PostMapping("comment/add")
    @RequiresPermissions("comment:add")
    public FebsResponse addComment(@Valid Comment comment) throws FebsException {
        try {
            User user = super.getCurrentUser();
            comment.setUserName(user.getUsername());
            comment.setUserAvatar(user.getAvatar());
            this.commentService.createComment(comment);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 获取资源评论接口
     * @param request
     * @param comment
     * @return
     */
    @GetMapping("comment/list")
    @ResponseBody
    @RequiresPermissions("comment:view")
    public FebsResponse commentList(QueryRequest request, Comment comment) {
        Map<String, Object> dataTable = getDataTable(this.commentService.findComments(request, comment));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 删除资源评论
     * @param commentIds
     * @return
     * @throws FebsException
     */
    @GetMapping("comment/delete/{commentIds}")
    @RequiresPermissions("comment:delete")
    public FebsResponse deleteComment(@NotBlank(message = "{required}") @PathVariable String commentIds) throws FebsException {
    	try {
        	User user = super.getCurrentUser();
        	List<String> list = Arrays.asList(commentIds.split(StringPool.COMMA));
        	if(!this.commentService.checkCreator(list, user.getUsername()))
        		return new FebsResponse().fail().data("无权限");
            this.commentService.deleteComments(list);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 修改资源评论数据
     * @param comment
     * @return
     * @throws FebsException
     */
    @PostMapping("comment/update")
    @RequiresPermissions("comment:update")
    public FebsResponse updateComment(Comment comment) throws FebsException {
        try {
            this.commentService.updateComment(comment);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 省级数量统计接口
     * @param request
     * @param field
     * @return
     */
    @GetMapping("province/statistical")
    @RequiresPermissions("province:statistical")
    public FebsResponse provinceStatistical(QueryRequest request, String field) {
        //school、user、device、classNum-开课次数
        long num = 0;
        if(field != null){
            switch(field){
                case "school":
                    num = this.schoolService.count();
                    break;
                case "user":
                    num = this.userService.count();
                    break;
                case "device":
                    num = this.deviceInfoService.count();
                    break;
                case "classNum":  //开课次数

                    break;
            }
        }
        FebsResponse febsResponse = new FebsResponse();
        return new FebsResponse().num(num).success();
    }

    @GetMapping("city/statistical")
    @RequiresPermissions("city:statistical")
    public FebsResponse cityStatistical(QueryRequest request, String field,String city) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if(field != null){
            switch(field){
                case "school":
                    num = this.schoolService.getCountOfCity(city);
                    break;
                case "user":
                    num = this.userService.countUserNumByDept(city);
                    break;
                case "device":
                    num = this.deviceInfoService.countDeviceByDept(city);
                    break;
                case "classNum":  //开课次数

                    break;
            }
        }
        FebsResponse febsResponse = new FebsResponse();
        return new FebsResponse().num(num).success();
    }

    @GetMapping("country/statistical")
    @RequiresPermissions("country:statistical")
    public FebsResponse countryStatistical(QueryRequest request, String field,String country) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if(field != null){
            switch(field){
                case "school":
                    num = this.schoolService.getCountOfCountry(country);
                    break;
                case "user":
                    num = this.userService.countUserNumByDept(country);
                    break;
                case "device":
                    //该区县有多少所学校，对应统计有多少设备
                    num = this.deviceInfoService.countDeviceByDept(country);
                    break;
                case "classNum":  //开课次数

                    break;
            }
        }
        FebsResponse febsResponse = new FebsResponse();
        return new FebsResponse().num(num).success();
    }

    @GetMapping("school/statistical")
    @RequiresPermissions("school:statistical")
    public FebsResponse schoolStatistical(QueryRequest request, String field,String school) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if(field != null){
            switch(field){
                case "user":
                    num = this.userService.countUserNumByDept(school);
                    break;
                case "device":
                    num = this.deviceInfoService.countDeviceBySchool(school);
                    break;
                case "classNum":  //开课次数

                    break;
            }
        }
        FebsResponse febsResponse = new FebsResponse();
        return new FebsResponse().num(num).success();
    }
}
