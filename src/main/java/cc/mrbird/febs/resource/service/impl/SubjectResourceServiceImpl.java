package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.SubjectResource;
import cc.mrbird.febs.resource.mapper.SubjectResourceMapper;
import cc.mrbird.febs.resource.service.ISubjectResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Arrays;
import java.util.List;
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
 * @date 2019-08-17 19:45:30
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SubjectResourceServiceImpl extends ServiceImpl<SubjectResourceMapper, SubjectResource> implements ISubjectResourceService {
	
    @Override
    public IPage<SubjectResource> findSubjectResources(QueryRequest request, SubjectResource subjectResource) {
        LambdaQueryWrapper<SubjectResource> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<SubjectResource> page = new Page<>(request.getPageNum(), request.getPageSize());
        if (subjectResource.getResourceId() != null) {
            queryWrapper.eq(SubjectResource::getResourceId, subjectResource.getResourceId());
        }
        return this.page(page, queryWrapper);
    }

    @Override
    public List<SubjectResource> findSubjectResources(SubjectResource subjectResource) {
	    LambdaQueryWrapper<SubjectResource> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
	    if (subjectResource.getResourceId() != null) {
            queryWrapper.eq(SubjectResource::getResourceId, subjectResource.getResourceId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSubjectResource(SubjectResource subjectResource) {
        this.save(subjectResource);
    }

    @Override
    @Transactional
    public void updateSubjectResource(SubjectResource subjectResource) {
        this.saveOrUpdate(subjectResource);
    }

    @Override
    @Transactional
    public void deleteSubjectResources(String subjectResourceIds) {
    	List<String> list = Arrays.asList(subjectResourceIds.split(StringPool.COMMA));
    	if(list.size()>0)
    		this.baseMapper.delete(new QueryWrapper<SubjectResource>().lambda().in(SubjectResource::getId, list));
	}

	@Override
	public void deleteSubjectResourcesBySubjectId(List<String> subjectIds) {
		if(subjectIds.size()>0)
			this.baseMapper.delete(new QueryWrapper<SubjectResource>().lambda().in(SubjectResource::getSubjectId, subjectIds));
	}

	@Override
	public void deleteSubjectResourcesByResourceId(List<String> resourceIds) {
		if(resourceIds.size()>0)
			this.baseMapper.delete(new QueryWrapper<SubjectResource>().lambda().in(SubjectResource::getResourceId, resourceIds));
	}
}
