package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.mapper.SchoolMapper;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Resource;

import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.service.IDeptService;
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
 * 学校表 Service实现
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Autowired
    private IClassInfoService classInfoService;
  
    @Autowired
    private IClassroomInfoService classroomInfoService;
    
    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private IDeptService deptService;

    @Override
    public IPage<School> findSchools(QueryRequest request, School school) {
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
//        if (school.getSchoolId() != 62) {
//        	queryWrapper.eq(School::getSchoolId, school.getSchoolId());
//        }
        if (StringUtils.isNotBlank(school.getSchoolName())) {
            queryWrapper.eq(School::getSchoolName, school.getSchoolName());
        }
        if (StringUtils.isNotBlank(school.getSchoolType())) {
            queryWrapper.eq(School::getSchoolType, school.getSchoolType());
        }
        if (StringUtils.isNotBlank(school.getSchoolCategory())) {
            queryWrapper.eq(School::getSchoolCategory, school.getSchoolCategory());
        }
        if (school.getState() != null) {
            queryWrapper.eq(School::getState, school.getState());
        }
        Page<School> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<School> findSchools(School school) {
	    LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(school.getSchoolName())) {
            queryWrapper.eq(School::getSchoolName, school.getSchoolName());
        }
        if (StringUtils.isNotBlank(school.getSchoolType())) {
            queryWrapper.eq(School::getSchoolType, school.getSchoolType());
        }
        if (StringUtils.isNotBlank(school.getSchoolCategory())) {
            queryWrapper.eq(School::getSchoolCategory, school.getSchoolCategory());
        }
        if (school.getState() != null) {
            queryWrapper.eq(School::getState, school.getState());
        }
        if (school.getSchoolId() != null) {
            queryWrapper.eq(School::getSchoolId, school.getSchoolId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<School> findSchoolsByName(String schoolName) {
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(schoolName)) {
            queryWrapper.eq(School::getSchoolName, schoolName);
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSchool(School school) {
    	school.setCreateTime(new Date());
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
    public void deleteSchool(String schoolIds) {
    	List<String> list = Arrays.asList(schoolIds.split(StringPool.COMMA));
    	if(list.size()>0){
//	        classInfoService.deleteClassInfosByschoolId(list); // 删除班级关联
//	        classroomInfoService.deleteClassroomInfosByschoolId(list);// 删除教室关联
//	        deviceInfoService.deleteDeviceInfoByschoolId(list);// 删除教室设备关联
	        this.baseMapper.delete(new QueryWrapper<School>().lambda().in(School::getSchoolId, list));

    	}
	}

    public Integer getCountOfCity(String cityName){
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(cityName)) {
            LambdaQueryWrapper<Dept> queryDeptWrapper = new LambdaQueryWrapper<>();
            queryDeptWrapper.eq(Dept::getDeptName, cityName);
            Dept dept = this.deptService.getOne(queryDeptWrapper);
            queryWrapper.eq(School::getCityDeptId, dept.getDeptId());
        }
        return this.baseMapper.selectCount(queryWrapper);
    }

    public Integer getCountOfCountry(String countryName){
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(countryName)) {
            LambdaQueryWrapper<Dept> queryDeptWrapper = new LambdaQueryWrapper<>();
            queryDeptWrapper.eq(Dept::getDeptName, countryName);
            Dept dept = this.deptService.getOne(queryDeptWrapper);
            queryWrapper.eq(School::getCountryDeptId, dept.getDeptId());
        }
        return this.baseMapper.selectCount(queryWrapper);
    }
}
