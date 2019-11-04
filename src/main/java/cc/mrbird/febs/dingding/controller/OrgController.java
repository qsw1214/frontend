package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.service.AppAbutmentService;
import cc.mrbird.febs.dingding.service.IApprovalService;
import cc.mrbird.febs.dingding.util.AddressListUtil;
import cc.mrbird.febs.dingding.util.ApprovalInfUtil;
import cc.mrbird.febs.dingding.util.MessageUtil;
import cc.mrbird.febs.dingding.vo.*;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.Role;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.entity.UserRole;
import cc.mrbird.febs.system.mapper.DeptMapper;
import cc.mrbird.febs.system.mapper.UserDeptMapper;
import cc.mrbird.febs.system.mapper.UserMapper;
import cc.mrbird.febs.system.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.Utils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import net.sf.json.JSONArray;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审批控制层
 * @Slf4j 方便写日志 log.info()、log.error(),集中起来处理
 * @Validated 异常时不直接返回错误,而是对数据进行校验
 * @RestController
 */
@Slf4j
@Validated
@RestController
@RequestMapping("org")
public class OrgController {
    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private static final Logger mainLogger = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private UserDeptMapper userDeptMapper;

     @Autowired
    private IDeptService deptService;

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;


    /**
     * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String CHECK_URL = "check_url";

    /**
     * 用户变更
     */
    private static final String USER_ADD_ORG = "user_add_org";//通讯录用户增加

    private static final String USER_MODIFY_ORG = "user_modify_org";//通讯录用户更改

    private static final String USER_LEAVE_ORG = "user_leave_org";//通讯录用户离职

    private static final String ORG_ADMIN_ADD = "org_admin_add";//通讯录用户被设为管理员

    private static final String ORG_ADMIN_REMOVE = "org_admin_remove";//通讯录用户被取消设置管理员

    /**
     * 部门变更
     */
    private static final String ORG_DEPT_CREATE = "org_dept_create";//通讯录企业部门创建

    private static final String ORG_DEPT_MODIFY = "org_dept_modify";//通讯录企业部门修改

    private static final String ORG_DEPT_RMOVE = "org_dept_remove";//通讯录企业部门删除

    /**
     * 企业信息变更
     */
    private static final String ORG_REMOVE = "org_remove";//企业被解散

    private static final String ORG_CHANGE = "org_change";//企业信息发生变更

    /**
     * 角色变更
     */
    private static final String LABEL_USER_CHANGE = "label_user_change";//员工角色信息发生变更

    private static final String LABEL_CONF_ADD = "label_conf_add";//增加角色或者角色组

    private static final String LABEL_CONF_DEL= "label_conf_del";//删除角色或者角色组

    private static final String LABEL_CONF_MODIFY = "label_conf_modify";//修改角色或者角色组

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

    public long checkSynchParentDeptInfo(long deptId){
        Dept dept = deptService.getById(deptId);
        if(dept == null){
            dept = new Dept();
            //本地数据库没有父级部门，需要先行同步父级部门数据
            DeptInfoDetailVO deptDetailVO = AddressListUtil.departmentMess(deptId);
            long parentId = deptDetailVO.getParentid();
            dept.setDeptId(deptDetailVO.getId());
            dept.setCreateTime(DateUtil.getNowDateTime());
            dept.setModifyTime(DateUtil.getNowDateTime());
            dept.setDeptName(deptDetailVO.getName());
            dept.setOrderNum(deptDetailVO.getOrder());
            if(parentId == 1){    //如果父级id为1，为固定第一级
                dept.setDeptGrade(1l);
                dept.setParentId(1l);
                this.deptService.saveOrUpdate(dept);
                return 1l;
            }else{
                long parentDeptGrade = checkSynchParentDeptInfo(parentId);
                dept.setDeptGrade(parentDeptGrade + 1l);
                dept.setParentId(parentId);
                this.deptService.saveOrUpdate(dept);
                return dept.getDeptGrade();
            }
        }else{
            return dept.getDeptGrade();
        }
    }


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
            System.out.println(eventType);
            Gson gson = new Gson();
            if (ORG_DEPT_CREATE.equals(eventType) || ORG_DEPT_MODIFY.equals(eventType)) {
                bizLogger.info("通讯录企业部门创建或修改: " + plainText);
                DeptInfoVO deptInfoVO = gson.fromJson(plainText,DeptInfoVO.class);
                List<Long> deptIds = deptInfoVO.getDeptId();
                Dept dept = null;
                for (long deptId:deptIds) {
                    DeptInfoDetailVO deptInfoDetailVO = AddressListUtil.departmentMess(deptId);
                    String deptName = deptInfoDetailVO.getName();
                    dept = this.deptService.getById(deptId);
                    if(dept == null){  //若本地不存在，则视为新建
                        dept = new Dept();
                        dept.setCreateTime(DateUtil.getNowDateTime());
                    }
                    long parentId = deptInfoDetailVO.getParentid();
                    long parentDeptGrade = checkSynchParentDeptInfo(parentId);
                    dept.setParentId(parentId);
                    dept.setDeptId(deptInfoDetailVO.getId());
                    dept.setDeptGrade(parentDeptGrade + 1l);
                    dept.setModifyTime(new Date());
                    dept.setDeptName(deptName);
                    dept.setOrderNum(deptInfoDetailVO.getOrder());
                    this.deptService.saveOrUpdate(dept);

                    if(dept.getDeptGrade() == 4) { // 级别为4的 是代表学校部门
                        //同步省市区编号到学校信息表
                        List<School> schools = this.schoolService.findSchoolsByName(deptName);
                        if(schools != null && schools.size() == 1){
                            long countryDeptId = parentId;
                            Dept countryDept = this.deptService.getById(countryDeptId);
                            long cityDeptId = countryDept.getParentId();
                            Dept cityDept = this.deptService.getById(cityDeptId);
                            long provinceDeptId = cityDept.getParentId();
                            School school = schools.get(0);
                            school.setCountryDeptId(countryDeptId);
                            school.setCityDeptId(cityDeptId);
                            school.setProvinceDeptId(provinceDeptId);
                            school.setDeptId(dept.getDeptId());
                            this.schoolService.updateSchool(school);
                        }
                    }
                }
            }else if(ORG_DEPT_RMOVE.equals(eventType)){
                bizLogger.info("通讯录企业部门删除: " + plainText);
                DeptInfoVO deptInfoVO = gson.fromJson(plainText,DeptInfoVO.class);
                List<Long> deptIds = deptInfoVO.getDeptId();
                for (long deptId:deptIds) {
                    this.deptService.removeById(deptId);
                }
            }else if(USER_ADD_ORG.equals(eventType)){
                bizLogger.info("通讯录用户增加: " + plainText);
                //调取用户详情接口
                UserInfoVO userInfoVO = gson.fromJson(plainText, UserInfoVO.class);
                List<String> userIds = userInfoVO.getUserId();
                User user=new User();
                for(String userid:userIds) {//遍历json数组内容  
                    UserInfoDetailVO userInfoDetailVO = AddressListUtil.userMess(userid);
                    List<String> deptIds = userInfoDetailVO.getDepartment();

                    UserInfoDetailVO map = AddressListUtil.userMess(userid);

                    if(userService.getById(userid)==null){
                        user.setUserId(userid);
                        user.setUsername(map.getName());
                        user.setPassword(MD5Util.encrypt("", "123456"));
                        user.setAvatar(map.getAvatar());
                        user.setMobile(map.getMobile());
                        user.setBoss(map.isBoss());
                        user.setStatus("1");
                        user.setCreateTime(new Date());
                        user.setModifyTime(new Date());
                        user.setEmail(map.getEmail());
                        user.setSenior(map.isSenior());
                        user.setActive(map.isActive());
                        user.setPosition(map.getPosition());
                        user.setHide(map.isHide());
                        user.setUnionid(map.getUnionid());
                        user.setAdmin(map.isAdmin());
                        userService.save(user);
                    }

                    userDeptMapper.deleteUserDept(userid);//先删用户部门表
                    for (String deptId : deptIds) {
                        userDeptMapper.insertUserDept(userid, deptId);//插入用户部门数据
                    }

                    List<RolesInfoVO> roles = map.getRoles();
                    UserRole userRole = new UserRole();
                    Role role = new Role();
                    if (roles == null) {//如果没有角色 那就是普通用户
                        userRole.setUserId(userid);
                        userRole.setRoleId(1L);
                        userRoleService.insertUserRole(userRole);
                    } else {
                        for (int i = 0; i < roles.size(); i++) {
                            userRole.setRoleId(roles.get(i).getId());
                            userRole.setUserId(userid);
                            userRoleService.insertUserRole(userRole);//插入用户角色

                            if (roleService.getById(userRole.getRoleId()) == null) {
                                role.setRoleId(roles.get(i).getId());
                                role.setRoleName(roles.get(i).getName());
                                roleService.save(role);
                            }
                        }
                    }

                }
            }else if(USER_MODIFY_ORG.equals(eventType)){
                bizLogger.info("通讯录用户更改: " + plainText);
                UserInfoVO userInfoVO = gson.fromJson(plainText,UserInfoVO.class);
                List<String> userIds = userInfoVO.getUserId();
                User user = new User();
                for(String userId:userIds) {//遍历json数组内容 
                    UserInfoDetailVO map = AddressListUtil.userMess(userId);
                    user.setUserId(userId);
                    user.setUsername(map.getName());
                    user.setPassword(MD5Util.encrypt("", "123456"));
                    user.setAvatar(map.getAvatar());
                    user.setMobile(map.getMobile());
                    user.setBoss(map.isBoss());
                    user.setStatus("1");
                    user.setModifyTime(new Date());
                    user.setEmail(map.getEmail());
                    user.setSenior(map.isSenior());
                    user.setActive(map.isActive());
                    user.setPosition(map.getPosition());
                    user.setHide(map.isHide());
                    user.setUnionid(map.getUnionid());
                    user.setAdmin(map.isAdmin());

                    if(userService.getById(userId)==null) {
                        user.setCreateTime(new Date());
                        userService.save(user);
                    }else{
                        userService.updateProfile(user);
                    }
                }
            }else if(USER_LEAVE_ORG.equals(eventType)){
                bizLogger.info("通讯录用户离职: " + plainText);
                UserInfoVO userInfoVO = gson.fromJson(plainText,UserInfoVO.class);
                List<String> userIds = userInfoVO.getUserId();
                for(String userId:userIds){
                    userDeptMapper.deleteUserDept(userId);//先删除用户部门关系
                    userRoleService.deleteUserRole(userId);//删除用户角色关系
                    userService.deleteUser(userId);//删除用户
                }
            }else if(ORG_ADMIN_ADD.equals(eventType)){
                bizLogger.info("通讯录用户被设为管理员: " + plainText);
            }else if(ORG_ADMIN_REMOVE.equals(eventType)){
                bizLogger.info("通讯录用户被取消设置管理员: " + plainText);
            }else if(LABEL_USER_CHANGE.equals(eventType)){
                bizLogger.info("员工角色信息发生变更: " + plainText);
                UserInfoVO userInfoVO = gson.fromJson(plainText,UserInfoVO.class);
                List<String> userIds = userInfoVO.getUserIdList();
                for(String userid:userIds) {//遍历json数组内容 
                    UserInfoDetailVO map = AddressListUtil.userMess(userid);
                    List<RolesInfoVO> roles=map.getRoles();
                    UserRole userRole=new UserRole();
                    userRoleService.deleteUserRole(userid);//删除用户角色
                    if(roles==null){
                        userRole.setUserId(userid);
                        userRole.setRoleId(1L);
                        userRoleService.insertUserRole(userRole);
                    }else {
                        for (int i = 0; i < roles.size(); i++) {
                            //userRole.setRoleId(roles.get(i).getId());
                            userRole.setRoleId(1L);
                            userRole.setUserId(userid);
                            userRoleService.insertUserRole(userRole);//插入用户角色

                            if (roleService.getById(userRole.getRoleId()) == null) {
                                Role role = new Role();
                                userRole.setRoleId(1L);
                                //role.setRoleId(roles.get(i).getId());
                                role.setRoleName(roles.get(i).getName());
                                role.setCreateTime(new Date());
                                role.setModifyTime(new Date());
                                roleService.save(role);
                            }
                        }
                        List<String> deptIds = map.getDepartment();
                        userDeptMapper.deleteUserDept(userid);
                        for (String deptId : deptIds) {
                            userDeptMapper.insertUserDept(userid, deptId);
                        }
                    }
                }
            }else if(LABEL_CONF_ADD.equals(eventType)){
                bizLogger.info("增加角色或者角色组: " + plainText);
            }else if(LABEL_CONF_DEL.equals(eventType)){
                bizLogger.info("删除角色或者角色组: " + plainText);
            }else if(LABEL_CONF_MODIFY .equals(eventType)){
                bizLogger.info("修改角色或者角色组: " + plainText);
            }else if (BPMS_TASK_CHANGE.equals(eventType)) {
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
                    switch (processCode) {
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
            }
            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }

    }
}
