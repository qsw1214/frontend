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
 * @date 2019-08-17 19:45:42
 */
@Data
@TableName("r_comment")
public class Comment {

    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 资源ID
     */
    @TableField("resource_id")
    private Long resourceId;

    /**
     * 评论人
     */
    @TableField("user_name")
    @Size(max = 50, message = "{noMoreThan}")
    private String userName;

    /**
     * 评价的ip
     */
    @TableField("user_ip")
    @Size(max = 64, message = "{noMoreThan}")
    private String userIp;

    /**
     * 评论用户头像
     */
    @TableField("user_avatar")
    @Size(max = 255, message = "{noMoreThan}")
    private String userAvatar;

    /**
     * 评分：0->5
     */
    @TableField("star")
    private Integer star;

    /**
     * 评论内容
     */
    @TableField("content")
    @NotBlank(message = "{required}")
    @Size(max = 1000, message = "{noMoreThan}")
    private String content;

    /**
     * 回复数量
     */
    @TableField("replay_count")
    private Integer replayCount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

}