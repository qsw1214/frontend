package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.mapper.SchoolMapper;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.system.controller.DeptController;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IUserDeptService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
@Slf4j
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
    
    @Autowired
	private IUserDeptService userDeptService;

    @Autowired
    private SchoolMapper schoolMapper;
 
    @Override
    public IPage<School> findSchools(QueryRequest request, School school) {
    	LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();     
        // TODO 设置查询条件   
        if (StringUtils.isNotBlank(school.getSchoolName())) {
            //queryWrapper.eq(School::getSchoolName, school.getSchoolName());
            queryWrapper.like(School::getSchoolName, school.getSchoolName());
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
        if (school.getBelongId() != null) {
            queryWrapper.eq(School::getBelongId, school.getBelongId());
        }
        if(school.getProvinceDeptId()!=null){
            queryWrapper.eq(School::getProvinceDeptId, school.getProvinceDeptId());
        }
        if(school.getCityDeptId()!=null){
            queryWrapper.eq(School::getCityDeptId, school.getCityDeptId());
        }
        if(school.getCountryDeptId()!=null){
            queryWrapper.eq(School::getCountryDeptId, school.getCountryDeptId());
        }
        Page<School> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<School> findSchools(School school) {
	    LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(school.getSchoolName())) {
            //queryWrapper.eq(School::getSchoolName, school.getSchoolName());
            queryWrapper.like(School::getSchoolName, school.getSchoolName());
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
        if (school.getBelongId() != null) {
            queryWrapper.eq(School::getBelongId, school.getBelongId());
        }
        if(school.getProvinceDeptId()!=null){
            queryWrapper.eq(School::getProvinceDeptId, school.getProvinceDeptId());
        }
        if(school.getCityDeptId()!=null){
            queryWrapper.eq(School::getCityDeptId, school.getCityDeptId());
        }
        if(school.getCountryDeptId()!=null){
            queryWrapper.eq(School::getCountryDeptId, school.getCountryDeptId());
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
    public School createSchool(School school) {
    	school.setCreateTime(new Date());
        this.save(school);
        return school;
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

	@Override
	public IPage<School> findSchoolsByDept(QueryRequest request, School school, Long deptId) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
			
    	// 获取用户所属部门
		Dept dept = userDeptService.getDeptByUserIdAndDeptId(user.getUserId(), deptId);
		if(dept == null){
			return new Page<School>();
		}
		
    	LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
    	if (StringUtils.isNotBlank(school.getSchoolName())) {
            queryWrapper.eq(School::getSchoolName, school.getSchoolName());
        }
    	
    	if(dept.getDeptGrade() == 1){
    		queryWrapper.eq(School::getProvinceDeptId, deptId);
    	}else if(dept.getDeptGrade() == 2){
    		queryWrapper.eq(School::getCityDeptId, deptId);
    	}else if(dept.getDeptGrade() == 3){
    		queryWrapper.eq(School::getCountryDeptId, deptId);
    	}
    	
    	Page<School> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
	}

	@Override
	public Integer getSchoolCount(Integer provinceId,Integer cityDeptId,Integer countryDeptId){
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        if (provinceId != null) {
            queryWrapper.eq(School::getProvinceDeptId, provinceId);
        }
        if (cityDeptId != null) {
            queryWrapper.eq(School::getCityDeptId, cityDeptId);
        }
        if (countryDeptId != null) {
            queryWrapper.eq(School::getCountryDeptId, countryDeptId);
        }
        return this.baseMapper.selectCount(queryWrapper);
    }

    public Map<String,Object> getLast12MonthSchoolCount(Integer provinceId, Integer cityDeptId, Integer countryDeptId){
        School school = new School();
        school.setProvinceDeptId(provinceId == null ? null:provinceId.longValue());
        school.setCityDeptId(cityDeptId == null ? null:cityDeptId.longValue());
        school.setCountryDeptId(countryDeptId == null ? null : countryDeptId.longValue());
        List<HashMap<String,Object>> result = this.schoolMapper.getLast12MonthSchoolCount(school);
        Map<String,Object> mapResult = new HashMap<String,Object>();
        for (int i = 0 ; i < result.size(); i++){
            HashMap<String,Object> map = result.get(i);
            System.out.print(map.get("yearMonth") + " ");
            System.out.print(map.get("totalNum") + " ");
            mapResult.put(map.get("yearMonth") + "",map.get("totalNum"));
        }
        return mapResult;
    }
}
