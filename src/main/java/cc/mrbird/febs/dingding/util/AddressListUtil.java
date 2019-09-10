package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.vo.DeptInfoDetailVO;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentGetRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.google.gson.Gson;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddressListUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(AddressListUtil.class);

    private static Gson gson = new Gson();
    //获取部门详情
    public static DeptInfoDetailVO departmentMess(long deptId) throws RuntimeException{
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/get");
            OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
            request.setId(deptId + "");
            request.setHttpMethod("GET");
            OapiDepartmentGetResponse response = client.execute(request, AccessTokenUtil.getToken());
            String deptBody = response.getBody();
            DeptInfoDetailVO deptInfoDetailVO = gson.fromJson(deptBody,DeptInfoDetailVO.class);
            return deptInfoDetailVO;
        } catch (ApiException e) {
            bizLogger.error("getDepartmentMess failed", e);
            throw new RuntimeException();
        }
    }
/*
    //获取部门详情
    public static Map departmentMess(String deptId) throws RuntimeException{
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/get");
            OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
            request.setId(deptId);
            request.setHttpMethod("GET");
            OapiDepartmentGetResponse response = client.execute(request, AccessTokenUtil.getToken());
            String deptBody = response.getBody();
            JSONObject jo = JSONObject.parseObject(new String(deptBody));
            Iterator<String> it = jo.keySet().iterator();
            Map deptMap = new HashMap();
            while (it.hasNext()) {
                // 获得key
                String key = it.next();
                String value = jo.getString(key);
                deptMap.put(key, value);
            }
            return deptMap;
        } catch (ApiException e) {
            bizLogger.error("getDepartmentMess failed", e);
            throw new RuntimeException();
        }
    }*/

    //获取用户详情
    public static Map userMess(String userId)throws RuntimeException {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, AccessTokenUtil.getToken());
            String userBody = response.getBody();
            JSONObject jo = JSONObject.parseObject(new String(userBody));
            Iterator<String> it = jo.keySet().iterator();
            Map userMap = new HashMap();
            while (it.hasNext()) {
                // 获得key
                String key = it.next();
                String value = jo.getString(key);
                userMap.put(key, value);
            }
            return userMap;
        } catch (ApiException e) {
            bizLogger.error("getUserMess failed", e);
            throw new RuntimeException();
        }
    }
}
