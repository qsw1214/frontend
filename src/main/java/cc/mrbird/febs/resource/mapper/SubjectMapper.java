package cc.mrbird.febs.resource.mapper;

import cc.mrbird.febs.resource.entity.Subject;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
