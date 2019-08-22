package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface LoginMapper extends BaseMapper<User> {

    User selectIsExistUser(Map userInfMap);

    void insertUserInf(Map userInfMap);

    void insertUserRole(Map userInfMap);

    Map selectRoleId(Map roleMap);
}
