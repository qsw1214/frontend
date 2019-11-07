package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsapiTicketUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(JsapiTicketUtil.class);

    public static String getToken(String accessToken) throws RuntimeException {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_JSAPITICKET_URL);
            OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
            req.setTopHttpMethod("GET");
            OapiGetJsapiTicketResponse execute = client.execute(req, accessToken);
            String jsApiTicket = execute.getTicket();
            return jsApiTicket;
        } catch (ApiException e) {
            bizLogger.error("getJsAPITicket failed", e);
            throw new RuntimeException();
        }

    }

}
