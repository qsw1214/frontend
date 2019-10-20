package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.UserDept;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户部门关联表 Service接口
 *
 * @author lb
 * @date 2019-09-08 16:53:13
 */
public interface IUserDeptService extends IService<UserDept> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param userDept userDept
     * @return IPage<UserDept>
     */
    IPage<UserDept> findUserDepts(QueryRequest request, UserDept userDept);

    /**
     * 查询（所有）
     *
     * @param userDept userDept
     * @return List<UserDept>
     */
    List<UserDept> findUserDepts(UserDept userDept);

    /**
     * 新增
     *
     * @param userDept userDept
     */
    void createUserDept(UserDept userDept);

    /**
     * 修改
     *
     * @param userDept userDept
     */
    void updateUserDept(UserDept userDept);

    /**
     * 删除
     *
     * @param userDept userDept
     */
    void deleteUserDept(UserDept userDept);
    
    /**
	 * 获取用户所属部门
	 * @param userId
	 * 
	 */
    List<Dept> getDeptByUserId(Long userId);
    
    /**
	 * 根据userId和deptId获取部门
	 * @param userId
	 * @param deptId
	 */
    Dept getDeptByUserIdAndDeptId(Long userId, Long deptId);
    
    /**
     * 判断用户对部门有无权限
     * @param deptId
     * @return
     */
    boolean isPermission(Long userId, Long deptId);
    
}
