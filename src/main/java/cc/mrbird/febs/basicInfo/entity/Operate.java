package cc.mrbird.febs.basicInfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 *  Entity
 *  @author wsq
 */
@Data
@TableName("jcc_operate_desc")
@Excel("操作指南表")
public class Operate {
    /**
     *指南id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField(value = "指南id")
    private Integer id;
    /**
     *标题
     */
    @TableField("title")
    @ExcelField(value = "标题")
    private String title;

    /**
     *作者
     */
    @TableField("author")
    @ExcelField(value = "作者")
    private String author;

    /**
     *上传时间
     */
    @TableField("upload_time")
    @ExcelField(value = "上传时间")
    private Date uploadTime;

    /**
     *更新时间
     */
    @TableField("update_time")
    @ExcelField(value = "更新时间")
    private Date updateTime;

    /**
     *附件地址
     */
    @TableField("attach_address")
    @ExcelField(value = "附件地址")
    private String attachAddress;


}
