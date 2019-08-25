package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.service.IResourceService;
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
 *  Controller
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
@Slf4j
@Validated
@RestController
public class ResourceController extends BaseController {

    @Autowired
    private IResourceService resourceService;

    @GetMapping("resource")
    @ResponseBody
    @RequiresPermissions("resource:view")
    public FebsResponse getAllResources(Resource resource) {
        return new FebsResponse().success().data(resourceService.findResources(resource));
    }

    @GetMapping("resource/list")
    @ResponseBody
    @RequiresPermissions("resource:view")
    public FebsResponse resourceList(QueryRequest request, Resource resource) {
        Map<String, Object> dataTable = getDataTable(this.resourceService.findDetails(resource, request));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Resource")
    @PostMapping("resource")
    @ResponseBody
    @RequiresPermissions("resource:add")
    public FebsResponse addResource(@Valid Resource resource) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	resource.setCreator(user.getUsername());
        	resource.setAvatar(user.getAvatar());
        	resource.setSchoolId(user.getSchoolId());
            this.resourceService.createResource(resource);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Resource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Resource")
    @GetMapping("resource/delete/{resourceIds}")
    @ResponseBody
    @RequiresPermissions("resource:delete")
    public FebsResponse deleteResource(@NotBlank(message = "{required}") @PathVariable String resourceIds) throws FebsException {
        try {
            this.resourceService.deleteResources(resourceIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Resource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Resource")
    @PostMapping("resource/update")
    @ResponseBody
    @RequiresPermissions("resource:update")
    public FebsResponse updateResource(Resource resource) throws FebsException {
        try {
            this.resourceService.updateResource(resource);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Resource失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("resource/excel")
    @ResponseBody
    @RequiresPermissions("resource:export")
    public void export(QueryRequest queryRequest, Resource resource, HttpServletResponse response) throws FebsException {
        try {
            List<Resource> resources = this.resourceService.findResources(queryRequest, resource).getRecords();
            ExcelKit.$Export(Resource.class, response).downXlsx(resources, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
