package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Resource;
import cc.mrbird.febs.resource.service.IResourceService;
import cc.mrbird.febs.system.entity.Dict;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IDictService;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    private IDictService dictService;

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
	public FebsResponse deleteResource(@NotBlank(message = "{required}") @PathVariable String resourceIds)
			throws FebsException {
		try {
			User user = super.getCurrentUser();
			List<String> list = Arrays.asList(resourceIds.split(StringPool.COMMA));
			if (!this.resourceService.checkCreator(list, user.getUsername()))
				return new FebsResponse().fail().data("无权限");
			this.resourceService.deleteResources(list);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "删除Resource失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("审核Resource")
	@PostMapping("resource/audit")
	@ResponseBody
	@RequiresPermissions("resource:audit")
	public FebsResponse audit(@RequestParam(value = "resourceIds[]") String[] resourceIds,
			@RequestParam("status") Integer status) throws FebsException {
		try {
			if (resourceIds == null || resourceIds.length == 0)
				return new FebsResponse().fail().data("resourceIds为空");
			if (status == null || status < 0 || status > 2)
				return new FebsResponse().fail().data("status参数非法");
			List<String> list = Arrays.asList(resourceIds);
			int count = this.resourceService.updateStatus(list, status);
			return new FebsResponse().success().data(count);
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

    @GetMapping("resource/excel")
    @ResponseBody
    @RequiresPermissions("resource:export")
    public void export(QueryRequest queryRequest, Resource resource, HttpServletResponse response) throws FebsException {
        try {
        	queryRequest.setPageSize(1);
            List<Resource> resources = this.resourceService.findResources(queryRequest, resource).getRecords();
            ExcelKit.$Export(Resource.class, response).downXlsx(resources, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    @PostMapping("resource/excel")
    @RequiresPermissions("resource:import")
    public FebsResponse addResource(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
        	User user = super.getCurrentUser();
        	String username = user.getUsername();
        	String avatar = user.getAvatar();
        	Date now = new Date();
     	
    	    long beginMillis = System.currentTimeMillis();
    	    List<Resource> successList = Lists.newArrayList();
    	    List<Map<String, Object>> errorList = Lists.newArrayList();
    	    ExcelKit.$Import(Resource.class).readXlsx(file.getInputStream(), new ExcelReadHandler<Resource>() {
    	    	@Override
    	        public void onSuccess(int sheetIndex, int rowIndex, Resource entity) {
    	    		entity.setCreator(username);
    	    		entity.setAvatar(avatar);
    	    		entity.setCreateTime(now);
    	        	successList.add(entity); // 单行读取成功，加入入库队列。
    	        }

    	        @Override
    	        public void onError(int sheetIndex, int rowIndex, List<ExcelErrorField> errorFields) {
    	        	// 读取数据失败，记录了当前行所有失败的数据
    	        	Map<String, Object> map = new HashMap<>();
    	        	map.put("sheetIndex", sheetIndex);
    	        	map.put("rowIndex", rowIndex);
    	        	map.put("errorFields", errorFields);
    	        	errorList.add(map);
    	        }
    	        });          
            log.info("解析耗时：", (System.currentTimeMillis() - beginMillis) / 1000L);
        	// TODO: 执行successList的入库操作。
        	if(errorList.isEmpty()){
        		this.resourceService.createResources(successList);
        		return new FebsResponse().success().data(successList);
        	}else{
        		return new FebsResponse().fail().data(errorList);
        	}      	
        } catch (Exception e) {
        	System.out.println(e);
        	log.error("解析失败:", e);
            throw new FebsException("解析失败");
        }
    }
    
    @GetMapping("resource/countByMonth")
    @ResponseBody
    @RequiresPermissions("resource:view")
    public FebsResponse getResourceCountEveryMonth(Integer provinceId, Integer cityDeptId, Integer countryDeptId){
    	 Integer deptId = 0;
    	 if(countryDeptId != null){
			 deptId = countryDeptId;
		 }else if(cityDeptId != null){
    	 	deptId = cityDeptId;
		 }else if(provinceId != null){
    	 	deptId = provinceId;
		 }
    	 return new FebsResponse().success().data(resourceService.getResourceCountEveryMonth(deptId));
    }
    
}
