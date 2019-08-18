package cc.mrbird.febs.system.entity;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author lb
 * @date 2019-08-17 19:44:52
 */
@Data
@TableName("t_dict")
public class Dict {

    /**
     * 字典ID
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 键
     */
    @TableField("k")
    @Size(max = 50, message = "{noMoreThan}")
    private String k;

    /**
     * 值
     */
    @TableField("v")
    @NotBlank(message = "{required}")
    @Size(max = 100, message = "{noMoreThan}")
    private String v;

    /**
     * 字段名称
     */
    @TableField("field")
    @NotBlank(message = "{required}")
    @Size(max = 100, message = "{noMoreThan}")
    private String field;

}