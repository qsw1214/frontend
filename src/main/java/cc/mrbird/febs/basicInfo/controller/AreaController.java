package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.service.IAreaService;
import cc.mrbird.febs.system.entity.User;
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
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
@Slf4j
@RestController
@RequestMapping("area")
public class AreaController extends BaseController {

    @Autowired
    private IAreaService areaService;

    @GetMapping("list")
    @RequiresPermissions("area:view")
    public FebsResponse areaList(Area area, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.areaService.findAreas(request,area));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("area")
    @RequiresPermissions("area:list")
    public FebsResponse getAllAreas(Area area) {
        return new FebsResponse().success().data(areaService.findAreas(area));
    }

    @Log("新增Area")
    @PostMapping
    @RequiresPermissions("area:add")
    public FebsResponse addArea(@Valid Area area) throws FebsException {
        try {
            area.setProvince("湖南省");
            this.areaService.createArea(area);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Area失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Area")
    @GetMapping("delete/{areaCodes}")
    @RequiresPermissions("area:delete")
    public FebsResponse deleteArea(@NotBlank(message = "{required}") @PathVariable String areaCodes) throws FebsException {
        try {
            this.areaService.deleteArea(areaCodes);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Area失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Area")
    @PostMapping("update")
    @RequiresPermissions("area:update")
    public FebsResponse updateArea(Area area) throws FebsException {
        try {
            this.areaService.updateArea(area);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Area失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("area/excel")
    @RequiresPermissions("area:export")
    public void export(QueryRequest queryRequest, Area area, HttpServletResponse response) throws FebsException {
        try {
            List<Area> areas = this.areaService.findAreas(queryRequest, area).getRecords();
            ExcelKit.$Export(Area.class, response).downXlsx(areas, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
