package cc.mrbird.febs.system.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户部门关联表 Entity
 *
 * @author lb
 * @date 2019-09-08 16:53:13
 */
@Data
@TableName("t_user_dept")
public class UserDept {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;

}