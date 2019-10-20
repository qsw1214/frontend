package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-08-22 17:35:44
 */
public interface IClassInfoService extends IService<ClassInfo> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param classInfo classInfo
     * @return IPage<ClassInfo>
     */
    IPage<ClassInfo> findClassInfos(QueryRequest request, ClassInfo classInfo);

    /**
     * 查询（所有）
     *
     * @param classInfo classInfo
     * @return List<ClassInfo>
     */
    List<ClassInfo> findClassInfos(ClassInfo classInfo);

    /**
     * 新增
     *
     * @param classInfo classInfo
     */
    void createClassInfo(ClassInfo classInfo);

    /**
     * 修改
     *
     * @param classInfo classInfo
     */
    void updateClassInfo(ClassInfo classInfo);

    /**
     * 删除
     *
     * @param classInfo classInfo
     */
    void deleteClassInfo(String classIds);

    /**
     * 通过学校 id 删除
     *
     * @param List<String> 学校id
     */
    void deleteClassInfosByschoolId(List<String> schoolIds);
    
    /**
     * 按部门查询（分页）
     * @param request QueryRequest
     * @param school school
     * @return IPage<School>
     */
    IPage<ClassInfo> findClassInfosByDept(QueryRequest request, ClassInfo classInfo, Long deptId);
    
}
