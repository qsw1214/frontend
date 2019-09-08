package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.mapper.ClassInfoMapper;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.SubjectResource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Arrays;
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
 * @author psy
 * @date 2019-08-22 17:35:44
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements IClassInfoService {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Override
    public IPage<ClassInfo> findClassInfos(QueryRequest request, ClassInfo classInfo) {
        LambdaQueryWrapper<ClassInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(classInfo.getClassName())) {
            queryWrapper.eq(ClassInfo::getClassName, classInfo.getClassName());
        }
        if (StringUtils.isNotBlank(classInfo.getGrade())) {
            queryWrapper.eq(ClassInfo::getGrade, classInfo.getGrade());
        }
        if (classInfo.getSchoolId() != null) {
            queryWrapper.eq(ClassInfo::getSchoolId, classInfo.getSchoolId());
        }
        Page<ClassInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ClassInfo> findClassInfos(ClassInfo classInfo) {
	    LambdaQueryWrapper<ClassInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(classInfo.getClassName())) {
            queryWrapper.eq(ClassInfo::getClassName, classInfo.getClassName());
        }
        if (StringUtils.isNotBlank(classInfo.getGrade())) {
            queryWrapper.eq(ClassInfo::getGrade, classInfo.getGrade());
        }
        if (classInfo.getSchoolId() != null) {
            queryWrapper.eq(ClassInfo::getSchoolId, classInfo.getSchoolId());
        }
        if (classInfo.getClassId() != null) {
            queryWrapper.eq(ClassInfo::getClassId, classInfo.getClassId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createClassInfo(ClassInfo classInfo) {
        this.save(classInfo);
    }

    @Override
    @Transactional
    public void updateClassInfo(ClassInfo classInfo) {
        this.saveOrUpdate(classInfo);
    }
    
    @Override
    @Transactional
    public void deleteClassInfo(String classIds) {
    	List<String> list = Arrays.asList(classIds.split(StringPool.COMMA));
        baseMapper.deleteBatchIds(list);
	}
    
	@Override
	public void deleteClassInfosByschoolId(List<String> schoolIds) {
		if(schoolIds.size()>0)
			this.baseMapper.delete(new QueryWrapper<ClassInfo>().lambda().in(ClassInfo::getSchoolId, schoolIds));
	}
}
