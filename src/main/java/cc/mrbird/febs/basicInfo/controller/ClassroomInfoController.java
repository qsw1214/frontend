package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.LiveRadioReqUtil;
import cc.mrbird.febs.common.utils.RadioStatus;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserDeptService;
import cc.mrbird.febs.basicInfo.entity.ClassroomInfo;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.basicInfo.service.IClassroomInfoService;
import cc.mrbird.febs.basicInfo.service.ISchoolService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 *  Controller
 *
 * @author psy
 * @date 2019-08-23 15:45:24
 */
@Slf4j
@Validated
@Controller
public class ClassroomInfoController extends BaseController {

    @Autowired
    private IClassroomInfoService classroomInfoService;
    @Autowired
    private IUserDeptService userDeptService;
    @Autowired
    private ISchoolService schoolService;

    @GetMapping("classroomInfo")
    @ResponseBody
    @RequiresPermissions("classroomInfo:view")
    public FebsResponse getAllClassroomInfos(ClassroomInfo classroomInfo) {
        return new FebsResponse().success().data(classroomInfoService.findClassroomInfos(classroomInfo));
    }

    @GetMapping("classroomInfo/list")
    @ResponseBody
    @RequiresPermissions("classroomInfo:view")
    public FebsResponse classroomInfoList(QueryRequest request, ClassroomInfo classroomInfo) {
        IPage<ClassroomInfo> classroomLists = this.classroomInfoService.findClassroomInfos(request, classroomInfo);
        List<ClassroomInfo> list = classroomLists.getRecords();
        for (int i = 0 ; i < list.size(); i++){
            ClassroomInfo info = list.get(i);
            String url = info.getUrl();
            List<RadioStatus> statusLists = LiveRadioReqUtil.getRadioStatus(url);
            if(statusLists.size() > 0){
                RadioStatus status = statusLists.get(0);
                info.setState(Integer.valueOf(status.getStatus()));
            }
        }
        Map<String, Object> dataTable = getDataTable(classroomLists);
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增ClassroomInfo")
    @PostMapping("classroomInfo")
    @ResponseBody
    @RequiresPermissions("classroomInfo:add")
    public FebsResponse addClassroomInfo(@Valid ClassroomInfo classroomInfo) throws FebsException {
        try {
            this.classroomInfoService.createClassroomInfo(classroomInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除ClassroomInfo")
    @GetMapping("classroomInfo/delete/{classroomIds}")
    @ResponseBody
    @RequiresPermissions("classroomInfo:delete")
    public FebsResponse deleteClassroomInfo(@NotBlank(message = "{required}") @PathVariable String classroomIds) throws FebsException {
        try {
        	this.classroomInfoService.deleteClassroomInfo(classroomIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改ClassroomInfo")
    @PostMapping("classroomInfo/update")
    @ResponseBody
    @RequiresPermissions("classroomInfo:update")
    public FebsResponse updateClassroomInfo(ClassroomInfo classroomInfo) throws FebsException {
        try {
        	ClassroomInfo old = classroomInfoService.getById(classroomInfo.getId());
			if(old == null)
				return new FebsResponse().fail().data("未找到");
			// 判断有无权限
			User user = getCurrentUser();			
			School school = schoolService.getById(old.getSchoolId());
			if(school != null && !userDeptService.isPermission(user.getUserId(), school.getDeptId())){
				return new FebsResponse().fail().data("无权限");
			}
            this.classroomInfoService.updateClassroomInfo(classroomInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改ClassroomInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("classroomInfo/excel")
    @ResponseBody
    @RequiresPermissions("classroomInfo:export")
    public void export(QueryRequest queryRequest, ClassroomInfo classroomInfo, HttpServletResponse response) throws FebsException {
        try {
            List<ClassroomInfo> classroomInfos = this.classroomInfoService.findClassroomInfos(queryRequest, classroomInfo).getRecords();
            ExcelKit.$Export(ClassroomInfo.class, response).downXlsx(classroomInfos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    private void resolveClassrModel(Integer classroomInfoId, Model model, Boolean transform) {
        ClassroomInfo classroomInfo = this.classroomInfoService.getById(classroomInfoId);
        model.addAttribute("classroomInfo", classroomInfo);

    }
    
    /**
     * 按部门查询教室
     * @param request
     * @param classroomInfo
     * @param deptId
     * @return
     */
    @GetMapping("classroomInfo/bydept/list")
	@ResponseBody
	@RequiresPermissions("classroomInfo:view")
	public FebsResponse getDeptClassroomInfoList(QueryRequest request, ClassroomInfo classroomInfo,
			@RequestParam(required = true) Long deptId) {
    	// 判断有无权限
		User user = getCurrentUser();		
		if(!userDeptService.isPermission(user.getUserId(), deptId)){
			return new FebsResponse().fail().data("无权限");
		}
		Map<String, Object> dataTable = getDataTable(
				this.classroomInfoService.findClassroomInfosByDept(request, classroomInfo, deptId));
		return new FebsResponse().success().data(dataTable);
	}
}
