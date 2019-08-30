package cc.mrbird.febs.dingding.controller;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.URLConstant;
import cc.mrbird.febs.dingding.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackDeleteCallBackRequest;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.request.OapiCallBackRegisterCallBackRequest;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.dingtalk.api.response.OapiCallBackRegisterCallBackResponse;
import com.taobao.api.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

import static cc.mrbird.febs.dingding.controller.CeShi.$params;

public class CeShiController {
    /**
     * 审批接口测试
     */
    public static void main(String[] args) throws ApiException {
        // 先删除企业已有的回调
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.DELETE_CALLBACK);
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        client.execute(request, AccessTokenUtil.getToken());

        // 重新为企业注册回调
        client = new DefaultDingTalkClient(URLConstant.REGISTER_CALLBACK);
        OapiCallBackRegisterCallBackRequest registerRequest = new OapiCallBackRegisterCallBackRequest();
        //设置回调路径
        registerRequest.setUrl(Constant.CALLBACK_URL_HOST+"/callback");
        //数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成
        registerRequest.setAesKey(Constant.ENCODING_AES_KEY);
        //加解密需要用到的token，企业可以随机填写。如 "12345"
        registerRequest.setToken(Constant.TOKEN);
        registerRequest.setCallBackTag(Arrays.asList("bpms_instance_change", "bpms_task_change"));
        OapiCallBackRegisterCallBackResponse registerResponse = client.execute(registerRequest,AccessTokenUtil.getToken());
        if (registerResponse.isSuccess()) {
            System.out.println("回调注册成功了！！！");
        }
       /* DingTalkClient  client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back");
        OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
        request.setHttpMethod("GET");
        OapiCallBackGetCallBackResponse response = client.execute(request,AccessTokenUtil.getToken());
        String userIdBody = response.getBody();
        System.out.println(userIdBody);*/
    }
}
