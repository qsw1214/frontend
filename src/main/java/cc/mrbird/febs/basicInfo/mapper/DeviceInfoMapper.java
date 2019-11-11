package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author psy
 * @date 2019-08-16 16:50:43
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
     * 查找设备详细信息
     *
     * @param page 分页对象
     * @param deviceInfo 设备对象，用于传递查询条件
     * @return Ipage
     */
    IPage<DeviceInfo> findDeviceDetailInfos(Page page, @Param("deviceInfo") DeviceInfo deviceInfo);

    public Integer countDeviceByDept(String deptName);

    public Integer countDeviceBySchool(String schoolName);
    
    /**
	 * 按部门查询
	 * @param page
	 * @param deviceInfo
	 * @param deptId
	 * @return
	 */
	IPage<DeviceInfo> findDeviceInfosByDept(Page<?> page, @Param("deviceInfo") DeviceInfo deviceInfo, 
			@Param("deptId") String deptId);
}
