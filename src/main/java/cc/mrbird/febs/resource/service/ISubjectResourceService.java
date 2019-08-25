package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.SubjectResource;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-08-17 19:45:30
 */
public interface ISubjectResourceService extends IService<SubjectResource> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param subjectResource subjectResource
     * @return IPage<SubjectResource>
     */
    IPage<SubjectResource> findSubjectResources(QueryRequest request, SubjectResource subjectResource);

    /**
     * 查询（所有）
     *
     * @param subjectResource subjectResource
     * @return List<SubjectResource>
     */
    List<SubjectResource> findSubjectResources(SubjectResource subjectResource);

    /**
     * 新增
     *
     * @param subjectResource subjectResource
     */
    void createSubjectResource(SubjectResource subjectResource);
    
    void addSubjectResources(Long subjectId, String resourceIds);

    /**
     * 修改
     *
     * @param subjectResource subjectResource
     */
    void updateSubjectResource(SubjectResource subjectResource);

    /**
     * 删除
     *
     * @param String subjectResourceIds
     */
    void deleteSubjectResources(String subjectResourceIds);
    
    /**
     * 通过专题 id 删除
     *
     * @param List<String> 专题 id
     */
    void deleteSubjectResourcesBySubjectId(List<String> subjectIds);
    
    /**
     * 通过资源 id 删除
     *
     * @param List<String> 资源 id
     */
    void deleteSubjectResourcesByResourceId(List<String> resourceIds);
    
}
