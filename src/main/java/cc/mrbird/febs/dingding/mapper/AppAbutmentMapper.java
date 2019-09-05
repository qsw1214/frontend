package cc.mrbird.febs.dingding.mapper;

import java.util.List;
import java.util.Map;

public interface AppAbutmentMapper {
    //通过app名称查找是否存在东西
    List selectAppAbutmentByAppName(Map params);
    //添加第三方应用接入的数据
    void insertAppAbutmentApply(Map paramsMap);
    //通过appName查找app的Inf
    Map selectAppInfByAppName(Map params);
}
