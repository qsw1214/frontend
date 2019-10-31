package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;
import cc.mrbird.febs.resource.mapper.SubjectMapper;
import cc.mrbird.febs.resource.service.ISubjectResourceService;
import cc.mrbird.febs.resource.service.ISubjectService;
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
 * @date 2019-08-17 19:45:05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {

    @Autowired
    private SubjectMapper subjectMapper;
    
    @Autowired
    private ISubjectResourceService subjectResourceService;

    @Override
    public IPage<Subject> findSubjects(QueryRequest request, Subject subject) {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(subject.getSubjectName())) {
            queryWrapper.like(Subject::getSubjectName, subject.getSubjectName());
        }
        if (StringUtils.isNotBlank(subject.getCreator())) {
            queryWrapper.eq(Subject::getCreator, subject.getCreator());
        }
        if (subject.getShowStatus() != null) {
            queryWrapper.eq(Subject::getShowStatus, subject.getShowStatus());
        }
        if (subject.getCategoryId() != null) {
            queryWrapper.eq(Subject::getCategoryId, subject.getCategoryId());
        }
        Page<Subject> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Subject> findSubjects(Subject subject) {
	    LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
	    if (StringUtils.isNotBlank(subject.getSubjectName())) {
            queryWrapper.like(Subject::getSubjectName, subject.getSubjectName());
        }
        if (subject.getSubjectId() != null) {
            queryWrapper.eq(Subject::getSubjectId, subject.getSubjectId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSubject(Subject subject) {
    	subject.setCreateTime(new Date());
        this.save(subject);
    }

    @Override
    @Transactional
    public void updateSubject(Subject subject) {
        this.saveOrUpdate(subject);
    }

    @Override
    @Transactional
    public void deleteSubjects(String subjectIds) {
        List<String> list = Arrays.asList(subjectIds.split(StringPool.COMMA));
        if(list.size()>0){
	        this.baseMapper.delete(new QueryWrapper<Subject>().lambda().in(Subject::getSubjectId, list));
	        subjectResourceService.deleteSubjectResourcesBySubjectId(list);
        }
	}

	@Override
	public void increaseReadCount(Long subjectId, Integer num) {
		subjectMapper.increaseReadCount(subjectId, num);;
	}
	
	@Override
	public void increaseResourceCount(Long subjectId, Integer num) {
		subjectMapper.increaseResourceCount(subjectId, num);;
	}
	
	@Override
	public IPage<Resource> findSubjectResources(Long subjectId, Resource resource, QueryRequest request) {
		Page<Resource> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
		return subjectMapper.findSubjectResources(page, subjectId, resource);
	}
}
