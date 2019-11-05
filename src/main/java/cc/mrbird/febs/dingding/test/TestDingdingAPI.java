package cc.mrbird.febs.dingding.test;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiGettokenResponse;

import java.util.Arrays;

/**
 * 钉钉群功能的测试；
 */
public class TestDingdingAPI {

    public static void main(String[] args){
        DefaultDingTalkClient client = null;
        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey("dingpefn0cpgo4bdoqqm");
        request.setAppsecret("LPZOM7ttqsP6mIpGgL1j3JZ9iJ_SmKiyRZ_tRhgFXNZ-KJ_FRtw4Kb16zJFKT6CD");
        request.setHttpMethod("GET");
        OapiGettokenResponse response = null;
        try{
            response = client.execute(request);
            System.out.println(response.getAccessToken());
        }catch(Exception e){

        }

        //创建群
        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/create");
        OapiChatCreateRequest requestOapiChat = new OapiChatCreateRequest();
        requestOapiChat.setName("聊天打屁测试群");
        requestOapiChat.setOwner("013461004124275852");
        requestOapiChat.setUseridlist(Arrays.asList("013461004124275852","102752341220899098","283922183129482430","176415581621823364"));
        requestOapiChat.setShowHistoryType(1L);
        OapiChatCreateResponse chatCreateResponse = null;
        try{
            chatCreateResponse = client.execute(requestOapiChat,"5e30e557a73d3014976b754a27f84ef0");
            System.out.println(chatCreateResponse.getChatid());
        }catch(Exception e){
        }

        client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest chatRequest = new OapiChatSendRequest();
        chatRequest.setChatid(chatCreateResponse.getChatid());

        for(int i = 0 ; i < 100; i++){
            OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
            msg.setMsgtype("text");
            OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
            text.setContent("海同意的请+ " + i);
            msg.setText(text);
            chatRequest.setMsg(msg);
            try{
                OapiChatSendResponse sendResponse = client.execute(chatRequest, "5e30e557a73d3014976b754a27f84ef0");
                System.out.println(sendResponse.getMessageId());
            }catch(Exception e){

            }
        }

    }
}
