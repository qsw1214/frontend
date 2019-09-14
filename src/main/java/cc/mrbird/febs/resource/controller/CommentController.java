package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Comment;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.system.entity.User;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *  资源评论 Controller
 *
 * @author lb
 * @date 2019-08-17 19:45:42
 */
@Slf4j
@Validated
@RestController
public class CommentController extends BaseController {

    @Autowired
    private ICommentService commentService;
    
    private int maxPageSize = 1000;

    @GetMapping("comment")
    @ResponseBody
    @RequiresPermissions("comment:view")
    public FebsResponse getAllComments(Comment comment) {
        return new FebsResponse().success().data(commentService.findComments(comment));
    }

    @GetMapping("comment/list")
    @ResponseBody
    @RequiresPermissions("comment:view")
    public FebsResponse commentList(QueryRequest request, Comment comment) {
    	if(request.getPageSize() > maxPageSize)
    		return new FebsResponse().fail().data("pageSize不能超过"+maxPageSize);
        Map<String, Object> dataTable = getDataTable(this.commentService.findComments(request, comment));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Comment")
    @PostMapping("comment")
    @ResponseBody
    @RequiresPermissions("comment:add")
    public FebsResponse addComment(@Valid Comment comment) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	comment.setUserName(user.getUsername());
        	comment.setUserAvatar(user.getAvatar());
            this.commentService.createComment(comment);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Comment")
    @GetMapping("comment/delete/{commentIds}")
    @ResponseBody
    @RequiresPermissions("comment:delete")
    public FebsResponse deleteComment(@NotBlank(message = "{required}") @PathVariable String commentIds) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	List<String> list = Arrays.asList(commentIds.split(StringPool.COMMA));
        	if(!this.commentService.checkCreator(list, user.getUsername()))
        		return new FebsResponse().fail().data("无权限");
            this.commentService.deleteComments(list);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Comment")
    @PostMapping("comment/update")
    @ResponseBody
    @RequiresPermissions("comment:update")
    public FebsResponse updateComment(Comment comment) throws FebsException {
        try {
            this.commentService.updateComment(comment);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Comment失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("comment/excel")
    @ResponseBody
    @RequiresPermissions("comment:export")
    public void export(QueryRequest queryRequest, Comment comment, HttpServletResponse response) throws FebsException {
        try {
            List<Comment> comments = this.commentService.findComments(queryRequest, comment).getRecords();
            ExcelKit.$Export(Comment.class, response).downXlsx(comments, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
