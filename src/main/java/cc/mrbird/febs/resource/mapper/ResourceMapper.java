package cc.mrbird.febs.resource.mapper;

import cc.mrbird.febs.resource.entity.Resource;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 *  Mapper
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
public interface ResourceMapper extends BaseMapper<Resource> {
	/**
	 * 增加评论数
	 * @param resourceId
	 * @param num
	 */
	void increaseCommentCount(@Param("resourceId") Long resourceId, @Param("num") Integer num);
	
	/**
	 * 增加浏览数
	 * @param resourceId
	 * @param num
	 */
	void increaseReadCount(@Param("resourceId") Long resourceId, @Param("num") Integer num);
}
