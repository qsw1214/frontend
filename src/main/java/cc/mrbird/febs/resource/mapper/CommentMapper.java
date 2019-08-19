package cc.mrbird.febs.resource.mapper;

import cc.mrbird.febs.resource.entity.Comment;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 *  Mapper
 *
 * @author lb
 * @date 2019-08-17 19:45:42
 */
public interface CommentMapper extends BaseMapper<Comment> {
	/**
	 * 增加回复数
	 * @param commentId
	 * @param num
	 */
	void increaseReplayCount(@Param("commentId") Long commentId, @Param("num") Integer num);
}
