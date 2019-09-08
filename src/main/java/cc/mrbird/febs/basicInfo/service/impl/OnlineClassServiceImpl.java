package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.OnlineClass;
import cc.mrbird.febs.basicInfo.mapper.OnlineClassMapper;
import cc.mrbird.febs.basicInfo.service.IOnlineClassService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Service实现
 *
 * @author psy
 * @date 2019-08-30 08:37:08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OnlineClassServiceImpl extends ServiceImpl<OnlineClassMapper, OnlineClass> implements IOnlineClassService {

    @Override
    public IPage<OnlineClass> findOnlineClass(QueryRequest request, OnlineClass onlineClass) {
        Page<OnlineClass> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.findOnlineClassInfos(page, onlineClass);
    }
}
