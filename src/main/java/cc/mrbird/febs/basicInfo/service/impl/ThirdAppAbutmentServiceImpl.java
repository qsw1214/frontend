package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.Abutment;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.mapper.SchoolMapper;
import cc.mrbird.febs.basicInfo.mapper.ThirdAppAbutmentMapper;
import cc.mrbird.febs.basicInfo.service.ISchoolService;
import cc.mrbird.febs.basicInfo.service.ThirdAppAbutmentService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ThirdAppAbutmentServiceImpl extends ServiceImpl<ThirdAppAbutmentMapper, Abutment> implements ThirdAppAbutmentService {

    /**
     * 查询分页
     * @param request QueryRequest
     * @param abutment school
     * @return
     */
    @Override
    public IPage<Abutment> selectAbutments(QueryRequest request, Abutment abutment) {
        LambdaQueryWrapper<Abutment> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (abutment.getId() != null) {
        	queryWrapper.eq(Abutment::getId, abutment.getId());
        }
        if (StringUtils.isNotBlank(abutment.getAppName())) {
       	queryWrapper.eq(Abutment::getAppName, abutment.getAppName());
        }
        if (StringUtils.isNotBlank(abutment.getApplySchool())) {
            queryWrapper.eq(Abutment::getApplySchool, abutment.getApplySchool());
        }
        /*if (StringUtils.isNotBlank(abutment.getSchoolType())) {
            queryWrapper.eq(Abutment::getSchoolType, abutment.getSchoolType());
        }
        if (StringUtils.isNotBlank(abutment.getProvince())) {
            queryWrapper.eq(Abutment::getProvince, abutment.getProvince());
        }
        if (StringUtils.isNotBlank(abutment.getCity())) {
            queryWrapper.eq(Abutment::getCity, abutment.getCity());
        }
        if (StringUtils.isNotBlank(abutment.getCountry())) {
            queryWrapper.eq(Abutment::getCountry, abutment.getCountry());
        }
        if (abutment.getState() != null) {
            queryWrapper.eq(Abutment::getState, abutment.getState());
        }*/
        Page<Abutment> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /**
     * 查询所有
     * @param abutment school
     * @return
     */
    @Override
    public List<Abutment> selectAbutments(Abutment abutment) {
        LambdaQueryWrapper<Abutment> queryWrapper = new LambdaQueryWrapper<>();
        /*if (StringUtils.isNotBlank(school.getSchoolName())) {
            queryWrapper.eq(School::getSchoolName, school.getSchoolName());
        }
        if (StringUtils.isNotBlank(school.getSchoolType())) {
            queryWrapper.eq(School::getSchoolType, school.getSchoolType());
        }
        if (StringUtils.isNotBlank(school.getProvince())) {
            queryWrapper.eq(School::getProvince, school.getProvince());
        }
        if (StringUtils.isNotBlank(school.getCity())) {
            queryWrapper.eq(School::getCity, school.getCity());
        }
        if (StringUtils.isNotBlank(school.getCountry())) {
            queryWrapper.eq(School::getCountry, school.getCountry());
        }
        if (school.getState() != null) {
            queryWrapper.eq(School::getState, school.getState());
        }
        if (school.getSchoolId() != null) {
            queryWrapper.eq(School::getSchoolId, school.getSchoolId());
        }*/
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 修改第三方应用申请
     * @param abutment abutment
     */
    @Override
    @Transactional
    public void updateAbutment(Abutment abutment) {
        this.saveOrUpdate(abutment);
    }


}
