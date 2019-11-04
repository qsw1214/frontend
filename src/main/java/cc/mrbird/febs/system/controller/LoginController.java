package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.CaptchaUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.dingding.util.UserInfLix;
import cc.mrbird.febs.monitor.entity.LoginLog;
import cc.mrbird.febs.monitor.service.ILoginLogService;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.system.service.LoginService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.Captcha;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.*;

import static cc.mrbird.febs.dingding.util.requestUtil.$params;

/**
 * @author MrBird
 */
@Validated
@RestController
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ILoginLogService loginLogService;
    @Autowired
    private LoginService loginService;


    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public FebsResponse login(
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        Map params = $params(request);
        String ways = String.valueOf(params.get("ways"));
        String freeLogin = String.valueOf(params.get("freeLogin"));
        //两个登录方式，钉钉扫码登录和企业内部登录
        if ("dingTalk".equals(ways)) {
            try {
                String tmpCode = String.valueOf(params.get("code"));
                //获取用户信息(根据isAbuemtn判断是否是扫码登录)
                Map userInfMap = new HashMap();
                if("1".equals(freeLogin)){
                    userInfMap = UserInfLix.getUserInfAbutment(tmpCode);
                }else {
                    userInfMap = UserInfLix.getUserInf(tmpCode);
                }
                //截取掉department
                String mapDepartment = String.valueOf(userInfMap.get("department"));
                String department = mapDepartment.substring(1, mapDepartment.length() - 1);
                userInfMap.put("department", department);
                //获取roleMap

                List<Map<String,Object>> roleMapList = new ArrayList<Map<String,Object>>();
                //免登不需要权限
                if(!"1".equals(freeLogin)){
                    roleMapList = getRolesMap(userInfMap);
                }
                //123456代替密码实现登录
                //加密密码
                //password = MD5Util.encrypt(String.valueOf(userInfMap.get("name")), String.valueOf(userInfMap.get("unionId")));
                String password = MD5Util.encrypt(String.valueOf(userInfMap.get("name")), "123456");
                //UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(userInfMap.get("name")), String.valueOf(userInfMap.get("unionid")), false);
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
                
                return new FebsResponse().success();
            } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
                throw new FebsException("用户信息验证失败！请联系管理员");
            } catch (AuthenticationException e) {
                throw new FebsException("认证失败！");
            } catch(Exception e){
                throw new FebsException("用户信息验证失败！请联系管理员");
            }
        }else{

            //用户名密码登录
            String username = String.valueOf(params.get("username"));
            String password = String.valueOf(params.get("password"));
            password = MD5Util.encrypt(username.toLowerCase(), password);
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
            try {
                super.login(token);
                // 保存登录日志
                LoginLog loginLog = new LoginLog();
                loginLog.setUsername(username);
                loginLog.setSystemBrowserInfo();
                this.loginLogService.saveLoginLog(loginLog);
                
                // token信息, 即sessionId
                Subject subject = SecurityUtils.getSubject();
                Serializable tokenId = subject.getSession().getId();
                Map authen = new HashMap(); 
                authen.put("token", tokenId);
                

                return new FebsResponse().success().data(authen);
            } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
                throw new FebsException(e.getMessage());
            } catch (AuthenticationException e) {
                throw new FebsException("认证失败！");
            }
        }
        /*if (!CaptchaUtil.verify(verifyCode, request)) {
            throw new FebsException("验证码错误！");
        }

        password = MD5Util.encrypt(username.toLowerCase(), password);
        //UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            super.login(token);
            // 保存登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setSystemBrowserInfo();
            this.loginLogService.saveLoginLog(loginLog);

            return new FebsResponse().success();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            throw new FebsException(e.getMessage());
        } catch (AuthenticationException e) {
            throw new FebsException("认证失败！");
        }*/

    }

    @PostMapping("regist")
    public FebsResponse regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws FebsException {
        User user = userService.findByName(username);
        if (user != null) {
            throw new FebsException("该用户名已存在");
        }
        this.userService.regist(username, password);
        return new FebsResponse().success();
    }

    @GetMapping("index/{username}")
    public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        // 更新登录时间
        this.userService.updateLoginTime(username);
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = this.loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = this.loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = this.loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().success().data(data);
    }

    @GetMapping("images/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
    }

    /**
     * 获取rolesMap
     * @param userInfMap
     * @return
     */
    public static List<Map<String, Object>> getRolesMap(Map userInfMap)  {
        String mapRoles = String.valueOf(userInfMap.get("roles"));
        //String roles = mapRoles.substring(1, mapRoles.length() - 1);
        List<Map> list = JSONArray.parseArray(mapRoles,Map.class);
        List<Map<String,Object>> roleList = new ArrayList<Map<String,Object>>();
        for(Map map:list){
            if("角色".equals(String.valueOf(map.get("groupName")))){
                roleList.add(map);
                //roleList.add(map);
            }
        }
       /* JSONObject jsonRoles = JSONObject.parseObject(roles);
        Iterator<String> it = jsonRoles.keySet().iterator();
        Map roleMap = new HashMap();
        while(it.hasNext()){
            // 获得key
            String key = it.next();
            String value = jsonRoles.getString(key);
            roleMap.put(key,value);
        }*/
        return roleList;
    }
}
