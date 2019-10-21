package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserDeptService;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.IClassInfoService;
import cc.mrbird.febs.basicInfo.service.ISchoolService;

import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 * Controller
 * 
 * @author psy
 * @date 2019-08-22 17:35:44
 */
@Slf4j
@Validated
@Controller
public class ClassInfoController extends BaseController {

	@Autowired
	private IClassInfoService classInfoService;
	@Autowired
    private IUserDeptService userDeptService;
	@Autowired
    private ISchoolService schoolService;

	@GetMapping("classInfo")
	@ResponseBody
	@RequiresPermissions("classInfo:view")
	public FebsResponse getAllClassInfos(ClassInfo classInfo) {
		return new FebsResponse().success().data(classInfoService.findClassInfos(classInfo));
	}

	@GetMapping("classInfo/list")
	@ResponseBody
	@RequiresPermissions("classInfo:view")
	public FebsResponse classInfoList(QueryRequest request, ClassInfo classInfo) {
		Map<String, Object> dataTable = getDataTable(this.classInfoService.findClassInfos(request, classInfo));
		return new FebsResponse().success().data(dataTable);
	}

	@Log("新增ClassInfo")
	@PostMapping("classInfo")
	@ResponseBody
	@RequiresPermissions("classInfo:add")
	public FebsResponse addClassInfo(@Valid ClassInfo classInfo) throws FebsException {
		System.out.println(classInfo);
		try {
			this.classInfoService.createClassInfo(classInfo);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "新增ClassInfo失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("删除ClassInfo")
	@GetMapping("classInfo/delete/{classIds}")
	@ResponseBody
	@RequiresPermissions("classInfo:delete")
	public FebsResponse deleteClassInfo(@NotBlank(message = "{required}") @PathVariable String classIds)
			throws FebsException {
		try {
			this.classInfoService.deleteClassInfo(classIds);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "删除ClassInfo失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("修改ClassInfo")
	@PostMapping("classInfo/update")
	@ResponseBody
	@RequiresPermissions("classInfo:update")
	public FebsResponse updateClassInfo(ClassInfo classInfo) throws FebsException {
		try {
			ClassInfo old = classInfoService.getById(classInfo.getClassId());
			if(old == null)
				return new FebsResponse().fail().data("未找到");
			// 判断有无权限
			User user = getCurrentUser();			
			School school = schoolService.getById(old.getSchoolId());
			if(school != null && !userDeptService.isPermission(user.getUserId(), school.getDeptId())){
				return new FebsResponse().fail().data("无权限");
			}
			this.classInfoService.updateClassInfo(classInfo);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "修改ClassInfo失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PostMapping("classInfo/excel")
	@ResponseBody
	@RequiresPermissions("classInfo:export")
	public void export(QueryRequest queryRequest, ClassInfo classInfo, HttpServletResponse response)
			throws FebsException {
		try {
			List<ClassInfo> classInfos = this.classInfoService.findClassInfos(queryRequest, classInfo).getRecords();
			ExcelKit.$Export(ClassInfo.class, response).downXlsx(classInfos, false);
		} catch (Exception e) {
			String message = "导出Excel失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	/**
	 * 按部门查询班级
	 */
	@GetMapping("classInfo/bydept/list")
	@ResponseBody
	@RequiresPermissions("classInfo:view")
	public FebsResponse getDeptClassInfoList(QueryRequest request, ClassInfo classInfo,
			@RequestParam(required = true) Long deptId) {
		// 判断有无权限
		User user = getCurrentUser();		
		if(!userDeptService.isPermission(user.getUserId(), deptId)){
			return new FebsResponse().fail().data("无权限");
		}
		Map<String, Object> dataTable = getDataTable(
				this.classInfoService.findClassInfosByDept(request, classInfo, deptId));
		return new FebsResponse().success().data(dataTable);
	}
}
