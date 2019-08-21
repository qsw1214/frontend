package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.URLConstant;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetUseridByUnionidRequest;
import com.dingtalk.api.response.OapiUserGetUseridByUnionidResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过unionId和ACCESS_TOKEN获取userId
 */
public class UserIdUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(UserIdUtil.class);

    public static String getUserId(String unionId) throws RuntimeException {
        try {
            DingTalkClient clientUserId = new DefaultDingTalkClient(URLConstant.URL_GET_USERID_BY_UNIONID);
            OapiUserGetUseridByUnionidRequest requestUserId = new OapiUserGetUseridByUnionidRequest();
            //这个unionID
            requestUserId.setUnionid(unionId);
            requestUserId.setHttpMethod("GET");

            OapiUserGetUseridByUnionidResponse responseUserId = clientUserId.execute(requestUserId, AccessTokenUtil.getToken());
            String userIdBody = responseUserId.getBody();
            JSONObject jo = JSONObject.parseObject(new String(userIdBody));
            String userId = String.valueOf(jo.get("userid"));
            return userId;
        } catch (ApiException e) {
            bizLogger.error("getUserId failed", e);
            throw new RuntimeException();
        }

    }
}
