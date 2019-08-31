package cc.mrbird.febs.dingding.mapper;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface DingRemindMapper extends BaseMapper<User> {
     List selectCourseInfList(Map params);
}
