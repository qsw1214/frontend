package cc.mrbird.febs.dingding.service;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface AppAbutmentService  {
    //通过app姓名查找数据库是否存在数据
    List selectAppAbutmentByAppName(Map paramsMap);
    //添加应用申请数据
    void insertAppAbutmentApply(Map paramsMap);
    //通过appName查找appKey和appSecret
    Map selectAppInfByAppName(Map params);
}
