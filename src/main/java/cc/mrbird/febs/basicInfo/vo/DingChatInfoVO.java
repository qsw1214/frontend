package cc.mrbird.febs.basicInfo.vo;

import lombok.Data;

@Data
public class DingChatInfoVO {
    private int errcode;

    private String errmsg;

    private Chat_info chat_info;
}
