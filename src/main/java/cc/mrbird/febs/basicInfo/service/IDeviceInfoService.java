package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-08-16 16:50:43
 */
public interface IDeviceInfoService extends IService<DeviceInfo> {

    DeviceInfo findDeviceById(Integer deviceId);
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param deviceInfo deviceInfo
     * @return IPage<DeviceInfo>
     */
    IPage<DeviceInfo> findDeviceInfos(QueryRequest request, DeviceInfo deviceInfo);

    /**
     * 查询（所有）
     *
     * @param deviceInfo deviceInfo
     * @return List<DeviceInfo>
     */
    List<DeviceInfo> findDeviceInfos(DeviceInfo deviceInfo);

    /**
     * 新增
     *
     * @param deviceInfo deviceInfo
     */
    void createDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 修改
     *
     * @param deviceInfo deviceInfo
     */
    void updateDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 删除
     *
     * @param deviceIds deviceIds
     */
    void deleteDeviceInfo(String deviceIds);
    
    /**
     * 通过学校 id 删除
     *
     * @param List<String> 学校id
     */
    void deleteDeviceInfoByschoolId(List<String> schoolIds);
}
