package cc.mrbird.febs.dingding.vo;

import lombok.Data;

import java.util.List;

/**
 * 钉钉返回的部门实体JSON
 */
@Data
public class DeptInfoVO {

    private String CorpId;
    private String EventType;
    private List<String> DeptId;
    private String TimeStamp;
}
