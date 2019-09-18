package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInfLix {


    private static final Logger bizLogger = LoggerFactory.getLogger(UserInfLix.class);

    public static Map getUserInf(String tmpAuthCode) throws RuntimeException {
        try {
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(tmpAuthCode);
            String unionId = UnionIdUtil.getUnionId(req, Constant.ACCESSKEY,Constant.ACCESSSECRET);
             /* 获取accessToken
            【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
            */
            //获取access_token
            String accessToken = AccessTokenUtil.getToken(Constant.APPKEY,Constant.APPSECRET);
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
}
