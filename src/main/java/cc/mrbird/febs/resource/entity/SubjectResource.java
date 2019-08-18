package cc.mrbird.febs.resource.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author lb
 * @date 2019-08-17 19:45:30
 */
@Data
@TableName("r_subject_resource")
public class SubjectResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 专题ID
     */
    @TableField("subject_id")
    private Long subjectId;

    /**
     * 资源ID
     */
    @TableField("resource_id")
    private Long resourceId;

}