package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.UserDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserDeptMapper extends BaseMapper<UserDept> {

    void insertUserDept(Long userid,Long deptId);


    void deleteUserDept(Long userid);
}
