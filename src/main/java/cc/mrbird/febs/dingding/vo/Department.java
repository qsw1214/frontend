package cc.mrbird.febs.dingding.vo;

import lombok.Data;

@Data
public class Department {
    private long id;
    private String name;
    private long parentid;
    private boolean createDeptGroup;
    private boolean autoAddUser;
}
