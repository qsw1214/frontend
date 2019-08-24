package cc.mrbird.febs.basicInfo.entity;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-22 17:35:44
 */
@Data
@TableName("jcc_class_info")
public class ClassInfo {

    /**
     * 
     */
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer classId;

    /**
     * 
     */
    @TableField("class_name")
    @NotBlank(message = "{required}")
    private String className;

    /**
     * 
     */
    @TableField("username")
    private String username;

    /**
     * 
     */
    @TableField("grade")
    private String grade;

    /**
     * 
     */
    @TableField("school_id")
    @NotNull(message = "{required}")
    private Integer schoolId;

}