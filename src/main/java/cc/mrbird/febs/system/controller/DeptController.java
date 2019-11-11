package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IUserDeptService;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class DeptController extends BaseController {

	@Autowired
	private IDeptService deptService;
	@Autowired
	private IUserDeptService userDeptService;
	
	@GetMapping("parents")
	public List<List<Dept>> getParents(@RequestParam String userId) throws FebsException {
		try {
			return this.deptService.getAllParentDept(userId);
		} catch (Exception e) {
			String message = "获取父部门失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}
	
	@GetMapping("userDepts")
	public List<Dept> getUserDepts(@RequestParam String userId) throws FebsException {
		try {
			return this.userDeptService.getDeptByUserId(userId);
		} catch (Exception e) {
			String message = "获取用户所属部门失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	/*@GetMapping("select/tree")
	public List<DeptTree<Dept>> getDeptTree() throws FebsException {
		try {
			return this.deptService.findDepts();
		} catch (Exception e) {
			String message = "获取部门树失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}*/

	@GetMapping("select/tree")
	public FebsResponse getDeptTree() throws FebsException {
		try {
			return new FebsResponse().success().data(deptService.selectDepts());
		} catch (Exception e) {
			String message = "获取部门树失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@GetMapping("tree")
	public FebsResponse getLimitDeptTree() throws FebsException {
		try {		
			String userId = super.getCurrentUser().getUserId();
			List<DeptTree<Dept>> depts = this.deptService.getLimitDeptTree(userId);
			return new FebsResponse().success().data(depts);
		} catch (Exception e) {
			String message = "获取部门树失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PostMapping("synchDingDeptData")
	public FebsResponse synchDingDeptData() throws FebsException {
		try {
			this.deptService.synchDingDeptData();
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "同步钉钉部门数据失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("新增部门")
	@PostMapping
	@RequiresPermissions("dept:add")
	public FebsResponse addDept(@Valid Dept dept) throws FebsException {
		try {
			this.deptService.createDept(dept);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "新增部门失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("删除部门")
	@GetMapping("delete/{deptIds}")
	@RequiresPermissions("dept:delete")
	public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds)
			throws FebsException {
		try {
			String[] ids = deptIds.split(StringPool.COMMA);
			this.deptService.deleteDepts(ids);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "删除部门失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("修改部门")
	@PostMapping("update")
	@RequiresPermissions("dept:update")
	public FebsResponse updateDept(@Valid Dept dept) throws FebsException {
		try {
			this.deptService.updateDept(dept);
			return new FebsResponse().success();
		} catch (Exception e) {
			String message = "修改部门失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@GetMapping("excel")
	@RequiresPermissions("dept:export")
	public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws FebsException {
		try {
			List<Dept> depts = this.deptService.findDepts(dept, request);
			ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
		} catch (Exception e) {
			String message = "导出Excel失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	/*@GetMapping("get")
	public FebsResponse get(Long deptId) throws FebsException {
		try {
			return new FebsResponse().success().data(deptService.getNameByDeptId(deptId));
		}catch(Exception e){
			String message = "所属地区获取失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}*/
	@GetMapping("get")
	public FebsResponse get( String deptId) throws FebsException {
		try {
			return new FebsResponse().success().data(deptService.getNameByDeptId(deptId));
		}catch(Exception e){
			String message = "所属地区获取失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}
}
