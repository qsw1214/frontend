package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.service.ISchoolTimetableService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 *  Controller
 * @author psy
 * @date 2019-08-21 10:38:49
 */
@Slf4j
@Validated
@RestController
@RequestMapping("schoolTimetable")
public class SchoolTimetableController extends BaseController {

    @Autowired
    private ISchoolTimetableService schoolTimetableService;

    @GetMapping("list")
    @RequiresPermissions("schoolTimetable:view")
    public FebsResponse schoolTimetableList(SchoolTimetable schoolTimetable, QueryRequest request) {
        IPage test = this.schoolTimetableService.findSchoolTimetables(request,schoolTimetable);
        Map<String, Object> dataTable = getDataTable(this.schoolTimetableService.findSchoolTimetables(request,schoolTimetable));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("schoolTimetable")
    @RequiresPermissions("schoolTimetable:list")
    public FebsResponse getAllDeviceInfos(SchoolTimetable schoolTimetable) {
        return new FebsResponse().success().data(schoolTimetableService.findSchoolTimetables(schoolTimetable));
    }

    @Log("新增SchoolTimetable")
    @PostMapping
    @RequiresPermissions("schoolTimetable:add")
    public FebsResponse addSchoolTimetable(@Valid SchoolTimetable schoolTimetable) throws FebsException {
        try {
            this.schoolTimetableService.createSchoolTimetable(schoolTimetable);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增SchoolTimetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除SchoolTimetable")
    @GetMapping("delete/{courseIds}")
    @RequiresPermissions("schoolTimetable:delete")
    public FebsResponse deleteSchoolTimetable(@NotBlank(message = "{required}") @PathVariable String courseIds) throws FebsException {
        try {
            this.schoolTimetableService.deleteSchoolTimetable(courseIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除SchoolTimetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改SchoolTimetable")
    @PostMapping("update")
    @RequiresPermissions("schoolTimetable:update")
    public FebsResponse updateSchoolTimetable(SchoolTimetable schoolTimetable) throws FebsException {
        try {
            this.schoolTimetableService.updateSchoolTimetable(schoolTimetable);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改SchoolTimetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("schoolTimetable/excel")
    @RequiresPermissions("schoolTimetable:export")
    public void export(QueryRequest queryRequest, SchoolTimetable schoolTimetable, HttpServletResponse response) throws FebsException {
        try {
            List<SchoolTimetable> schoolTimetables = this.schoolTimetableService.findSchoolTimetables(queryRequest, schoolTimetable).getRecords();
            ExcelKit.$Export(SchoolTimetable.class, response).downXlsx(schoolTimetables, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
