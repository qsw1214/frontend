package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.mapper.ResourceMapper;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.resource.service.ISubjectResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
 * @date 2019-08-17 19:44:02
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ISubjectResourceService subjectResourceService;
    @Autowired
    private ICommentService commentService;

    @Override
    public IPage<Resource> findResources(QueryRequest request, Resource resource) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(resource.getResourceName())) {
            queryWrapper.like(Resource::getResourceName, resource.getResourceName());
        }
        if (StringUtils.isNotBlank(resource.getCreator())) {
            queryWrapper.eq(Resource::getCreator, resource.getCreator());
        }
        if (StringUtils.isNotBlank(resource.getSchool())) {
            queryWrapper.eq(Resource::getSchool, resource.getSchool());
        }
        if (StringUtils.isNotBlank(resource.getSubject())) {
            queryWrapper.eq(Resource::getSubject, resource.getSubject());
        }
        if (StringUtils.isNotBlank(resource.getGrade())) {
            queryWrapper.eq(Resource::getGrade, resource.getGrade());
        }
        if (resource.getStatus() != null) {
            queryWrapper.eq(Resource::getStatus, resource.getStatus());
        }
        if (resource.getCategoryId() != null) {
            queryWrapper.eq(Resource::getCategoryId, resource.getCategoryId());
        }
        Page<Resource> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Resource> findResources(Resource resource) {
	    LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
	    if (StringUtils.isNotBlank(resource.getResourceName())) {
            queryWrapper.like(Resource::getResourceName, resource.getResourceName());
        }
	    if (StringUtils.isNotBlank(resource.getCreator())) {
            queryWrapper.eq(Resource::getCreator, resource.getCreator());
        }
        if (StringUtils.isNotBlank(resource.getSchool())) {
            queryWrapper.eq(Resource::getSchool, resource.getSchool());
        }
        if (StringUtils.isNotBlank(resource.getSubject())) {
            queryWrapper.eq(Resource::getSubject, resource.getSubject());
        }
        if (StringUtils.isNotBlank(resource.getGrade())) {
            queryWrapper.eq(Resource::getGrade, resource.getGrade());
        }
        if (resource.getCategoryId() != null) {
            queryWrapper.eq(Resource::getCategoryId, resource.getCategoryId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createResource(Resource resource) {
    	resource.setCreateTime(new Date());
        this.save(resource);
    }

    @Override
    @Transactional
    public void updateResource(Resource resource) {
    	resource.setModifyTime(new Date());
        this.saveOrUpdate(resource);
    }

    @Override
    @Transactional
    public void deleteResources(String resourceIds) {
    	List<String> list = Arrays.asList(resourceIds.split(StringPool.COMMA));
    	if(list.size()>0){
	        this.baseMapper.delete(new QueryWrapper<Resource>().lambda().in(Resource::getResourceId, list));
	        subjectResourceService.deleteSubjectResourcesByResourceId(list); // 删除专题关联
	        commentService.deleteCommentsByResourceId(list); // 删除评论
    	}
	}

	@Override
	public void increaseCommentCount(Long resourceId, Integer num) {
		resourceMapper.increaseCommentCount(resourceId, num);
	}

	@Override
	public void increaseReadCount(Long resourceId, Integer num) {
		resourceMapper.increaseReadCount(resourceId, num);
	}
}
