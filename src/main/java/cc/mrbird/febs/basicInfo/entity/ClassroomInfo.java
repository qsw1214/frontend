package cc.mrbird.febs.basicInfo.entity;


import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "{required}")
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

    @TableField("play_url")
    private String playUrl;

    /**
     * 上课状态 -1 0 1 已结束  未开始  进行中
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
    @TableField("grade")
    private String grade;

    @ExcelField(value = "学校名称")
    @TableField(exist = false)
    private String schoolName;
}