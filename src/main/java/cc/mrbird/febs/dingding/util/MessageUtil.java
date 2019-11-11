package cc.mrbird.febs.dingding.util;

import cc.mrbird.febs.dingding.config.Constant;
import cc.mrbird.febs.dingding.config.URLConstant;

import cc.mrbird.febs.dingding.controller.AuthHelper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 发信息
 */
public class MessageUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(MessageUtil.class);

    public static void sendMessageToOriginator(String processInstanceId) throws RuntimeException {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_GET);
            OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
            //审批实例id
            request.setProcessInstanceId(processInstanceId);
            OapiProcessinstanceGetResponse response = client.execute(request, AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET));
            //发起人,接受人
            String recieverUserId = response.getProcessInstance().getOriginatorUserid();

            client = new DefaultDingTalkClient(URLConstant.MESSAGE_ASYNCSEND);

            OapiMessageCorpconversationAsyncsendV2Request messageRequest = new OapiMessageCorpconversationAsyncsendV2Request();
            messageRequest.setUseridList(recieverUserId);
            messageRequest.setAgentId(Constant.AGENTID);
            messageRequest.setToAllUser(false);

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("text");
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent("代换课申请通过了!!!");
            messageRequest.setMsg(msg);

            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(messageRequest,AuthHelper.getAccessToken(Constant.APPKEY,Constant.APPSECRET));
        } catch (ApiException e) {
            bizLogger.error("send message failed", e);
            throw new RuntimeException();
        }
    }
}
