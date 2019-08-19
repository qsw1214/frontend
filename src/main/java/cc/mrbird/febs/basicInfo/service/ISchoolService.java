package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.School;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 查询（所有）
     *
     * @param school school
     * @return List<School>
     */
    List<School> findSchools(School school);

    /**
     * 新增
     *
     * @param school school
     */
    void createSchool(School school);

    /**
     * 修改
     *
     * @param school school
     */
    void updateSchool(School school);

    /**
     * 删除
     *
     * @param school school
     */
    void deleteSchool(String[] schoolIds); 
}
