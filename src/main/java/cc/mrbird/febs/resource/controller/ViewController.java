package cc.mrbird.febs.resource.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.resource.entity.Comment;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.entity.Subject;
import cc.mrbird.febs.resource.service.ICommentService;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.resource.service.ISubjectService;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.service.IDictService;
import cc.mrbird.febs.system.service.IUserDeptService;

/**
 *  Controller
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
@Controller("resourceView")
public class ViewController extends BaseController{
	
	@Autowired
    private IResourceService resourceService;
	@Autowired
    private ICommentService commentService;
	@Autowired
    private ISubjectService subjectService;
	@Autowired
	private IUserDeptService userDeptService;
	@Autowired
	private IDictService dictService;
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/{resourceId}/comment")
    @RequiresPermissions("comment:view")
    public String comment(@NotBlank(message = "resourceId不能为空") @PathVariable Long resourceId, Model model){
    	Resource resource = resourceService.findDetailById(resourceId);
    	model.addAttribute("resource", resource);
        return FebsUtil.view("resource/comment/comment");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/{resourceId}/comment/add")
    @RequiresPermissions("comment:view")
    public String commentAdd(@NotBlank(message = "resourceId不能为空") @PathVariable Long resourceId, Model model){
    	model.addAttribute("resourceId", resourceId);
        return FebsUtil.view("resource/comment/commentAdd");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/comment/{commentId}/commentReplay")
    @RequiresPermissions("comment:view")
    public String commentReplayIndex(@NotBlank(message = "commentId不能为空") @PathVariable Long commentId, Model model){
    	Comment comment = commentService.getById(commentId);
    	model.addAttribute("comment", comment);
        return FebsUtil.view("resource/commentReplay/commentReplay");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/comment/{commentId}/commentReplay/add")
    @RequiresPermissions("comment:view")
    public String commentReplayAdd(@NotBlank(message = "commentId不能为空") @PathVariable Long commentId, Model model){
    	model.addAttribute("commentId", commentId);
        return FebsUtil.view("resource/commentReplay/commentReplayAdd");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/category")
    @RequiresPermissions("category:view")
    public String category(){
        return FebsUtil.view("resource/category/category");
    }  
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/resource")
    @RequiresPermissions("resource:view")
    public String resourceIndex(Model model){
        return FebsUtil.view("resource/resource/resource");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/resource/add")
    @RequiresPermissions("resource:add")
    public String resourceAdd(Model model) {
    	String userId = super.getCurrentUser().getUserId();
    	List<Dept> depts = userDeptService.getDeptByUserId(userId);
    	List<Dict> grades = dictService.findDictsByField("grade");
    	List<Dict> subjects = dictService.findDictsByField("subject");
    	List<Dict> fileTypes = dictService.findDictsByField("file_type");
    	model.addAttribute("depts", depts);
    	model.addAttribute("grades", grades);
    	model.addAttribute("subjects", subjects);
    	model.addAttribute("fileTypes", fileTypes);
        return FebsUtil.view("resource/resource/resourceAdd");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/resource/update/{resourceId}")
    @RequiresPermissions("resource:update")
    public String resourceUpdate(@PathVariable String resourceId, Model model) {
    	Resource resource = resourceService.getById(resourceId);
    	String userId = super.getCurrentUser().getUserId();
    	List<Dept> depts = userDeptService.getDeptByUserId(userId);
    	List<Dict> grades = dictService.findDictsByField("grade");
    	List<Dict> subjects = dictService.findDictsByField("subject");
    	List<Dict> fileTypes = dictService.findDictsByField("file_type");
    	model.addAttribute("depts", depts);
    	model.addAttribute("grades", grades);
    	model.addAttribute("subjects", subjects);
    	model.addAttribute("fileTypes", fileTypes);
        model.addAttribute("resource", resource);    
        return FebsUtil.view("resource/resource/resourceUpdate");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/subject")
    @RequiresPermissions("subject:view")
    public String subjectIndex(){
        return FebsUtil.view("resource/subject/subject");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/subject/add")
    @RequiresPermissions("subject:add")
    public String subjectAdd() {
        return FebsUtil.view("resource/subject/subjectAdd");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/subject/update/{subjectId}")
    @RequiresPermissions("subject:update")
    public String subjectUpdate(@PathVariable String subjectId, Model model) {
    	Subject subject = subjectService.getById(subjectId);
        model.addAttribute("subject", subject);
        return FebsUtil.view("resource/subject/subjectUpdate");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/subject/{subjectId}/addResource")
    @RequiresPermissions("subject:update")
    public String subjectRelate(@PathVariable String subjectId, Model model) {
        model.addAttribute("subjectId", subjectId);
        return FebsUtil.view("resource/subjectResource/addResource");
    }
    
    @GetMapping(FebsConstant.VIEW_PREFIX + "resource/subject/{subjectId}/resources")
    @RequiresPermissions("subject:update")
    public String subjectResources(@PathVariable String subjectId, Model model) {
        model.addAttribute("subject", this.subjectService.getById(subjectId));
        return FebsUtil.view("resource/subjectResource/subjectResource");
    }
}
