package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.Env;
import cc.mrbird.febs.dingding.service.AppAbutmentService;
import cc.mrbird.febs.dingding.service.IApprovalService;
import cc.mrbird.febs.dingding.util.*;
import cc.mrbird.febs.monitor.entity.LoginLog;
import cc.mrbird.febs.monitor.service.ILoginLogService;
import cc.mrbird.febs.monitor.service.impl.LoginLogServiceImpl;
import cc.mrbird.febs.system.controller.LoginController;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.LoginService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.oapi.lib.aes.Utils;
import io.micrometer.core.instrument.util.JsonUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static cc.mrbird.febs.dingding.util.requestUtil.$params;

/**
 * 审批控制层
 * @Slf4j 方便写日志 log.info()、log.error(),集中起来处理
 * @Validated 异常时不直接返回错误,而是对数据进行校验
 * @RestController
 */
@Slf4j
@Validated
@RestController
@RequestMapping("approval")
public class ApprovalController extends BaseController {
    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private static final Logger mainLogger = LoggerFactory.getLogger(ApprovalController.class);

    /**
     * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String CHECK_URL = "check_url";

    /**
     * 审批任务回调
     */
    private static final String BPMS_TASK_CHANGE = "bpms_task_change";

    /**
     * 审批实例回调
     */
    private static final String BPMS_INSTANCE_CHANGE = "bpms_instance_change";

    /**
     * 相应钉钉回调时的值
     */
    private static final String CALLBACK_RESPONSE_SUCCESS = "success";

    @Autowired
    private IApprovalService approvalService;

    @Autowired
    private AppAbutmentService appAbutmentService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ILoginLogService loginLogService;

    
    @RequestMapping(value = "/callback")
    @ResponseBody
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) JSONObject json) {
        String params = " signature:"+signature + " timestamp:"+timestamp +" nonce:"+nonce+" json:"+json;
        try {
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY,
                    Constant.CORP_ID);
            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            JSONObject obj = JSON.parseObject(plainText);

            //根据回调数据类型做不同的业务处理
            String eventType = obj.getString("EventType");
            //审批任务开始，结束，转交

            if (BPMS_TASK_CHANGE.equals(eventType)) {
                bizLogger.info("收到审批任务进度更新: " + plainText);
                //todo: 实现审批的业务逻辑，如发消息
                //通过这个map获取到的信息,同步数据库的数据
                //CallBackService.insertOrUpdateApprovalInf(map);
            } else if (BPMS_INSTANCE_CHANGE.equals(eventType)) {
                bizLogger.info("收到审批实例状态更新: " + plainText);
                //调用审批审批详情接口
                //通过这个map获取到的信息,同步数据库的数据
                //CallBackService.insertOrUpdateApprovalInf(map);
                //todo: 实现审批的业务逻辑，如发消息
                String processInstanceId = obj.getString("processInstanceId");
                if (obj.containsKey("result") && obj.getString("result").equals("agree")) {
                    MessageUtil.sendMessageToOriginator(processInstanceId);
                    //审批实例开始，结束
                    String processCode = obj.getString("processCode");
                    switch(processCode){
                        //学校入驻审批实例
                        case Constant.SCHOOL_CALLBACK_URL_HOST:
                            //调用审批详情接口，获取详情
                            Map map = ApprovalInfUtil.getToken(obj.getString("processInstanceId"));
                            //JSON字符串
                            String processInstance = (String) map.get("process_instance");
                            this.approvalService.dealSchoolApprovalData(processInstance);
                            break;
                        //第三方应用接入审批实例
                        case Constant.APP_ABUTMENT_CALLBACK_URL_HOST:
                            //调用审批详情接口，获取详情
                            Map abutmentMap = ApprovalInfUtil.getToken(obj.getString("processInstanceId"));
                            //JSON字符串
                            String processInstance2 = (String) abutmentMap.get("process_instance");
                            this.approvalService.insertAppAbutmentApply(processInstance2);
                            break;

                    }
                }
            } else {
                // 其他类型事件处理
            }

            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }
    }

    /**
     * 通过app名称获取appKey和appSecret方法
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/reBackAppInf")
    @ResponseBody
    public FebsResponse selectAppInfByAppName(HttpServletRequest request, HttpServletResponse response) throws FebsException {
        Map params = $params(request);
        params.put("app_name","1");
        try {
            Map appInf = appAbutmentService.selectAppInfByAppName(params);
            if(appInf == null){
                return new FebsResponse().success().data("您申请的应用正在审核中...");
            }else{
                JSONObject jsonObject = new JSONObject(appInf);
                return new FebsResponse().success().data(jsonObject);
            }
        }catch (Exception e){
            String message = "获取app信息失败！请联系管理员";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    /**
     * 免登通过code获取用户详情接口
     * 实现免登
     * @param request
     * @param model
     * @return
     */
    @GetMapping("index")
    public Object index(HttpServletRequest request,Model model) {
        //获取参数
        Map params = $params(request);
        String tmpAuthCode = String.valueOf(params.get("tmpAuthCode"));
        Map userInfMap = getUserInf(tmpAuthCode);
        //截取掉department
        String mapDepartment = String.valueOf(userInfMap.get("department"));
        String department = mapDepartment.substring(1, mapDepartment.length() - 1);
        userInfMap.put("department", department);
        //获取roleMap
        List<Map<String,Object>> roleMapList = LoginController.getRolesMap(userInfMap);
        //123456代替密码实现登录
        //加密密码
        String password = MD5Util.encrypt(String.valueOf(userInfMap.get("name")), "123456");
        //1.判断是否第一次登陆,第一次登陆需要将用户信息和权限添加到相应的表中
        User isExistUser = loginService.selectIsExistUser(userInfMap);
        UsernamePasswordToken token;
        if (isExistUser == null) {
            userInfMap.put("password", password);
            loginService.insertUserInf(userInfMap);
            //获取钉钉登录的权限名字,并从t_role表拿到role_id,一个用户可能有多个角色
            for(Map map : roleMapList){
                Map roleIdMap = loginService.selectRoleId(map);
                userInfMap.put("roleId", roleIdMap.get("roleId"));
                //新增的用户权限与钉钉的绑定
                loginService.insertUserRole(userInfMap);
            }

            token = new UsernamePasswordToken(String.valueOf(userInfMap.get("name")), password, false);
        } else {
            token = new UsernamePasswordToken(isExistUser.getUsername(), isExistUser.getPassword(), false);
        }
        super.login(token);

        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(String.valueOf(userInfMap.get("name")));
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    /**
     * 获取鉴权数据的接口
     * @param ，response
     * @return
     * @throws RuntimeException
     */
    /*@RequestMapping(value = "/authentication")
    @ResponseBody
    public String authentication(HttpServletRequest request,HttpServletResponse response){
        String config = AuthHelper.getConfig(request);
        return config;
    }*/


    public static Map getUserInf(String tmpAuthCode) throws RuntimeException {
        try {
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(tmpAuthCode);
            String unionId = UnionIdUtil.getUnionId(req, Env.ACCESSKEY,Env.ACCESSSECRET);
             /* 获取accessToken
            【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
            */
            //获取access_token
            String accessToken = AuthHelper.getAccessToken(Env.APP_KEY,Env.APP_SECRET);
            //通过access_token和unionId获取userId
            String userId = UserIdUtil.getUserId(unionId,accessToken);
            //通过userId获取用户详情
            JSONObject jsonObject = UserInfUtil.getUserInf(userId,accessToken);
            Iterator<String> it = jsonObject.keySet().iterator();
            Map userMap = new HashMap();
            while(it.hasNext()){
                // 获得key
                String key = it.next();
                String value = jsonObject.getString(key);
                userMap.put(key,value);
            }
            return userMap;
        } catch (Exception e) {
            bizLogger.error("getUserInf failed", e);
            throw new RuntimeException();
        }

    }
}
