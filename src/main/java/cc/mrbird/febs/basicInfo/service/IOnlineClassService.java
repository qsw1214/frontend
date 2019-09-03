package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.entity.OnlineClass;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 * @author psy
 * @date 2019-08-30 08:37:08
 */
public interface IOnlineClassService extends IService<OnlineClass> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param onlineClass onlineClass
     * @return IPage<Area>
     */
    IPage<OnlineClass> findOnlineClass(QueryRequest request, OnlineClass onlineClass);
}
