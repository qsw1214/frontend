package cc.mrbird.febs.basicInfo.vo;

import lombok.Data;

import java.util.List;

@Data
public class Chat_info {

    private String name;

    private String owner;

    private String chatid;

    private int conversationTag;

    private List<String> useridlist;
}
