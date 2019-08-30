package cc.mrbird.febs.dingding.service;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DingRemindService extends IService<User> {
    //查询一个学校学期的所有课程
    void selectCourseInfList(Map params);
}
