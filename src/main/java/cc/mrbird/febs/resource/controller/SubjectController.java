package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Subject;
import cc.mrbird.febs.resource.service.ISubjectService;
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
 *  专题 Controller
 *
 * @author lb
 * @date 2019-08-17 19:45:05
 */
@Slf4j
@Validated
@RestController
public class SubjectController extends BaseController {

    @Autowired
    private ISubjectService subjectService;

    @GetMapping("subject")
    @ResponseBody
    @RequiresPermissions("subject:view")
    public FebsResponse getAllSubjects(Subject subject) {
        return new FebsResponse().success().data(subjectService.findSubjects(subject));
    }

    @GetMapping("subject/list")
    @ResponseBody
    @RequiresPermissions("subject:view")
    public FebsResponse subjectList(QueryRequest request, Subject subject) {
        Map<String, Object> dataTable = getDataTable(this.subjectService.findSubjects(request, subject));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Subject")
    @PostMapping("subject")
    @ResponseBody
    @RequiresPermissions("subject:add")
    public FebsResponse addSubject(@Valid Subject subject) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	subject.setCreator(user.getUsername());
        	subject.setAvatar(user.getAvatar());
            this.subjectService.createSubject(subject);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Subject失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Subject")
    @GetMapping("subject/delete/{subjectIds}")
    @ResponseBody
    @RequiresPermissions("subject:delete")
    public FebsResponse deleteSubject(@NotBlank(message = "{required}") @PathVariable String subjectIds) throws FebsException {
        try {
            this.subjectService.deleteSubjects(subjectIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Subject失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Subject")
    @PostMapping("subject/update")
    @ResponseBody
    @RequiresPermissions("subject:update")
    public FebsResponse updateSubject(Subject subject) throws FebsException {
        try {
            this.subjectService.updateSubject(subject);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Subject失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("subject/excel")
    @ResponseBody
    @RequiresPermissions("subject:export")
    public void export(QueryRequest queryRequest, Subject subject, HttpServletResponse response) throws FebsException {
        try {
            List<Subject> subjects = this.subjectService.findSubjects(queryRequest, subject).getRecords();
            ExcelKit.$Export(Subject.class, response).downXlsx(subjects, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
