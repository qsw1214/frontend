package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.entity.DingChat;
import cc.mrbird.febs.basicInfo.mapper.AreaMapper;
import cc.mrbird.febs.basicInfo.mapper.DingChatMapper;
import cc.mrbird.febs.basicInfo.service.IAreaService;
import cc.mrbird.febs.basicInfo.service.IDingChatService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 *  Service实现
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DingChatServiceImpl extends ServiceImpl<DingChatMapper, DingChat> implements IDingChatService {

    @Override
    @Transactional
    public void createDingChat(DingChat dingChat) {
        this.save(dingChat);
    }

    public List<DingChat> getDingChatList(){
        LambdaQueryWrapper<DingChat> queryWrapper = new LambdaQueryWrapper<DingChat>();
        return this.baseMapper.selectList(queryWrapper);
    }
}
