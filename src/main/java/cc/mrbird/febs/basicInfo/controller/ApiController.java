package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.*;
import cc.mrbird.febs.basicInfo.service.*;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.resource.entity.Comment;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.resource.service.ISubjectService;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IDictService;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.system.vo.AreaDataCountVO;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.*;

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
     *
     * @param deviceInfo
     * @param request
     * @return
     */
    @GetMapping("device/list")
    @RequiresPermissions("device:list")
    public FebsResponse getDeviceInfoList(DeviceInfo deviceInfo, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.deviceInfoService.findDeviceInfos(request, deviceInfo));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 根据学校编号或学校名称获取巡课数据
     *
     * @param onlineClass
     * @param request
     * @return
     */
    @GetMapping("onlineclass/list")
    @RequiresPermissions("onlineclass:list")
    public FebsResponse getOnlineClassList(OnlineClass onlineClass, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.onlineClassService.findOnlineClass(request, onlineClass));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 获取用户信息列表
     *
     * @param user
     * @param request
     * @return
     */
    @GetMapping("user/list")
    @RequiresPermissions("user:list")
    public FebsResponse getUserInfoList(User user, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.userService.findUserDetail(user, request));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 新增学校
     *
     * @param school
     * @param file
     * @return
     * @throws FebsException
     */
    @PostMapping("school/add")
    @RequiresPermissions("school:add")
    public FebsResponse addSchool(@Valid School school, @RequestParam(required = false, value = "file") MultipartFile file) throws FebsException {
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
            if (!this.commentService.checkCreator(list, user.getUsername()))
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
     *
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

/*    *//**
     * 省级数量统计接口
     *
     * @param request
     * @param field
     * @return
     *//*
    @GetMapping("province/statistical")
    @RequiresPermissions("province:statistical")
    public FebsResponse provinceStatistical(QueryRequest request, String field) {
        //school、user、device、classNum-开课次数
        long num = 0;
        if (field != null) {
            switch (field) {
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
    public FebsResponse cityStatistical(QueryRequest request, String field, String city) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if (field != null) {
            switch (field) {
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
    public FebsResponse countryStatistical(QueryRequest request, String field, String country) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if (field != null) {
            switch (field) {
                case "school":
                    num = this.schoolService.getCountOfCountry(country);
                    break;
                case "user":
//                    num = this.userService.countUserNumByDept(country);
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
    public FebsResponse schoolStatistical(QueryRequest request, String field, String school) {
        //school、user、device、classNum-开课次数
        Integer num = 0;
        if (field != null) {
            switch (field) {
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
    }*/

    /**
     * 根据省市区条件进行统计开课数量统计
     * @param request
     * @param cityDeptId
     * @param countryDeptId
     * @return
     */
    @GetMapping("netCLassCount")
//    @RequiresPermissions("count:netClassCount")
    public FebsResponse getNetClassCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId) {
        //1、根据省市区条件获取所有符合条件的学校列表
        //2、学校关联教室，获取其所有的教室列表对应的URL地址
        //3、以2019-10-01为起始时间，至当前时间，作为查询条件，统计其推流次数即开课数量统计
        List<ClassroomInfo> classroomLists = this.classroomInfoService.getClassroomInfoByCityCountry(provinceId, cityDeptId, countryDeptId);
        Integer count = 0;
        for (int i = 0; i < classroomLists.size(); i++) {
            ClassroomInfo info = classroomLists.get(i);
            String url = info.getUrl();
            String byear = "2019", bmonth = "10", bday = "01";
            String eyear = DateUtil.getLatestYear() + "";
            String emonth = DateUtil.getLatestMonth() + "";
            String eday = DateUtil.getLatestDay() + "";
            Map<String, Integer> map = LiveRadioReqUtil.getRadioPlayCount(byear, bmonth, bday, eyear, emonth, eday, url);
            count += map.get(url);
        }
        return new FebsResponse().num(count).success();
    }

    /**
     * 根据省市区条件获取教室数量
     * @param request
     * @param provinceId
     * @param cityDeptId
     * @param countryDeptId
     * @return
     */
    @GetMapping("classroomCount")
//    @RequiresPermissions("count:classroomCount")
    public FebsResponse getClassroomCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId) {
        Integer count = this.classroomInfoService.getClassroomCount(provinceId, cityDeptId, countryDeptId);
        return new FebsResponse().num(count).success();
    }

    /**
     * 根据省市区进行资源统计
     *
     * @return
     */
    @GetMapping("resourceCount")
//    @RequiresPermissions("count:resourceCount")
    public FebsResponse getResourceCount(QueryRequest request, Integer deptId) {
        Integer count = this.resourceService.getResourceCount(deptId);
        return new FebsResponse().num(count).success();
    }

    /**
     * 根据省市区条件进行学校数量统计
     */
    @GetMapping("schoolCount")
//    @RequiresPermissions("count:schoolCount")
    public FebsResponse getSchoolCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId) {
        Integer count = this.schoolService.getSchoolCount(provinceId, cityDeptId, countryDeptId);
        return new FebsResponse().num(count).success();
    }

    /**
     * 根据省市区学校条件进行教师数量统计
     */
    @GetMapping("teacherCount")
//    @RequiresPermissions("count:teacherCount")
    public FebsResponse getTeacherCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId,Integer schoolDeptId) {
        Integer count = 1000;
        return new FebsResponse().num(count).success();
    }

    /**
     * 根据省市区条件进行学生数量统计
     */
    @GetMapping("studentCount")
//    @RequiresPermissions("count:studentCount")
    public FebsResponse getStudentCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId,Integer schoolDeptId) {
        Integer count = 2000;
        return new FebsResponse().num(count).success();
    }

    /**
     * 最近十二个月的开课数量统计
     */
    @GetMapping("perMonthNetClassCount")
//    @RequiresPermissions("count:perMonthNetClassCount")
    public FebsResponse getPerMonthNetClassCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId) {
/*
        List<ClassroomInfo> classroomLists = this.classroomInfoService.getClassroomInfoByCityCountry(provinceId, cityDeptId, countryDeptId);
        String[] last12Month = DateUtil.getLast12Months();
        Map<String, Integer> resultCount = new TreeMap<String, Integer>();
        for (int i = 0; i < last12Month.length; i++) {
            String monthDate = last12Month[i];
            String[] yearMonth = monthDate.split("-");
            Integer count = 0;
            for (int j = 0; j < classroomLists.size(); j++) {
                ClassroomInfo info = classroomLists.get(j);
                String url = info.getUrl();
                Map<String, Integer> map = LiveRadioReqUtil.getRadioPlayCount(yearMonth[0], yearMonth[1], "01", yearMonth[0], yearMonth[1], "31", url);
                count += map.get(url);
            }
            resultCount.put(monthDate, count);
        }*/
        //构造测试数据
        String[] last12Month = DateUtil.getLast12Months();
        Map<String, Integer> resultCount = new TreeMap<String, Integer>();
        for (int i = 0; i < last12Month.length; i++) {
            resultCount.put(last12Month[i], Integer.valueOf(new Random().nextInt(1000)));
        }
        return new FebsResponse().data(resultCount).success();
    }

    /**
     * 最近十二个月的接入学校数量统计
     */
    @GetMapping("perMonthSchoolCount")
//    @RequiresPermissions("count:perMonthNetClassCount")
    public FebsResponse getPerMonthSchoolCount(QueryRequest request, Integer provinceId, Integer cityDeptId, Integer countryDeptId) {
        /*Map<String, Object> resultCount = new TreeMap<String, Object>();
        resultCount = this.schoolService.getLast12MonthSchoolCount(provinceId,cityDeptId,countryDeptId);*/
        //构造测试数据
        String[] last12Month = DateUtil.getLast12Months();
        Map<String, Integer> resultCount = new TreeMap<String, Integer>();
        for (int i = 0; i < last12Month.length; i++) {
            resultCount.put(last12Month[i], Integer.valueOf(new Random().nextInt(1000)));
        }
        return new FebsResponse().data(resultCount).success();
    }

    /**
     * 根据主校ID带出所有正在上课的教室列表
     */
    @GetMapping("onlineClassRooms")
//    @RequiresPermissions("count:perMonthNetClassCount")
    public FebsResponse getOnlineClassRooms(QueryRequest request, Integer schoolId) {
        //查询出属于所有学校的教室列表，然后再pass掉未上课的教室
        List<ClassroomInfo> resultInfos = new ArrayList<ClassroomInfo>();
        if(schoolId != null){
            List<ClassroomInfo> roomInfos = this.classroomInfoService.findClassroomByMainSchoolId(schoolId);
            /*for(int i = 0; i < roomInfos.size(); i++){
                ClassroomInfo info = roomInfos.get(i);
                List<RadioStatus> list = LiveRadioReqUtil.getRadioStatus(info.getUrl());
                if(list.size() > 0){
                    RadioStatus status = list.get(0);
                    //若为在线状态，则放入结果集合
                    if("1".equals(status.getStatus())){
                        info.setPlayUrl(PropertiesUtil.getProperty("radio_install_address") + info.getPlayUrl());
                        resultInfos.add(info);
                    }
                }
            }*/
            //构造测试数据
            resultInfos = roomInfos;
        }
        return new FebsResponse().data(resultInfos).success();
    }

    /**
     * 根据学校编号列出教师列表
     */
    @GetMapping("teacherListBySchoolId")
//    @RequiresPermissions("list:teacherListBySchoolId")
    public FebsResponse getTeacherListBySchoolId(QueryRequest request, Integer schoolId) {
        IPage<User> list = this.userService.getTeacherListBySchoolId(schoolId,request);
        return new FebsResponse().data(list).success();
    }

    /**
     * 区域开课、资源、学生数量柱状图统计
     * @return
     */
    @GetMapping("getAllCityData")
//    @RequiresPermissions("list:allCityData")
    public FebsResponse getAllCityData(){
        List<Dept> cityDatas = this.deptService.getAllCityData();
        List<String> cityNameLists = new ArrayList<String>();
        List<Integer> netCountLists = new ArrayList<Integer>();
        List<Integer> resourceCountLists = new ArrayList<Integer>();
        List<Integer> studentCountLists = new ArrayList<Integer>();

        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getDeptName,"湖南省");
        Dept provinceDept = this.deptService.getOne(queryWrapper);
        for(int i = 0 ; i < cityDatas.size() ; i++){
            //统计每个市区的开课数量总数
            Integer count = 0;
           /* List<ClassroomInfo> classroomLists = this.classroomInfoService
                    .getClassroomInfoByCityCountry(provinceDept.getDeptId().intValue(), cityDatas.get(i).getDeptId().intValue(), null);
            for (int j = 0; j < classroomLists.size(); j++) {
                ClassroomInfo info = classroomLists.get(j);
                String url = info.getUrl();
                int nowYear = DateUtil.getLatestYear();
                Map<String, Integer> map = LiveRadioReqUtil
                        .getRadioPlayCount("2019", "11", "01",
                                DateUtil.getLatestYear().toString(),
                                DateUtil.getLatestMonth().toString(),
                                DateUtil.getLatestDay().toString(), url);
                count += map.get(url);
            }*/
            count = Integer.valueOf(new Random().nextInt(10000));
            netCountLists.add(count);
            //统计每个市区的资源数统计 -- 暂时没有统计包括市级下面的区县的资源数据
            Integer resourceCount = this.resourceService.getResourceCount(cityDatas.get(i).getDeptId().intValue());
            resourceCountLists.add(resourceCount);
            //统计每个市区的学生数  —— 暂时以随机数代替
            Integer studentCount = Integer.valueOf(new Random().nextInt(10000));
            studentCountLists.add(studentCount);
            //获取所有的市级的名称
            cityNameLists.add(cityDatas.get(i).getDeptName());
        }
        AreaDataCountVO countVo = new AreaDataCountVO();
        countVo.setCityNameList(cityNameLists);
        countVo.setNetClassCountList(netCountLists);
        countVo.setResourceCountList(resourceCountLists);
        countVo.setStudentCountList(studentCountLists);
        return new FebsResponse().data(countVo).success();
    }
}
