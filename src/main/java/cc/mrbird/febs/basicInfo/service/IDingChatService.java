package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.entity.DingChat;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-11-06 08:37:08
 */
public interface IDingChatService extends IService<DingChat> {
    /**
     * 新增
     * @param dingChat dingChat
     */
    void createDingChat(DingChat dingChat);

    List<DingChat> getDingChatList();

}
