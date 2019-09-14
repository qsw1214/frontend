package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.UserDept;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserDeptMapper extends BaseMapper<UserDept> {

    void insertUserDept(Long userid,Long deptId);


    void deleteUserDept(Long userid);
    
    /**
	 * 获取用户所属部门
	 * @param userId
	 */
	List<Dept> getUserDepts(long userId);
}
