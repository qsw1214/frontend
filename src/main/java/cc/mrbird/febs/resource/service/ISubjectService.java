package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.resource.entity.Subject;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	void increaseReadCount(@Param("subjectId") Long subjectId, @Param("num") Integer num);
}
