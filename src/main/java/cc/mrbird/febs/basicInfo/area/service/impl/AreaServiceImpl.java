package cc.mrbird.febs.basicInfo.area.service.impl;

import cc.mrbird.febs.basicInfo.area.entity.Area;
import cc.mrbird.febs.basicInfo.area.mapper.AreaMapper;
import cc.mrbird.febs.basicInfo.area.service.IAreaService;
import cc.mrbird.febs.common.entity.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  Service实现
 *
 * @author psy
 * @date 2019-08-15 21:34:16
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public IPage<Area> findAreas(QueryRequest request, Area area) {
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Area> page = new Page<Area>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Area> findAreas(Area area) {
	    LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createArea(Area area) {
        this.save(area);
    }

    @Override
    @Transactional
    public void updateArea(Area area) {
        this.saveOrUpdate(area);
    }

    @Override
    @Transactional
    public void deleteArea(Area area) {
        LambdaQueryWrapper<Area> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}
}
