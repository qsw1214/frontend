package cc.mrbird.febs.dingding.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    private String CorpId;
    private String EventType;
    private List<Long> UserId;
    private String TimeStamp;
    private List<Long> UserIdList;
}
