package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.ClassroomInfo;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-08-23 15:45:24
 */
public interface IClassroomInfoService extends IService<ClassroomInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param classroomInfo classroomInfo
     * @return IPage<ClassroomInfo>
     */
    IPage<ClassroomInfo> findClassroomInfos(QueryRequest request, ClassroomInfo classroomInfo);

    /**
     * 查询（所有）
     *
     * @param classroomInfo classroomInfo
     * @return List<ClassroomInfo>
     */
    List<ClassroomInfo> findClassroomInfos(ClassroomInfo classroomInfo);

    /**
     * 新增
     *
     * @param classroomInfo classroomInfo
     */
    void createClassroomInfo(ClassroomInfo classroomInfo);

    /**
     * 修改
     *
     * @param classroomInfo classroomInfo
     */
    void updateClassroomInfo(ClassroomInfo classroomInfo);

    /**
     * 删除
     *
     * @param String classroomInfo
     */
    void deleteClassroomInfo(String classroomIds);
    
    /**
     * 通过学校 id 删除
     *
     * @param List<String> 学校id
     */
    void deleteClassroomInfosByschoolId(List<String> schoolIds);

    List<ClassroomInfo> getClassroomInfoByCityCountry(Integer provinceId,Integer cityDeptId,Integer countryDeptId);

    Integer getClassroomCount(Integer provinceId,Integer cityDeptId,Integer countryDeptId);
}
