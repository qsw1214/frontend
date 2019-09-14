package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-08-17 19:45:05
 */
public interface ISubjectService extends IService<Subject> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param subject subject
     * @return IPage<Subject>
     */
    IPage<Subject> findSubjects(QueryRequest request, Subject subject);

    /**
     * 查询（所有）
     *
     * @param subject subject
     * @return List<Subject>
     */
    List<Subject> findSubjects(Subject subject);

    /**
     * 新增
     *
     * @param subject subject
     */
    void createSubject(Subject subject);

    /**
     * 修改
     *
     * @param subject subject
     */
    void updateSubject(Subject subject);

    /**
     * 删除
     *
     * @param subjectId subjectIds
     */
    void deleteSubjects(String subjectIds);
    
    /**
	 * 增加浏览数
	 * @param subjectId
	 * @param num
	 */
	void increaseReadCount(Long subjectId, Integer num);
	
	/**
	 * 增加资源数
	 * @param subjectId
	 * @param num
	 */
	void increaseResourceCount(Long subjectId, Integer num);
	
	/**
     * 查找专题的资源
     *
     * @param page 分页对象
     * @param resource 资源对象，用于传递查询条件
     * @return Ipage
     */
    IPage<Resource> findSubjectResources(Long subjectId, Resource resource, QueryRequest request);
}
