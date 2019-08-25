package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.Operate;
import cc.mrbird.febs.basicInfo.mapper.OperateMapper;
import cc.mrbird.febs.basicInfo.service.IOpertaeService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 *  Service实现
 *
 * @author wsq
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OperateServiceImpl extends ServiceImpl<OperateMapper, Operate> implements IOpertaeService {
    @Autowired
    private OperateMapper operateMapper;

    @Override
    public IPage<Operate> findOperate(QueryRequest request, Operate operate) {
        QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(operate.getAuthor()))
            queryWrapper.lambda().eq(Operate::getAuthor, operate.getAuthor());
        if (StringUtils.isNotBlank(operate.getTitle()))
            queryWrapper.lambda().eq(Operate::getTitle, operate.getTitle());
        Page<Operate> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Operate> findOperate(Operate operate) {
        LambdaQueryWrapper<Operate> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createOperate(Operate operate) {
        this.save(operate);
    }

    @Override
    @Transactional
    public void updateOperate(Operate operate) {
        this.updateById(operate);
    }

    @Override
    @Transactional
    public void deleteOperate(String id) {
        List<String> list = Arrays.asList(id.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Operate>().lambda().in(Operate::getId, list));
    }
}
