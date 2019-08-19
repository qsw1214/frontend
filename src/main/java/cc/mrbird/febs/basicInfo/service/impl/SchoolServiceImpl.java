package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.mapper.SchoolMapper;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.common.entity.QueryRequest;

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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 学校表 Service实现
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public IPage<School> findSchools(QueryRequest request, School school) {
//        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
//        // TODO 设置查询条件
	    QueryWrapper<School> queryWrapper = new QueryWrapper<>();
	    if (StringUtils.isNotBlank(school.getSchoolName()))
	        queryWrapper.lambda().like(School::getSchoolName, school.getSchoolName());
	    if (StringUtils.isNotBlank(school.getSchoolType()))
	    	queryWrapper.lambda().like(School::getSchoolType, school.getSchoolType());
        Page<School> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<School> findSchools(School school) {
	    LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSchool(School school) {
        this.save(school);
    }

    @Override
    @Transactional
    public void updateSchool(School school) {
        this.saveOrUpdate(school);
    }

//    @Override
//    @Transactional
//    public void deleteSchool(School school) {
//        LambdaQueryWrapper<School> wapper = new LambdaQueryWrapper<>();
//	    // TODO 设置删除条件
//	    this.remove(wapper);
//	}
    @Override
    @Transactional
    public void deleteSchool(String[] schoolIds) {
    	List<String> list = Arrays.asList(schoolIds);
        baseMapper.deleteBatchIds(list);
	}
}
