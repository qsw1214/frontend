package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.CommentReplay;
import cc.mrbird.febs.resource.mapper.CommentReplayMapper;
import cc.mrbird.febs.resource.service.ICommentReplayService;
import cc.mrbird.febs.resource.service.ICommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

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
 * @date 2019-08-17 19:46:00
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommentReplayServiceImpl extends ServiceImpl<CommentReplayMapper, CommentReplay> implements ICommentReplayService {

	@Autowired
	private ICommentService commentService;
	
    @Override
    public IPage<CommentReplay> findCommentReplays(QueryRequest request, CommentReplay commentReplay) {
        LambdaQueryWrapper<CommentReplay> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(commentReplay.getUserName())) {
            queryWrapper.eq(CommentReplay::getUserName, commentReplay.getUserName());
        }
        if (commentReplay.getCommentId() != null) {
            queryWrapper.eq(CommentReplay::getCommentId, commentReplay.getCommentId());
        }
        Page<CommentReplay> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<CommentReplay> findCommentReplays(CommentReplay commentReplay) {
	    LambdaQueryWrapper<CommentReplay> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
	    if (StringUtils.isNotBlank(commentReplay.getUserName())) {
            queryWrapper.eq(CommentReplay::getUserName, commentReplay.getUserName());
        }
        if (commentReplay.getCommentId() != null) {
            queryWrapper.eq(CommentReplay::getCommentId, commentReplay.getCommentId());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createCommentReplay(CommentReplay commentReplay) {
    	commentReplay.setCreateTime(new Date());
        this.save(commentReplay);
        commentService.increaseReplayCount(commentReplay.getCommentId(), 1);
    }

    @Override
    @Transactional
    public void updateCommentReplay(CommentReplay commentReplay) {
        this.saveOrUpdate(commentReplay);
    }

    @Override
    @Transactional
    public void deleteCommentReplays(String commentReplayIds) {
    	List<String> list = Arrays.asList(commentReplayIds.split(StringPool.COMMA));
    	if(list.size()>0){
    		CommentReplay cr = this.baseMapper.selectById(list.get(0));
    		int n = this.baseMapper.delete(new QueryWrapper<CommentReplay>().lambda().in(CommentReplay::getCommentReplayId, list));
    		commentService.increaseReplayCount(cr.getCommentId(), -n);
    	}
	}

	@Override
	public void deleteCommentReplaysByCommentId(List<String> commentIds) {
		if(commentIds.size()>0){
			this.baseMapper.delete(new QueryWrapper<CommentReplay>().lambda().in(CommentReplay::getCommentId, commentIds));
		}
	}
	
}
