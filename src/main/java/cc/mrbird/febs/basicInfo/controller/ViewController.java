package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.*;
import cc.mrbird.febs.basicInfo.service.*;
import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.HttpUtils;
import cc.mrbird.febs.dingding.config.Constant;
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
    private IClassroomInfoService classroomInfoService;

    @Autowired
    private ShiroHelper shiroHelper;

    @Autowired
    private ThirdAppAbutmentService thirdAppAbutmentService;

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo")
    @RequiresPermissions("classroomInfo:view")
    private String classroomIndex(){
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfo");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/classroomInfoAdd")
    @RequiresPermissions("classroomInfo:add")
    private String classroomAdd(Model model){
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/detail/{classroomId}")
    @RequiresPermissions("classroomInfo:view")
    public String classroomInfoDetail(@PathVariable Integer classroomId, Model model) {
        resolveClassrModel(classroomId,model, true);
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classroomInfo/update/{classroomInfoId}")
    @RequiresPermissions("classroomInfo:update")
    public String classroomInfoUpdate(@PathVariable Integer classroomInfoId, Model model) {
        resolveClassrModel(classroomInfoId,model, true);
        return FebsUtil.view("basicInfo/classroomInfo/classroomInfoUpdate");
    }

    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classInfo")
    @RequiresPermissions("classInfo:view")
    private String gradeIndex(){
        return FebsUtil.view("basicInfo/classInfo/classInfo");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classInfo/classInfoAdd")
    @RequiresPermissions("classInfo:add")
    private String gradeAdd(){
        return FebsUtil.view("basicInfo/classInfo/classInfoAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classInfo/detail/{classInfoId}")
    @RequiresPermissions("classInfo:view")
    public String gradeDetail(@PathVariable Integer classInfoId, Model model) {
        resolveClassModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/classInfo/classInfoDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/classInfo/update/{classInfoId}")
    @RequiresPermissions("classInfo:update")
    public String gradeUpdate(@PathVariable Integer classInfoId, Model model) {
        resolveClassModel(classInfoId,model, true);
        return FebsUtil.view("basicInfo/classInfo/classInfoUpdate");
    }
    //==============================================END==================================================

    //==============================================START==================================================

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school")
    @RequiresPermissions("school:view")
    private String schoolIndex(){
        return FebsUtil.view("basicInfo/school/school");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX +  "basicInfo/school/schoolAdd")
    @RequiresPermissions("school:add")
    public String schoolAdd() {
        return FebsUtil.view("basicInfo/school/schoolAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school/detail/{schoolId}")
    @RequiresPermissions("school:view")
    public String systemUserDetail(@PathVariable Long schoolId, Model model) {
        resolveSchoolrModel(schoolId,model, true);
        return FebsUtil.view("basicInfo/school/schoolDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/school/update/{schoolId}")
    @RequiresPermissions("school:update")
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

    /**
     * 第三方移动应用接入
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/abutment")
    public String basicInfoAbutment() {
        return FebsUtil.view("basicInfo/abutment/abutment");
    }

    /**
     * 第三方移动应用详情信息
     * @param abutmentId
     * @param model
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/abutment/detail/{abutmentId}")
//  @RequiresPermissions("abutment:view")
    public String abutMentDetail(@PathVariable Long abutmentId, Model model) {
        resolveAbutmentModel(abutmentId,model, true);
        return FebsUtil.view("basicInfo/abutment/abutmentDetail");
    }

    /**
     * 修改第三方移动应用
     * @param abutmentId
     * @param model
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/abutment/update/{abutmentId}")
    //@RequiresPermissions("abutment:update")
    public String abutmentUpdate(@PathVariable Long abutmentId, Model model) {
        resolveAbutmentModel(abutmentId,model, true);
        return FebsUtil.view("basicInfo/abutment/abutmentUpdate");
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

    private void resolveAbutmentModel(Long abutmentId, Model model, Boolean transform) {
        Abutment abutment = this.thirdAppAbutmentService.getById(abutmentId);
        model.addAttribute("abutment", abutment);
    }

    private void resolveClassrModel(Integer classroomInfoId, Model model, Boolean transform) {
        ClassroomInfo classroomInfo = this.classroomInfoService.getById(classroomInfoId);
        model.addAttribute("classroomInfo", classroomInfo);
    }
    
    private void resolveClassModel(Integer classInfoId, Model model, Boolean transform) {
        ClassInfo classInfo = this.classInfoService.getById(classInfoId);
        model.addAttribute("classInfo", classInfo);
    }
}
