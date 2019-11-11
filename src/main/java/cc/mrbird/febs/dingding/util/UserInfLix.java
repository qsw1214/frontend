package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.URLConstant;
import cc.mrbird.febs.dingding.controller.AuthHelper;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfLix {


    private static final Logger bizLogger = LoggerFactory.getLogger(UserInfLix.class);

    /**
     * 获取钉钉扫码的用户信息
     * @param tmpAuthCode
     * @return
     * @throws RuntimeException
     */
    public static Map getUserInf(String tmpAuthCode) throws RuntimeException {
        try {
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(tmpAuthCode);
            String unionId = UnionIdUtil.getUnionId(req, Constant.ACCESSKEY,Constant.ACCESSSECRET);
              /*获取accessToken
            【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。*/
            //获取access_token
            String accessToken = AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET);
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

    /**
     * 获取移动应用接入的用户信息
     * @param tmpAuthCode
     * @return
     * @throws RuntimeException
     */
    public static Map getUserInfAbutment(String tmpAuthCode) throws RuntimeException {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
            OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
            request.setCode(tmpAuthCode);
            request.setHttpMethod("GET");
            String accessToken = AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET);
            OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
            String userId = response.getUserid();
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
