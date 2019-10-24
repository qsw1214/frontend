package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.Timetable;
import cc.mrbird.febs.basicInfo.service.ITimetableService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
 *
 * @author wsq
 * @date 2019-10-23 15:34:24
 */
@Configuration
@Slf4j
@RestController
@Controller
public class TimetableController extends BaseController {

    @Autowired
    private ITimetableService timetableService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "timetable")
    private String timetableIndex(){
        return FebsUtil.view("timetable/timetable");
    }

    @GetMapping("timetable")
    public FebsResponse getAllTimetables(Timetable timetable) {
        return new FebsResponse().success().data(timetableService.findTimetables(timetable));
    }

    @GetMapping("timetable/list")
    public FebsResponse timetableList(QueryRequest request, Timetable timetable) {
        Map<String, Object> dataTable = getDataTable(this.timetableService.findTimetables(request, timetable));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Timetable")
    @PostMapping("timetable")
    public FebsResponse addTimetable(@Valid Timetable timetable) throws FebsException {
        try {
            this.timetableService.createTimetable(timetable);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Timetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Timetable")
    @GetMapping("timetable/delete/{tid}")
    public FebsResponse deleteTimetable(@NotBlank(message = "{required}") @PathVariable String tid) throws FebsException {
        try {
            this.timetableService.deleteTimetable(tid);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Timetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Timetable")
    @PostMapping("timetable/update")
    public FebsResponse updateTimetable(Timetable timetable) throws FebsException {
        try {
            this.timetableService.updateTimetable(timetable);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Timetable失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("timetable/excel")
    public void export(QueryRequest queryRequest, Timetable timetable, HttpServletResponse response) throws FebsException {
        try {
            List<Timetable> timetables = this.timetableService.findTimetables(queryRequest, timetable).getRecords();
            ExcelKit.$Export(Timetable.class, response).downXlsx(timetables, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
