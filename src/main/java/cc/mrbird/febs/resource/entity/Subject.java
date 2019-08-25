package cc.mrbird.febs.resource.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.ExcelField;

import cc.mrbird.febs.common.converter.TimeConverter;

/**
 *  Entity
 *
 * @author lb
 * @date 2019-08-17 19:45:05
 */
@Data
@TableName("r_subject")
public class Subject {

    /**
     * 专题ID
     */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Long subjectId;

    /**
     * 专题名称
     */
    @TableField("subject_name")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    private String subjectName;

    /**
     * 创建人
     */
    @TableField("creator")
    @Size(max = 50, message = "{noMoreThan}")
    private String creator;
    
    /**
     * 创建人头像
     */
    @TableField("avatar")
    @Size(max = 255, message = "{noMoreThan}")
    private String avatar;

    /**
     * 专题描述
     */
    @TableField("description")
    @Size(max = 1000, message = "{noMoreThan}")
    private String description;

    /**
     * 类别ID
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 阅读数
     */
    @TableField("read_count")
    private Integer readCount;
    
    /**
     * 资源数
     */
    @TableField("resource_count")
    private Integer resourceCount;

    /**
     * 专题图片
     */
    @TableField("pic")
    @Size(max = 255, message = "{noMoreThan}")
    private String pic;

    /**
     * 排序
     */
    @TableField("order_num")
    private Long orderNum;

    /**
     * 显示状态：0->不显示；1->显示
     */
    @TableField("show_status")
    private Integer showStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

}