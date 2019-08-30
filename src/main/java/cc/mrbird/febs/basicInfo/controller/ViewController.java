package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.basicInfo.service.ISchoolTimetableService;
import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Psy
 */
@Controller("basicInfoView")
public class ViewController extends BaseController {

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private ISchoolTimetableService schoolTimetableService;

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IClassInfoService classInfoService;

    @Autowired
    private ShiroHelper shiroHelper;

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo")
    private String classroomIndex(){
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfo");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/classroomInfoAdd")
    private String classroomAdd(){
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/detail/{classroomId}")
//  @RequiresPermissions("schoolInfo:view")
    public String classroomInfoDetail(@PathVariable Integer classroomId, Model model) {
        resolveClassrModel(classroomId,model, true);
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/update/{classInfoId}")
    //@RequiresPermissions("classInfo:update")
    public String classroomInfoUpdate(@PathVariable Integer classInfoId, Model model) {
        resolveClassrModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade")
    private String gradeIndex(){
        return FebsUtil.view("basicInfo/grade/grade");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/gradeAdd")
    private String gradeAdd(){

        return FebsUtil.view("basicInfo/grade/gradeAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/detail/{classInfoId}")
//  @RequiresPermissions("classInfo:view")
    public String gradeDetail(@PathVariable Integer classInfoId, Model model) {
        resolveClassrModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/grade/gradeDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/grade/update/{classInfoId}")
    //@RequiresPermissions("classInfo:update")
    public String gradeUpdate(@PathVariable Integer classInfoId, Model model) {
        resolveClassrModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/grade/gradeUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school")
    private String schoolIndex(){
        return FebsUtil.view("basicInfo/school/school");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX +  "basicInfo/school/schoolAdd")
//  @RequiresPermissions("school:add")
    public String schoolAdd() {
        return FebsUtil.view("basicInfo/school/schoolAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school/detail/{schoolId}")
//  @RequiresPermissions("schoolInfo:view")
    public String systemUserDetail(@PathVariable Long schoolId, Model model) {
        resolveSchoolrModel(schoolId,model, true);
        return FebsUtil.view("basicInfo/school/schoolDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school/update/{schoolId}")
    //@RequiresPermissions("schoolInfo:update")
    public String systemUserUpdate(@PathVariable Long schoolId, Model model) {
        resolveSchoolrModel(schoolId,model, true);
        return FebsUtil.view("basicInfo/school/schoolUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/area")
    @RequiresPermissions("area:view")
    public String basicInfoArea() {
        return FebsUtil.view("basicInfo/area/area");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/deviceInfo")
    @RequiresPermissions("deviceInfo:view")
    public String basicInfoDeviceInfo() {

        return FebsUtil.view("basicInfo/deviceInfo/deviceInfo");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/deviceInfo/add")
    @RequiresPermissions("deviceInfo:add")
    public String basicInfoDeviceInfoAdd() {

        return FebsUtil.view("basicInfo/deviceInfo/deviceInfoAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/deviceInfo/update/{deviceId}")
    @RequiresPermissions("deviceInfo:update")
    public String basicInfoDeviceInfoUpdate(@PathVariable Integer deviceId, Model model) {
        resolveDeviceModel(deviceId, model, false);
        return FebsUtil.view("basicInfo/deviceInfo/deviceInfoUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/schoolTimetable")
    @RequiresPermissions("schoolTimetable:view")
    public String basicInfoSchoolTimetable() {

        return FebsUtil.view("basicInfo/schoolTimetable/schoolTimetable");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/schoolTimetable/add")
    @RequiresPermissions("schoolTimetable:add")
    public String basicInfoSchoolTimetableAdd() {

        return FebsUtil.view("basicInfo/schoolTimetable/schoolTimetableAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/schoolTimetable/update/{courseId}")
    @RequiresPermissions("schoolTimetable:update")
    public String basicInfoSchoolTimetableUpdate(@PathVariable Integer courseId, Model model) {
        resolveSchoolTimetableModel(courseId, model, false);
        return FebsUtil.view("basicInfo/schoolTimetable/schoolTimetableUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/operate")
    @RequiresPermissions("operate:view")
    public String basicInfoOperate() {
        return FebsUtil.view("basicInfo/operate/operate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/dingRemind")
    public String basicInfodingding() {
        return FebsUtil.view("basicInfo/dingRemind/dingRemind");
    }
    //==============================================END==================================================


    private void resolveDeviceModel(Integer deviceId, Model model, Boolean transform) {
        DeviceInfo deviceInfo = deviceInfoService.findDeviceById(deviceId);
        model.addAttribute("deviceInfo", deviceInfo);

        if (deviceInfo.getBuytTime() != null)
            model.addAttribute("buytTime", DateUtil.getDateFormat(deviceInfo.getBuytTime(), DateUtil.FULL_TIME_PATTERN_NO_DETAIL));
    }

    private void resolveSchoolTimetableModel(Integer courseId, Model model, Boolean transform) {
        SchoolTimetable schoolTimetable = schoolTimetableService.findCourseById(courseId);
        model.addAttribute("schoolTimetable", schoolTimetable);

        if (schoolTimetable.getBeginDate() != null)
            model.addAttribute("beginDate", DateUtil.getDateFormat(schoolTimetable.getBeginDate(), DateUtil.FULL_TIME_PATTERN_NO_DETAIL));
    }

    private void resolveSchoolrModel(Long schoolId, Model model, Boolean transform) {
        School school = this.schoolService.getById(schoolId);
        model.addAttribute("school", school);
    }

    private void resolveClassrModel(Integer classInfoId, Model model, Boolean transform) {
        ClassInfo classInfo = this.classInfoService.getById(classInfoId);
        model.addAttribute("classInfo", classInfo);

    }
}
