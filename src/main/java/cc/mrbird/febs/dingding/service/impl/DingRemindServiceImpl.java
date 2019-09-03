package cc.mrbird.febs.dingding.service.impl;

import cc.mrbird.febs.dingding.mapper.DingRemindMapper;
import cc.mrbird.febs.dingding.service.DingRemindService;
import cc.mrbird.febs.dingding.util.DingRemindUtil;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.mapper.LoginMapper;
import cc.mrbird.febs.system.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DingRemindServiceImpl extends ServiceImpl<DingRemindMapper, User> implements DingRemindService {

    @Autowired
    private DingRemindMapper dingRemindMapper;

    @Override
    public void selectCourseInfList(Map params) {
        //获取某学校一个学期的所有课程，并创建日程
        List<Map<String, Object>> courseInfList = dingRemindMapper.selectCourseInfList(params);
        //user_id,begin_date,duration
        if (courseInfList.size() > 0) {
            for (Map map : courseInfList) {
                DingRemindUtil.doDingRemind(map);
            }
        }

    }
}
