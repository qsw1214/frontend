package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.Area;
import cc.mrbird.febs.basicInfo.mapper.AreaMapper;
import cc.mrbird.febs.basicInfo.service.IAreaService;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.Role;
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
 * @date 2019-08-16 08:37:08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Override
    public IPage<Area> findAreas(QueryRequest request, Area area) {
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(area.getCity()))
            queryWrapper.lambda().eq(Area::getCity, area.getCity());
        Page<Area> page = new Page<>(request.getPageNum(), request.getPageSize());
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
        //this.baseMapper.insert(area);
    }

    @Override
    @Transactional
    public void updateArea(Area area) {
        this.updateById(area);
    }

    @Override
    @Transactional
    public void deleteArea(String areaCodes) {
        List<String> list = Arrays.asList(areaCodes.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Area>().lambda().in(Area::getAreaCode, list));
    }
}
