package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.UserDept;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserDeptMapper extends BaseMapper<UserDept> {

    void insertUserDept(@Param("userId") String userId,@Param("deptId") String deptId);


    void deleteUserDept(String userid);
    
    /**
	 * 获取用户所属部门
	 * @param userId
	 */
	List<Dept> getDeptByUserId(String userId);
	
	/**
	 * 根据用户id和部门id获取部门
	 * @param userId
	 */
	Dept getDeptByUserIdAndDeptId(@Param("userId") String userId, @Param("deptId") String deptId);
}
