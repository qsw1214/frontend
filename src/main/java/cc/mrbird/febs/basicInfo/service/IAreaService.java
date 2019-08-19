package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Area;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-08-16 08:37:08
 */
public interface IAreaService extends IService<Area> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param area area
     * @return IPage<Area>
     */
    IPage<Area> findAreas(QueryRequest request, Area area);

    /**
     * 查询（所有）
     *
     * @param area area
     * @return List<Area>
     */
    List<Area> findAreas(Area area);

    /**
     * 新增
     *
     * @param area area
     */
    void createArea(Area area);

    /**
     * 修改
     *
     * @param area area
     */
    void updateArea(Area area);

    /**
     * 删除
     *
     * @param area area
     */
    void deleteArea(String areaCodes);
}
