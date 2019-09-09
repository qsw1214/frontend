package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MrBird
 */
@Data
@TableName("t_user_role")
public class UserDept implements Serializable {

    private static final long serialVersionUID = 1321465;
    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 部门ID
     */
    @TableField("DEPT_ID")
    private Long deptId;


}