package cc.mrbird.febs.basicInfo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cc.mrbird.febs.basicInfo.entity.ClassroomInfo;
import cc.mrbird.febs.common.entity.QueryRequest;

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
     */
    void deleteClassroomInfo(String classroomIds);
    
    /**
     * 通过学校 id 删除
     *
     *
     */
    void deleteClassroomInfosByschoolId(List<String> schoolIds);

    List<ClassroomInfo> getClassroomInfoByCityCountry(String provinceId,String cityDeptId,String countryDeptId);

    Integer getClassroomCount(String provinceId,String cityDeptId,String countryDeptId);

    public List<ClassroomInfo> findClassroomByMainSchoolId(Integer mainSchoolId);
    
    /**
     * 按部门查询（分页）
     * @param request QueryRequest
     * @param
     * @return IPage<School>
     */
    IPage<ClassroomInfo> findClassroomInfosByDept(QueryRequest request, ClassroomInfo classroomInfo, String deptId);
}
