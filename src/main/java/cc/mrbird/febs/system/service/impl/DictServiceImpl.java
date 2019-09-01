package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.mapper.DictMapper;
import cc.mrbird.febs.system.service.IDictService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 *  Service实现
 *
 * @author lb
 * @date 2019-08-17 19:44:52
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Override
    public IPage<Dict> findDicts(QueryRequest request, Dict dict) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(dict.getField())) {
            queryWrapper.eq(Dict::getField, dict.getField());
        }
        Page<Dict> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<Dict> findDictsByField(String field) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isBlank(field)) {
            return new ArrayList<>();
        }
        queryWrapper.eq(Dict::getField, field);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Dict> findDicts(Dict dict) {
	    LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
	    if (StringUtils.isNotBlank(dict.getField())) {
            queryWrapper.eq(Dict::getField, dict.getField());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDict(Dict dict) {
        this.save(dict);
    }

    @Override
    @Transactional
    public void updateDict(Dict dict) {
        this.saveOrUpdate(dict);
    }

    @Override
    @Transactional
    public void deleteDicts(String dictIds) {
    	List<String> list = Arrays.asList(dictIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Dict>().lambda().in(Dict::getDictId, list));
	}
}
