package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.CommentReplay;
import cc.mrbird.febs.resource.service.ICommentReplayService;
import cc.mrbird.febs.system.entity.User;

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

import java.util.List;
import java.util.Map;

/**
 *  回复评论 Controller
 *
 * @author lb
 * @date 2019-08-17 19:46:00
 */
@Slf4j
@Validated
@RestController
public class CommentReplayController extends BaseController {

    @Autowired
    private ICommentReplayService commentReplayService;

    @GetMapping("commentReplay")
    @ResponseBody
    @RequiresPermissions("comment:view")
    public FebsResponse getAllCommentReplays(CommentReplay commentReplay) {
        return new FebsResponse().success().data(commentReplayService.findCommentReplays(commentReplay));
    }

    @GetMapping("commentReplay/list")
    @ResponseBody
    @RequiresPermissions("comment:view")
    public FebsResponse commentReplayList(QueryRequest request, CommentReplay commentReplay) {
        Map<String, Object> dataTable = getDataTable(this.commentReplayService.findCommentReplays(request, commentReplay));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增CommentReplay")
    @PostMapping("commentReplay")
    @ResponseBody
    @RequiresPermissions("comment:add")
    public FebsResponse addCommentReplay(@Valid CommentReplay commentReplay) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	commentReplay.setUserName(user.getUsername());
        	commentReplay.setUserAvatar(user.getAvatar());
            this.commentReplayService.createCommentReplay(commentReplay);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增CommentReplay失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除CommentReplay")
    @GetMapping("commentReplay/delete/{commentReplayIds}")
    @ResponseBody
    @RequiresPermissions("comment:delete")
    public FebsResponse deleteCommentReplay(@NotBlank(message = "{required}") @PathVariable String commentReplayIds) throws FebsException {
        try {
            this.commentReplayService.deleteCommentReplays(commentReplayIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除CommentReplay失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改CommentReplay")
    @PostMapping("commentReplay/update")
    @ResponseBody
    @RequiresPermissions("comment:update")
    public FebsResponse updateCommentReplay(CommentReplay commentReplay) throws FebsException {
        try {
            this.commentReplayService.updateCommentReplay(commentReplay);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改CommentReplay失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("commentReplay/excel")
    @ResponseBody
    @RequiresPermissions("comment:export")
    public void export(QueryRequest queryRequest, CommentReplay commentReplay, HttpServletResponse response) throws FebsException {
        try {
            List<CommentReplay> commentReplays = this.commentReplayService.findCommentReplays(queryRequest, commentReplay).getRecords();
            ExcelKit.$Export(CommentReplay.class, response).downXlsx(commentReplays, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
