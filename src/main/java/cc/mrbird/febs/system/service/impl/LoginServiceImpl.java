package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.mapper.LoginMapper;
import cc.mrbird.febs.system.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    /**
     * 查询是否存在这个用户
     * @param userInfMap
     * @return
     */
    @Override
    public User selectIsExistUser(Map userInfMap) {
        User user = loginMapper.selectIsExistUser(userInfMap);
        return user;
    }

    @Override
    public void insertUserInf(Map userInfMap) {
       loginMapper.insertUserInf(userInfMap);
    }

    @Override
    public void insertUserRole(Map userInfMap) {
        loginMapper.insertUserRole(userInfMap);
    }

    @Override
    public Map selectRoleId(Map roleMap) {
        Map requireMap = loginMapper.selectRoleId(roleMap);
        return requireMap;
    }
}
