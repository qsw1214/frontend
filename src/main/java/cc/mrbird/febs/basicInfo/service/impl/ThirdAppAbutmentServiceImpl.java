package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.Abutment;
import cc.mrbird.febs.basicInfo.mapper.ThirdAppAbutmentMapper;
import cc.mrbird.febs.basicInfo.service.ThirdAppAbutmentService;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ThirdAppAbutmentServiceImpl extends ServiceImpl<ThirdAppAbutmentMapper, Abutment> implements ThirdAppAbutmentService {

    /**
     * 查询分页
     * @param request QueryRequest
     * @param abutment school
     * @return
     */
    @Override
    public IPage<Abutment> selectAbutments(QueryRequest request, Abutment abutment) {
        LambdaQueryWrapper<Abutment> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (abutment.getId() != null) {
        	queryWrapper.eq(Abutment::getId, abutment.getId());
        }
        if (StringUtils.isNotBlank(abutment.getAppName())) {
       	queryWrapper.like(Abutment::getAppName, abutment.getAppName());
        }
        if (StringUtils.isNotBlank(abutment.getApplySchool())) {
            queryWrapper.like(Abutment::getApplySchool, abutment.getApplySchool());
        }

        Page<Abutment> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /**
     * 查询所有
     * @param abutment school
     * @return
     */
    @Override
    public List<Abutment> selectAbutments(Abutment abutment) {
        LambdaQueryWrapper<Abutment> queryWrapper = new LambdaQueryWrapper<>();

        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 修改第三方应用申请
     * @param abutment abutment
     */
    @Override
    @Transactional
    public void updateAbutment(Abutment abutment) {
        this.saveOrUpdate(abutment);
    }


}
