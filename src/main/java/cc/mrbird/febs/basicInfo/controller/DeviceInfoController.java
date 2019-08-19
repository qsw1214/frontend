package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("deviceInfo")
public class DeviceInfoController extends BaseController {

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @GetMapping("list")
    @RequiresPermissions("deviceInfo:view")
    public FebsResponse deviceInfoList(DeviceInfo deviceInfo, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.deviceInfoService.findDeviceInfos(request,deviceInfo));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("deviceInfo")
    @RequiresPermissions("deviceInfo:list")
    public FebsResponse getAllDeviceInfos(DeviceInfo deviceInfo) {
        return new FebsResponse().success().data(deviceInfoService.findDeviceInfos(deviceInfo));
    }

    @Log("新增DeviceInfo")
    @PostMapping
    @RequiresPermissions("deviceInfo:add")
    public FebsResponse addDeviceInfo(@Valid DeviceInfo deviceInfo) throws FebsException {
        try {
            deviceInfo.setState(0);
            this.deviceInfoService.createDeviceInfo(deviceInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增DeviceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除DeviceInfo")
    @GetMapping("delete/{deviceIds}")
    @RequiresPermissions("deviceInfo:delete")
    public FebsResponse deleteDeviceInfo(@NotBlank(message = "{required}") @PathVariable String deviceIds) throws FebsException {
        try {
            this.deviceInfoService.deleteDeviceInfo(deviceIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除DeviceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改DeviceInfo")
    @PostMapping("update")
    @RequiresPermissions("deviceInfo:update")
    public FebsResponse updateDeviceInfo(DeviceInfo deviceInfo) throws FebsException {
        try {
            this.deviceInfoService.updateDeviceInfo(deviceInfo);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改DeviceInfo失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("deviceInfo/excel")
    @RequiresPermissions("deviceInfo:export")
    public void export(QueryRequest queryRequest, DeviceInfo deviceInfo, HttpServletResponse response) throws FebsException {
        try {
            List<DeviceInfo> deviceInfos = this.deviceInfoService.findDeviceInfos(queryRequest, deviceInfo).getRecords();
            ExcelKit.$Export(DeviceInfo.class, response).downXlsx(deviceInfos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
