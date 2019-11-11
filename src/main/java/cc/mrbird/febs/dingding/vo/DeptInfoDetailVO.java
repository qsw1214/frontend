package cc.mrbird.febs.dingding.vo;

import lombok.Data;

/**
 * 根据部门ID获取的部门详细数据JSON
 */
@Data
public class DeptInfoDetailVO {

    private int errcode;
    private String userPermits;
    private String userPerimits;
    private boolean outerDept;
    private String errmsg;
    private String deptManagerUseridList;
    private String parentid;
    private boolean groupContainSubDept;
    private String outerPermitUsers;
    private String outerPermitDepts;
    private String deptPerimits;
    private boolean createDeptGroup;
    private String name;
    private String id;
    private boolean autoAddUser;
    private boolean deptHiding;
    private String deptPermits;
    private String order;
}
