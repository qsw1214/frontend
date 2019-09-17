package cc.mrbird.febs.dingding.util;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import cc.mrbird.febs.dingding.config.Constant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

public class ApprovalInfUtil {
	 private static final Logger bizLogger = LoggerFactory.getLogger(ApprovalInfUtil.class);
     //获取审批详情信息
     public static Map getToken(String processInstanceId) throws RuntimeException {
         try {
        	 DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
             OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
             request.setProcessInstanceId(processInstanceId);
             OapiProcessinstanceGetResponse response = client.execute(request,AccessTokenUtil.getToken(Constant.APPKEY,Constant.APPSECRET));
             String userInfBody = response.getBody();
             JSONObject jo = JSONObject.parseObject(new String(userInfBody));
             Iterator<String> it = jo.keySet().iterator();
             Map userMap = new HashMap();
             while(it.hasNext()){
                 // 获得key
                 String key = it.next();
                 String value = jo.getString(key);
                 userMap.put(key,value);
             }
             return userMap;
         } catch (ApiException e) {
             bizLogger.error("getApprovalInf failed", e);
             throw new RuntimeException();
         }

     }
}
