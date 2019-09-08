package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.util.AddressListUtil;
import cc.mrbird.febs.system.mapper.DeptMapper;
import cc.mrbird.febs.system.mapper.UserDeptMapper;
import cc.mrbird.febs.system.mapper.UserMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import net.sf.json.JSONArray;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;

import java.util.Arrays;
import java.util.HashMap;
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
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDeptMapper userDeptMapper;

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
     * 相应钉钉回调时的值
     */
    private static final String CALLBACK_RESPONSE_SUCCESS = "success";

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
            if (ORG_DEPT_CREATE.equals(eventType)) {
                bizLogger.info("通讯录企业部门创建: " + plainText);
                //调用部门详情接口
                com.alibaba.fastjson.JSONArray deptId=obj.getJSONArray("DeptId");
                for (int i = 0; i < deptId.size(); i++) {
                    Map map = AddressListUtil.departmentMess(deptId.get(i).toString());
                    String parentId=map.get("parentid").toString();

                    if(parentId.equals("1")){//如果父级id为1，为固定第一级
                        map.put("deptGrade", 1);
                        deptMapper.insertDept(map);
                    }else {
                        Long grade = deptMapper.findGradeByParentId(parentId);//根据查询父级id所在部门的等级
                        map.put("deptGrade", grade + 1);
                        deptMapper.insertDept(map);
                    }
                }
            } else if (ORG_DEPT_MODIFY.equals(eventType)) {
                bizLogger.info("通讯录企业部门修改: " + plainText);
                //调用部门详情接口
                com.alibaba.fastjson.JSONArray deptId=obj.getJSONArray("DeptId");
                for (int i = 0; i < deptId.size(); i++) {
                    Map map = AddressListUtil.departmentMess(deptId.get(i).toString());
                    String parentId=map.get("parentid").toString();
                    Long grade=deptMapper.findGradeByParentId(parentId);
                    map.put("deptGrade",grade+1);
                    deptMapper.updateDept(map);
                }
            } else if(ORG_DEPT_RMOVE.equals(eventType)){
                bizLogger.info("通讯录企业部门删除: " + plainText);
                com.alibaba.fastjson.JSONArray deptId=obj.getJSONArray("DeptId");
                for (int i = 0; i < deptId.size(); i++) {
                    deptMapper.deleteDept(deptId.get(i).toString());
                }
            }else if(USER_ADD_ORG.equals(eventType)){
                bizLogger.info("通讯录用户增加: " + plainText);
                //调取用户详情接口
                String userid=obj.getString("UserId");
                JSONArray jsonArray=JSONArray.fromObject(userid);
                for(int i=0;i<jsonArray.size();i++) {//遍历json数组内容  
                    Object object = jsonArray.get(i);
                    Map map = AddressListUtil.userMess(object.toString());
                    String deptId = map.get("department").toString();
                    deptId=deptId.substring(deptId.indexOf("[")+1,deptId.indexOf("]"));
                    List<String> lis = Arrays.asList(deptId.split(","));
                    for (String string : lis) {
                        userDeptMapper.insertUserDept(jsonArray.get(i).toString(),string);
                    }
                    map.put("password", MD5Util.encrypt("", "123456"));
                    map.put("isBoss", Boolean.getBoolean(map.get("isBoss").toString()));
                    map.put("isSenior", Boolean.getBoolean(map.get("isSenior").toString()));
                    map.put("active", Boolean.getBoolean(map.get("active").toString()));
                    map.put("isAdmin", Boolean.getBoolean(map.get("isAdmin").toString()));
                    map.put("isHide", Boolean.getBoolean(map.get("isHide").toString()));
                    userMapper.insertUser(map);
                }
            }else if(USER_MODIFY_ORG.equals(eventType)){
                bizLogger.info("通讯录用户更改: " + plainText);
                String userid=obj.getString("UserId");
                JSONArray jsonArray=JSONArray.fromObject(userid);//userList:"["12315","15616"]"
                for(int i=0;i<jsonArray.size();i++) {//遍历json数组内容 
                    Object object = jsonArray.get(i);
                    Map map = AddressListUtil.userMess(object.toString());
                    map.put("password", MD5Util.encrypt("", "123456"));//初始密码：123456
                    map.put("isBoss", Boolean.getBoolean(map.get("isBoss").toString()));
                    map.put("isSenior", Boolean.getBoolean(map.get("isSenior").toString()));
                    map.put("active", Boolean.getBoolean(map.get("active").toString()));
                    map.put("isAdmin", Boolean.getBoolean(map.get("isAdmin").toString()));
                    map.put("isHide", Boolean.getBoolean(map.get("isHide").toString()));
                    userMapper.updateUser(map);
                }
            }else if(USER_LEAVE_ORG.equals(eventType)){
                bizLogger.info("通讯录用户离职: " + plainText);
                String userid=obj.getString("UserId");
                JSONArray jsonArray=JSONArray.fromObject(userid);
                for(int i=0;i<jsonArray.size();i++) {//遍历json数组内容  
                    Object object = jsonArray.get(i);
                    userDeptMapper.deleteUserDept(object.toString());//先删除用户部门关系
                    userMapper.deleteUser(object.toString());//删除用户
                }
            }else if(ORG_ADMIN_ADD.equals(eventType)){
                bizLogger.info("通讯录用户被设为管理员: " + plainText);
            }else if(ORG_ADMIN_REMOVE.equals(eventType)){
                bizLogger.info("通讯录用户被取消设置管理员: " + plainText);
            }else if(LABEL_USER_CHANGE.equals(eventType)){
                bizLogger.info("员工角色信息发生变更: " + plainText);
                String userid=obj.getString("UserIdList");
                JSONArray jsonArray=JSONArray.fromObject(userid);//userList:"["12315","15616"]"
                for(int i=0;i<jsonArray.size();i++) {//遍历json数组内容 
                    Object object = jsonArray.get(i);
                    Map map = AddressListUtil.userMess(object.toString());
                    String deptId = map.get("department").toString();//depart:"[1234,6456]"
                    deptId=deptId.substring(deptId.indexOf("[")+1,deptId.indexOf("]"));
                    List<String> lis = Arrays.asList(deptId.split(","));
                    userDeptMapper.deleteUserDept(jsonArray.get(i).toString());
                    for (String string : lis) {
                        userDeptMapper.insertUserDept(jsonArray.get(i).toString(), string);
                    }
                }
            }/*else if(LABEL_CONF_ADD.equals(eventType)){
                bizLogger.info("增加角色或者角色组: " + plainText);
            }else if(LABEL_CONF_DEL.equals(eventType)){
                bizLogger.info("删除角色或者角色组: " + plainText);
            }else if(LABEL_CONF_MODIFY .equals(eventType)){
                bizLogger.info("修改角色或者角色组: " + plainText);
            }*/

            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }

    }
}
