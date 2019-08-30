package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.service.DingRemindService;
import cc.mrbird.febs.dingding.util.ApprovalInfUtil;
import cc.mrbird.febs.dingding.util.MessageUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.dingtalk.oapi.lib.aes.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static cc.mrbird.febs.dingding.util.requestUtil.$params;

@Slf4j
@Validated
@RestController
@RequestMapping("dingRemind")
public class DingRemindController extends BaseController {
    @Autowired
    private DingRemindService dingRemindService;


    @RequestMapping(value = "/dingRemind")
    @ResponseBody
    public FebsResponse dingRemind(HttpServletRequest request, HttpServletResponse response) throws FebsException {
        Map params = $params(request);
        //获取某学校一个学期的所有课程，并创建日程
        try {
            dingRemindService.selectCourseInfList(params);
            return new FebsResponse().success();
        }catch (NullPointerException e){
            String message = "企业内部错误,请联系管理员";
            log.error(message, e);
            throw new FebsException(message);
        }catch (Exception e){
            String message = "企业内部错误,请联系管理员";
            log.error(message, e);
            throw new FebsException(message);
        }

    }



}
