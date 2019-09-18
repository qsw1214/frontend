package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cc.mrbird.febs.dingding.config.URLConstant.URL_GET_USER_INFO_BYCODE;

public class UnionIdUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(UnionIdUtil.class);

    public static String getUnionId(OapiSnsGetuserinfoBycodeRequest req,String AccessKey,String AccessSecret) throws RuntimeException {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_USER_INFO_BYCODE);
            OapiSnsGetuserinfoBycodeResponse response2 = client.execute(req, AccessKey, AccessSecret);
            String unionIdDemo = response2.getBody();
            JSONObject jo = JSONObject.parseObject(unionIdDemo);
            String userInf = String.valueOf(jo.get("user_info"));
            jo = JSONObject.parseObject(userInf);
            String unionId = String.valueOf(jo.get("unionid"));
            System.out.println(1);
            return unionId;
        } catch (ApiException e) {
            bizLogger.error("getUnionIdUtil failed", e);
            throw new RuntimeException();
        }

    }
}
