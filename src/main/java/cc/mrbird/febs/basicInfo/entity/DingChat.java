package cc.mrbird.febs.basicInfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("jcc_dingchat")
public class DingChat {

    @TableId(value = "chat_id")
    public String chatId;

    @TableId(value = "chat_name")
    public String chatName;

    @TableId(value = "user_count")
    public Integer userCount;

    @TableId(value = "user_id")
    public String userId;
}
