package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cc.mrbird.febs.dingding.config.URLConstant.URL_GET_TOKKEN;


/**
 * 获取企业内部应用access_token工具类
 */
public class AccessTokenUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(AccessTokenUtil.class);

    public static String getToken(String AppKey,String AppSecret) throws RuntimeException {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(AppKey);
            request.setAppsecret(AppSecret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            return accessToken;
        } catch (ApiException e) {
            bizLogger.error("getAccessToken failed", e);
            throw new RuntimeException();
        }

    }
}
