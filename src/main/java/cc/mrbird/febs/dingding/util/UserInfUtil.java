package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.URLConstant;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(UserInfUtil.class);

    public static JSONObject getUserInf(String userId) throws RuntimeException {
        try {
            //通过userId获取用户详情
            DingTalkClient clientUserInf = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest requestUserInf = new OapiUserGetRequest();
            requestUserInf.setUserid(userId);
            requestUserInf.setHttpMethod("GET");
            OapiUserGetResponse responseUserInf = clientUserInf.execute(requestUserInf, AccessTokenUtil.getToken());
            String userInfBody = responseUserInf.getBody();
            JSONObject jo = JSONObject.parseObject(new String(userInfBody));
            //通过用户详情里的权限去绑定自已系统的权限{"roles":[{"groupName":"默认","id":475171896,"name":"主管理员","type":101}]}
            /*String roles = String.valueOf(jo.get("roles"));
            String roles2 = roles.substring(1, roles.length()-1);
            JSONObject rolesData = JSONObject.parseObject(roles2);*/
            return jo;
        } catch (ApiException e) {
            bizLogger.error("getUserInfUtil failed", e);
            throw new RuntimeException();
        }

    }


}
