package cc.mrbird.febs.search.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.search.entity.KeywordCount;
import cc.mrbird.febs.search.mapper.KeywordCountMapper;
import cc.mrbird.febs.search.service.IKeywordCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 搜索热词统计表 Service实现
 *
 * @author lb
 * @date 2019-09-22 23:15:01
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KeywordCountServiceImpl extends ServiceImpl<KeywordCountMapper, KeywordCount> implements IKeywordCountService {


    @Override
    public IPage<KeywordCount> findKeywordCounts(QueryRequest request, KeywordCount keywordCount) {
        LambdaQueryWrapper<KeywordCount> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<KeywordCount> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findKeywordsByDate(Integer k, Date countDate) {
		return this.baseMapper.findKeywordsByDate(k, countDate);
    }

    @Override
    @Transactional
    public void createKeywordCount(KeywordCount keywordCount) {
        this.save(keywordCount);
    }

    @Override
    @Transactional
    public void updateKeywordCount(KeywordCount keywordCount) {
        this.saveOrUpdate(keywordCount);
    }

    @Override
    @Transactional
    public void deleteKeywordCount(KeywordCount keywordCount) {
        LambdaQueryWrapper<KeywordCount> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}

	@Override
	public KeywordCount getLatestKeyword() {
		LambdaQueryWrapper<KeywordCount> wapper = new LambdaQueryWrapper<>();
		wapper.orderByDesc(KeywordCount::getKeywordCountId);
		wapper.last("LIMIT 1");
		return this.baseMapper.selectOne(wapper);
	}
	
	@Override
	public List<Map<String, Object>> countKeywords(Integer k, Date startDate, Date endDate) {
		return this.baseMapper.countKeywords(k, startDate, endDate);
	}
}
