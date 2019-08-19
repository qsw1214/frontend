package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.mapper.DeviceInfoMapper;
import cc.mrbird.febs.basicInfo.service.IDeviceInfoService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 *  Service实现
 *
 * @author psy
 * @date 2019-08-16 16:50:43
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {

    @Override
    public IPage<DeviceInfo> findDeviceInfos(QueryRequest request, DeviceInfo deviceInfo) {
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        if (deviceInfo.getState() != null)
            queryWrapper.lambda().eq(DeviceInfo::getState, deviceInfo.getState());
        if (StringUtils.isNotBlank(deviceInfo.getDeviceName()))
            queryWrapper.lambda().eq(DeviceInfo::getDeviceName, deviceInfo.getDeviceName());
        Page<DeviceInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<DeviceInfo> findDeviceInfos(DeviceInfo deviceInfo) {
	    LambdaQueryWrapper<DeviceInfo> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public DeviceInfo findDeviceById(Integer deviceId){
        return this.getById(deviceId);
    }

    @Override
    @Transactional
    public void createDeviceInfo(DeviceInfo deviceInfo) {

        this.save(deviceInfo);
    }

    @Override
    @Transactional
    public void updateDeviceInfo(DeviceInfo deviceInfo) {

        this.updateById(deviceInfo);
    }

    @Override
    @Transactional
    public void deleteDeviceInfo(String deviceIds) {
        List<String> list = Arrays.asList(deviceIds.split(StringPool.COMMA));
        this.baseMapper.delete(
                new QueryWrapper<DeviceInfo>().lambda().in(DeviceInfo::getDeviceId, list));
	}


}
