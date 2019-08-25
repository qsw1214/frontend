package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
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
 * @author MrBird
 */
@Controller("basicInfoView")
public class ViewController extends BaseController {

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private ISchoolTimetableService schoolTimetableService;

    @Autowired
    private ShiroHelper shiroHelper;

    /**
     * 数据管理
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "basicInfo/area")
    @RequiresPermissions("area:view")
    public String basicInfoArea() {
        return FebsUtil.view("basicInfo/area/area");
    }

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
}
