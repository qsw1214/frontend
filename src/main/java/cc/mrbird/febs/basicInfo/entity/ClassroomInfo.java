package cc.mrbird.febs.basicInfo.entity;


import lombok.Data;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-23 15:45:24
 */
@Data
@TableName("jcc_classroom_info")
public class ClassroomInfo {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField("school_id")
    @NotBlank(message = "{required}")
    private Integer schoolId;

    /**
     * 
     */
    @TableField("location")
    private String location;

    /**
     * 
     */
    @TableField("contain_num")
    private Integer containNum;

    /**
     * 
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 
     */
    @TableField("url")
    private String url;

    /**
     * 
     */
    @TableField("state")
    private Integer state;

    /**
     * 
     */
    @TableField("subject")
    private String subject;

    /**
     * 
     */
    @TableField("garde")
    private String garde;

    /**
     * 
     */
    @TableField("order_num")
    private Long orderNum;

}