package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.UserDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserDeptMapper extends BaseMapper<UserDept> {

    void insertUserDept(String userid,String deptId);


    void deleteUserDept(String userid);
}
