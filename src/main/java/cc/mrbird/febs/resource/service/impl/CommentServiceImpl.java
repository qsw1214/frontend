package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Comment;
import cc.mrbird.febs.resource.mapper.CommentMapper;
import cc.mrbird.febs.resource.service.ICommentReplayService;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.resource.service.IResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 *  Service实现
 *
 * @author lb
 * @date 2019-08-17 19:45:42
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private ICommentReplayService commentReplayService;
	@Autowired
	private IResourceService resourceService;
	
    @Override
    public IPage<Comment> findComments(QueryRequest request, Comment comment) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(comment.getUserName())) {
            queryWrapper.eq(Comment::getUserName, comment.getUserName());
        }
        if (comment.getResourceId() != null) {
            queryWrapper.eq(Comment::getResourceId, comment.getResourceId());
        }
        Page<Comment> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Comment> findComments(Comment comment) {
	    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createComment(Comment comment) {
    	comment.setCreateTime(new Date());
        this.save(comment);
        resourceService.increaseCommentCount(comment.getResourceId(), 1);
    }

    @Override
    @Transactional
    public void updateComment(Comment comment) {
        this.saveOrUpdate(comment);
    }

    @Override
    @Transactional
    public void deleteComments(String commentIds) {
    	List<String> list = Arrays.asList(commentIds.split(StringPool.COMMA));
    	if(list.size()>0){
	        this.baseMapper.delete(new QueryWrapper<Comment>().lambda().in(Comment::getCommentId, list)); // 删除评论
	        commentReplayService.deleteCommentReplaysByCommentId(list); // 删除回复
    	}
	}

	@Override
	public void increaseReplayCount(Long commentId, Integer num) {
		commentMapper.increaseReplayCount(commentId, num);
	}

	@Override
	public void deleteCommentsByResourceId(List<String> resourceIds) {
		List<Comment> list = this.baseMapper.selectList(new QueryWrapper<Comment>().lambda().in(Comment::getResourceId, resourceIds));		
		// List<Long> commentIds = list.stream().map(Comment::getCommentId).collect(Collectors.toList());
		List<String> commentIds = new ArrayList<>();
		for(int i=0, len=list.size(); i<len; i++)
			commentIds.add(list.get(i).getCommentId().toString());
		if(list.size()>0){
			this.baseMapper.delete(new QueryWrapper<Comment>().lambda().in(Comment::getCommentId, list)); // 删除评论
			commentReplayService.deleteCommentReplaysByCommentId(commentIds); // 删除回复
		}
	}
}
