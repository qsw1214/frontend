package cc.mrbird.febs.dingding.vo;

import cc.mrbird.febs.system.entity.Role;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * 根据用户ID获取的用户详细数据JSON
 */
@Data
public class UserInfoDetailVO {
    private String errcode;
    private String unionid;
    private String remark;
    private String userid;
    private String isLeaderInDepts;
    private boolean isBoss;
    private Number hiredDate;
    private boolean isSenior;
    private String tel;
    private String workPlace;
    private String email;
    private String orderInDepts;
    private String mobile;
    private String errmsg;
    private boolean active;
    private String avatar;
    private boolean isAdmin;
    private boolean isHide;
    private String jobnumber;
    private String name;
    private String position;
    private List<String> department;
    private List<RolesInfoVO> roles;
}
