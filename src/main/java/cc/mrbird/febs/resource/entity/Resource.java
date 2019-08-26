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
 * @date 2019-08-17 19:44:02
 */
@Data
@TableName("r_resource")
public class Resource {

    /**
     * 资源ID
     */
    @TableId(value = "resource_id", type = IdType.AUTO)
    private Long resourceId;

    /**
     * 资源名称
     */
    @TableField("resource_name")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    private String resourceName;

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
     * 学校
     */
    @TableField("school_id")
    private Integer schoolId;

    /**
     * 年级
     */
    @TableField("grade_id")
    private Integer gradeId;

    /**
     * 科目
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 类别ID
     */
    @TableField("category_id")
    private Long categoryId;
    
    /**
     * 文件类型
     */
    @TableField("file_type")
    @Size(max = 30, message = "{noMoreThan}")
    private String fileType;

    /**
     * 资源图片
     */
    @TableField("pic")
    @Size(max = 255, message = "{noMoreThan}")
    private String pic;

    /**
     * 资源地址
     */
    @TableField("url")
    @NotBlank(message = "{required}")
    @Size(max = 500, message = "{noMoreThan}")
    private String url;
    
    /**
     * 评分：0->5
     */
    @TableField("star")
    private Integer star;

    /**
     * 资源介绍
     */
    @TableField("description")
    @Size(max = 500, message = "{noMoreThan}")
    private String description;

    /**
     * 阅读数
     */
    @TableField("read_count")
    private Integer readCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 排序
     */
    @TableField("order_num")
    private Long orderNum;

    /**
     * 审核状态：0->未审核；1->审核通过；2->审核不通过
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date modifyTime;
    
    /**
     * 类别名称
     */
    @ExcelField(value = "类别名")
    @TableField(exist = false)
    private String categoryName;
    
    /**
     * 学校名称
     */
    @ExcelField(value = "学校名")
    @TableField(exist = false)
    private String schoolName;
    
    /**
     * 年级名称
     */
    @ExcelField(value = "年级名")
    @TableField(exist = false)
    private String gradeName;
    
    /**
     * 科目名称
     */
    @ExcelField(value = "科目名")
    @TableField(exist = false)
    private String subjectName;
    
    @TableField(exist = false)
    private String createTimeFrom;
    @TableField(exist = false)
    private String createTimeTo;

    @TableField(exist = false)
    private Long subjectResourceId;
}