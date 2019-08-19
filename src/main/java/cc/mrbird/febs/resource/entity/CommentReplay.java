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
 * @date 2019-08-17 19:46:00
 */
@Data
@TableName("r_comment_replay")
public class CommentReplay {

    /**
     * 回复ID
     */
    @TableId(value = "comment_replay_id", type = IdType.AUTO)
    private Long commentReplayId;

    /**
     * 评论ID
     */
    @TableField("comment_id")
    private Long commentId;

    /**
     * 回复人
     */
    @TableField("user_name")
    @Size(max = 50, message = "{noMoreThan}")
    private String userName;

    /**
     * 回复人头像
     */
    @TableField("user_avatar")
    @Size(max = 255, message = "{noMoreThan}")
    private String userAvatar;

    /**
     * 回复内容
     */
    @TableField("content")
    @NotBlank(message = "{required}")
    @Size(max = 1000, message = "{noMoreThan}")
    private String content;

    /**
     * 回复时间
     */
    @TableField("create_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

}