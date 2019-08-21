package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface LoginService extends IService<User> {
    //查询是否存在User
    User selectIsExistUser(Map userInfMap);

    void insertUserInf(Map userInfMap);

    void insertUserRole(Map userInfMap);

    Map selectRoleId(Map roleMap);
}
