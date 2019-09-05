package cc.mrbird.febs.dingding.service.impl;

import cc.mrbird.febs.dingding.mapper.AppAbutmentMapper;
import cc.mrbird.febs.dingding.mapper.DingRemindMapper;
import cc.mrbird.febs.dingding.service.AppAbutmentService;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AppAbutmentServiceImpl implements AppAbutmentService {
    @Autowired
    private AppAbutmentMapper appAbutmentMapper;

    /**
     * 通过app姓名查找数据库是否存在数据
     * @param paramsMap
     * @return
     */
    @Override
    public List selectAppAbutmentByAppName(Map paramsMap) {
        return appAbutmentMapper.selectAppAbutmentByAppName(paramsMap);
    }

    /**
     * 添加应用申请数据
     * @param paramsMap
     */
    @Override
    public void insertAppAbutmentApply(Map paramsMap) {
        appAbutmentMapper.insertAppAbutmentApply(paramsMap);
    }

    /**
     * 通过appName查找app的Inf
     * @param params
     * @return
     */
    @Override
    public Map selectAppInfByAppName(Map params) {
        return appAbutmentMapper.selectAppInfByAppName(params);
    }
}
