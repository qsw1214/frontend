package cc.mrbird.febs.basicInfo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  Entity
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
@Data
@TableName("t_news")
public class News {

    /**
     * 
     */
    @TableId(value = "nid")
    private String nid;

    /**
     * 
     */
    @TableField("link")
    private String link;

    /**
     * 
     */
    @TableField("classify")
    private String classify;

    /**
     * 
     */
    @TableField("title")
    private String title;

    @TableField("pic")
    private String pic;


    @TableField("date")
    private String date;
}