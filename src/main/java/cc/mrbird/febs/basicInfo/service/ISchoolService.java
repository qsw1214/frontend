package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.School;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校表 Service接口
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
public interface ISchoolService extends IService<School> {
	
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param school school
     * @return IPage<School>
     */
    IPage<School> findSchools(QueryRequest request, School school);

    IPage<School> findSchoolByMap(QueryRequest request,School school, Map<String, Object> params);

    /**
     * 查询（所有）
     *
     * @param school school
     * @return List<School>
     */
    List<School> findSchools(School school);

    /**
     * 根据学校名称全文匹配查找数据
     * @param schoolName
     * @return
     */
    List<School> findSchoolsByName(String schoolName);

    /**
     * 新增
     *
     * @param school school
     */
    School createSchool(School school);

    /**
     * 修改
     *
     * @param school school
     */
    void updateSchool(School school);

    /**
     * 删除
     * @param schoolIds
     */
    void deleteSchool(String schoolIds);

    Integer getCountOfCity(String city);

    Integer getCountOfCountry(String country);
    
    /**
     * 按部门查询（分页）
     *
     * @param request QueryRequest
     * @param school school
     * @return IPage<School>
     */
    IPage<School> findSchoolsByDept(QueryRequest request, School school, String deptId);

    Integer getSchoolCount(String provinceId, String cityDeptId, String countryDeptId);

    Map<String,Object>  getLast12MonthSchoolCount(String provinceId, String cityDeptId, String countryDeptId);

}
