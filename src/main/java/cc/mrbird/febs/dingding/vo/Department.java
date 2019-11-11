package cc.mrbird.febs.dingding.vo;

import lombok.Data;

@Data
public class Department {
    private String id;
    private String name;
    private String parentid;
    private boolean createDeptGroup;
    private boolean autoAddUser;
}
