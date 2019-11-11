package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.util.AccessTokenUtil;
import cc.mrbird.febs.dingding.util.FileUtils;
import cc.mrbird.febs.dingding.util.JsapiTicketUtil;
import cc.mrbird.febs.dingding.util.OApiException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkJsApiSingnature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static cc.mrbird.febs.dingding.util.requestUtil.$params;

/**
 * AccessToken和jsticket的获取封装
 */
@Slf4j
@Validated
@RestController
public class AuthHelper {

    /**
     * 调整到1小时50分钟
     */
    public static final long cacheTime = 1000 * 60 * 55 * 2;

    /**
     * 在此方法中，为了避免频繁获取access_token，
     * 在距离上一次获取access_token时间在两个小时之内的情况，
     * 将直接从持久化存储中读取access_token
     *
     * 因为access_token和jsapi_ticket的过期时间都是7200秒
     * 所以在获取access_token的同时也去获取了jsapi_ticket
     * 注：jsapi_ticket是在前端页面JSAPI做权限验证配置的时候需要使用的
     * 具体信息请查看开发者文档--权限验证配置
     */
    public static String getAccessToken(String appKey,String appSecret) {
        long curTime = System.currentTimeMillis();
        JSONObject accessTokenValue = (JSONObject) FileUtils.getValue("accesstoken", Constant.APPKEY);
        String accToken = "";
        JSONObject jsontemp = new JSONObject();
        if (accessTokenValue == null || curTime - accessTokenValue.getLong("begin_time") >= cacheTime) {
            try {
                //获取AccessToken
                accToken = AccessTokenUtil.getToken(appKey, appSecret);
                // save accessToken
                JSONObject jsonAccess = new JSONObject();
                jsontemp.clear();
                jsontemp.put("access_token", accToken);
                jsontemp.put("begin_time", curTime);
                jsonAccess.put(Constant.APPKEY, jsontemp);
                //真实项目中最好保存到数据库中
                FileUtils.write2File(jsonAccess, "accesstoken");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return accessTokenValue.getString("access_token");
        }
        return accToken;
    }

    /**
     * 获取JSTicket, 用于js的签名计算
     * 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
     */
    public static String getJsapiTicket(String accessToken) throws OApiException {
        JSONObject jsTicketValue = (JSONObject) FileUtils.getValue("jsticket", Constant.APPKEY);
        long curTime = System.currentTimeMillis();
        String jsTicket = "";

        if (jsTicketValue == null || curTime -
                jsTicketValue.getLong("begin_time") >= cacheTime) {
            try {
                //获取jsapiTicket
                jsTicket = JsapiTicketUtil.getToken(accessToken);

                JSONObject jsonTicket = new JSONObject();
                JSONObject jsontemp = new JSONObject();
                jsontemp.clear();
                jsontemp.put("ticket", jsTicket);
                jsontemp.put("begin_time", curTime);
                jsonTicket.put(Constant.APPKEY, jsontemp);
                FileUtils.write2File(jsonTicket, "jsticket");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsTicket;
        } else {
            return jsTicketValue.getString("ticket");
        }
    }

    public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws OApiException {
        try {
            return DingTalkJsApiSingnature.getJsApiSingnature(url, nonceStr, timeStamp, ticket);
        } catch (Exception ex) {
            throw new OApiException(0, ex.getMessage());
        }
    }

    /**
     * 计算当前请求的jsapi的签名数据<br/>
     * <p>
     * 如果签名数据是通过ajax异步请求的话，签名计算中的url必须是给用户展示页面的url
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "dingChat",method = RequestMethod.GET)
    public static String getConfig(HttpServletRequest request,String urlString) throws UnsupportedEncodingException {
        //String urlString = request.getRequestURL().toString();
        Map params = $params(request);
        String queryString = String.valueOf(params.get("urlString"));


        String queryStringEncode = null;
        String url;
        if (queryString != null) {
            url = URLDecoder.decode(queryString,"utf-8");
        } else {
            url = URLDecoder.decode(queryString,"utf-8");
        }
        /**
         * 确认url与配置的应用首页地址一致
         */
        System.out.println(url);

        /**
         * 随机字符串
         */
        String nonceStr = "abcdefg";
        long timeStamp = System.currentTimeMillis() / 1000;
        String signedUrl = url;
        String accessToken = null;
        String ticket = null;
        String signature = null;

        try {
            accessToken = AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET);

            ticket = AuthHelper.getJsapiTicket(accessToken);
            System.out.println("ticket="+ticket);
            signature = AuthHelper.sign(ticket, nonceStr, timeStamp, signedUrl);

        } catch (OApiException e) {
            e.printStackTrace();
        }

        Map<String, Object> configValue = new HashMap<>();
        configValue.put("jsticket", ticket);
        configValue.put("signature", signature);
        configValue.put("nonceStr", nonceStr);
        configValue.put("timeStamp", timeStamp);
        configValue.put("corpId", Constant.CORP_ID);
        configValue.put("agentId", Constant.AGENTID);

        String config = JSON.toJSONString(configValue);
        return config;
    }

    public static String getSsoToken() throws OApiException {
        String url = "https://oapi.dingtalk.com/sso/gettoken?corpid=" + Constant.CORP_ID + "&corpsecret=" + Constant.SSO_Secret;
        JSONObject response = HttpHelper.httpGet(url);
        String ssoToken;
        if (response.containsKey("access_token")) {
            ssoToken = response.getString("access_token");
        } else {
            throw new OApiException("Sso_token");
        }
        return ssoToken;

    }
}
