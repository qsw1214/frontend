package cc.mrbird.febs.resource.mapper;

import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 *  Mapper
 *
 * @author lb
 * @date 2019-08-17 19:45:05
 */
public interface SubjectMapper extends BaseMapper<Subject> {
	/**
	 * 增加浏览数
	 * @param subjectId
	 * @param num
	 */
	void increaseReadCount(@Param("subjectId") Long subjectId, @Param("num") Integer num);
	
	/**
	 * 增加资源数
	 * @param subjectId
	 * @param num
	 */
	void increaseResourceCount(@Param("subjectId") Long subjectId, @Param("num") Integer num);
	
	/**
     * 查找专题的资源
     *
     * @param page 分页对象
     * @param subjectId
     * @return Ipage
     */
    IPage<Resource> findSubjectResources(Page<?> page, @Param("subjectId") Long subjectId, @Param("resource") Resource resource);
}
