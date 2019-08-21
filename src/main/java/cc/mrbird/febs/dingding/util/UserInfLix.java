package cc.mrbird.febs.dingding.util;

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
            String unionId = UnionIdUtil.getUnionId(req);
             /* 获取accessToken
            【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
            */
            //针对不同jar包的转json写法
            JSONObject jo = JSONObject.parseObject(new String(""));
            //获取access_token
            String accessToken = AccessTokenUtil.getToken();
            //通过access_token和unionId获取userId
            String userId = UserIdUtil.getUserId(unionId);
            //通过userId获取用户详情
            JSONObject jsonObject = UserInfUtil.getUserInf(userId);
            //通过用户详情里的权限去绑定自已系统的权限{"roles":[{"groupName":"默认","id":475171896,"name":"主管理员","type":101}]}
           /* String roles = String.valueOf(jsonObject.get("roles"));
            String roles2 = roles.substring(1, roles.length() - 1);
            JSONObject rolesData = JSONObject.parseObject(roles2);*/
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
