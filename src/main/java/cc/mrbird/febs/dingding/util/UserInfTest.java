package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.URLConstant;
import cc.mrbird.febs.dingding.controller.AuthHelper;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetUseridByUnionidRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetUseridByUnionidResponse;
import com.taobao.api.ApiException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static cc.mrbird.febs.dingding.config.URLConstant.URL_GET_TOKKEN;
import static cc.mrbird.febs.dingding.config.URLConstant.URL_GET_USER_INFO_BYCODE;

public class UserInfTest {
    public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        /**
         * 获取signature,详情看钉钉文档
         */
		/*String stringToSign = "1564710568000"+"\n" + "1314";
		Mac mac = Mac.getInstance("HmacSHA256");
		String suitSeccret = "QZRYu6IkbOJ-RG31ARzhqLwLKSc4iKnB1OWFTlXXz2Mcd5-ykoqx0JCqHO41o0fv";
		mac.init(new SecretKeySpec(suitSeccret.getBytes("UTF-8"), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		//return new String(Base64.encodeBase64(signData));
		String s = new String(Base64.encodeBase64(signData));


		try {
	        String encoded = URLEncoder.encode(s, "UTF-8");
	        String finalCode = encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
	        System.out.print(finalCode);
	        String finalCode2 = encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
	    } catch (UnsupportedEncodingException e) {
	        throw new IllegalArgumentException("FailedToEncodeUri", e);
	    }*/
        /**
         * 获取unionId
         * 用户授权的临时授权码code，只能使用一次；在前面步骤中跳转到redirect_uri时会追加code参数
         */
        String unionId = "";
        DefaultDingTalkClient  client2 = new DefaultDingTalkClient(URL_GET_USER_INFO_BYCODE);
        OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
        req.setTmpAuthCode("0a7aa16588133390b561572d9f40ea20");
        try {
            //OapiSnsGetuserinfoBycodeResponse response2 = client2.execute(req,"yourAppId","yourAppSecret");
            //相应登录，对应登录的APPID,APPSecret
            OapiSnsGetuserinfoBycodeResponse response2 = client2.execute(req, Constant.ACCESSKEY, Constant.ACCESSSECRET);
            String unionIdDemo = response2.getBody();
            JSONObject jo = JSONObject.parseObject(unionIdDemo);
            String userInf = String.valueOf(jo.get("user_info"));
            jo = JSONObject.parseObject(userInf);
            unionId = String.valueOf(jo.get("unionid"));
            System.out.println(1);
        } catch (ApiException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /**
         * 获取accessToken
         * 【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
         */
        DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        //企业内部应用的appkey
        request.setAppkey(Constant.APPKEY);
        //企业内部应用的appSecret
        request.setAppsecret(Constant.APPSECRET);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            String s = response.getBody();
            //针对不同jar包的转json写法
            JSONObject jo = JSONObject.parseObject(new String(s));
            //获取access_token
            //String accessToken = String.valueOf(jo.get("access_token"));
            String accessToken = AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET);
            //通过access_token和unionId获取userId
            DingTalkClient clientUserId = new DefaultDingTalkClient(URLConstant.URL_GET_USERID_BY_UNIONID);
            OapiUserGetUseridByUnionidRequest requestUserId = new OapiUserGetUseridByUnionidRequest();
            //这个unionID
            requestUserId.setUnionid(unionId);
            requestUserId.setHttpMethod("GET");

            OapiUserGetUseridByUnionidResponse responseUserId = clientUserId.execute(requestUserId, accessToken);
            String userIdBody = responseUserId.getBody();
            jo = JSONObject.parseObject(new String(userIdBody));
            String userId = String.valueOf(jo.get("userid"));
            //通过userId获取用户详情
            DingTalkClient clientUserInf = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest requestUserInf = new OapiUserGetRequest();
            requestUserInf.setUserid(userId);
            requestUserInf.setHttpMethod("GET");
            OapiUserGetResponse responseUserInf = clientUserInf.execute(requestUserInf, accessToken);
            String userInfBody = responseUserInf.getBody();
            jo = JSONObject.parseObject(new String(userInfBody));
            //通过用户详情里的权限去绑定自已系统的权限{"roles":[{"groupName":"默认","id":475171896,"name":"主管理员","type":101}]}
            String roles = String.valueOf(jo.get("roles"));
            String roles2 = roles.substring(1, roles.length()-1);
            JSONObject rolesData = JSONObject.parseObject(roles2);

        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
