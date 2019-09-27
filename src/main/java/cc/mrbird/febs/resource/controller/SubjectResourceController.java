package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.SubjectResource;
import cc.mrbird.febs.resource.service.ISubjectResourceService;
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
 *  Controller
 *
 * @author lb
 * @date 2019-08-17 19:45:30
 */
@Slf4j
@Validated
@RestController
public class SubjectResourceController extends BaseController {

    @Autowired
    private ISubjectResourceService subjectResourceService;

    @GetMapping("subjectResource")
    @ResponseBody
    @RequiresPermissions("subjectResource:view")
    public FebsResponse getAllSubjectResources(SubjectResource subjectResource) {
        return new FebsResponse().success().data(subjectResourceService.findSubjectResources(subjectResource));
    }

    @GetMapping("subjectResource/list")
    @ResponseBody
    //@RequiresPermissions("subjectResource:view")
    public FebsResponse subjectResourceList(QueryRequest request, SubjectResource subjectResource) {
        Map<String, Object> dataTable = getDataTable(this.subjectResourceService.findSubjectResources(request, subjectResource));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增SubjectResource")
    @PostMapping("subjectResource")
    @ResponseBody
    @RequiresPermissions("subjectResource:add")
    public FebsResponse addSubjectResource(@Valid SubjectResource subjectResource) throws FebsException {
        try {
            this.subjectResourceService.createSubjectResource(subjectResource);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增SubjectResource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    @Log("新增SubjectResource")
    @PostMapping("subjectResources")
    @RequiresPermissions("subjectResource:add")
    public FebsResponse addSubjectResources(Long subjectId, String resourceIds) throws FebsException {
        try {
        	this.subjectResourceService.addSubjectResources(subjectId, resourceIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增SubjectResource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除SubjectResource")
    @GetMapping("subjectResource/delete/{subjectResourceIds}")
    @ResponseBody
    @RequiresPermissions("subjectResource:delete")
    public FebsResponse deleteSubjectResources(@NotBlank(message = "{required}") @PathVariable String subjectResourceIds) throws FebsException {
        try {
            this.subjectResourceService.deleteSubjectResources(subjectResourceIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除SubjectResource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改SubjectResource")
    @PostMapping("subjectResource/update")
    @ResponseBody
    @RequiresPermissions("subjectResource:update")
    public FebsResponse updateSubjectResource(SubjectResource subjectResource) throws FebsException {
        try {
            this.subjectResourceService.updateSubjectResource(subjectResource);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改SubjectResource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("subjectResource/excel")
    @ResponseBody
    @RequiresPermissions("subjectResource:export")
    public void export(QueryRequest queryRequest, SubjectResource subjectResource, HttpServletResponse response) throws FebsException {
        try {
            List<SubjectResource> subjectResources = this.subjectResourceService.findSubjectResources(queryRequest, subjectResource).getRecords();
            ExcelKit.$Export(SubjectResource.class, response).downXlsx(subjectResources, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
