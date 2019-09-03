package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.OnlineClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author psy
 * @date 2019-08-30 08:37:08
 */
public interface OnlineClassMapper extends BaseMapper<OnlineClass> {

    IPage<OnlineClass> findOnlineClassInfos(Page page, @Param("onlineClass") OnlineClass onlineClass);

}
