package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.Abutment;
import cc.mrbird.febs.basicInfo.service.*;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Validated
@Controller
public class ThirdAppAbutmentController extends BaseController {
    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private IClassroomInfoService classroomInfoService;

    @Autowired
    private IClassInfoService classInfoService;

    @Autowired
    private ThirdAppAbutmentService thirdAppAbutmentService;



    @GetMapping("abutment/list")
    @ResponseBody
    public FebsResponse abutmentList(QueryRequest request, Abutment abutment) throws FebsException {
//    	User currentUser = getCurrentUser();
//    	school.setSchoolId(currentUser.getSchoolId());
        System.out.println("1232");
        try {
            Map<String, Object> dataTable = getDataTable(this.thirdAppAbutmentService.selectAbutments(request, abutment));
            return new FebsResponse().success().data(dataTable);
        }catch(Exception e){
            //throw new FebsException("用户信息验证失败！请联系管理员");
            String message = "系统内部异常,请联系管理员";
            log.error(message, e);
            throw new FebsException(message);
        }

    }

    @Log("修改第三方应用申请")
    @PostMapping("abutment/update")
    @ResponseBody
//    @RequiresPermissions("school:update")
    public FebsResponse updateSchool(Abutment abutment, @RequestParam(required=false,value="file") MultipartFile file) throws FebsException {
        try {

            this.thirdAppAbutmentService.updateAbutment(abutment);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改第三方应用申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }



}

