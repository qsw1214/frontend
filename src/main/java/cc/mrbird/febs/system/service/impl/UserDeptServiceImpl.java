package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.entity.UserDept;
import cc.mrbird.febs.system.mapper.UserDeptMapper;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IUserDeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户部门关联表 Service实现
 *
 * @author lb
 * @date 2019-09-08 16:53:13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDeptServiceImpl extends ServiceImpl<UserDeptMapper, UserDept> implements IUserDeptService {
	@Autowired
	private IDeptService deptService;

    @Override
    public IPage<UserDept> findUserDepts(QueryRequest request, UserDept userDept) {
        LambdaQueryWrapper<UserDept> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<UserDept> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<UserDept> findUserDepts(UserDept userDept) {
	    LambdaQueryWrapper<UserDept> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createUserDept(UserDept userDept) {
        this.save(userDept);
    }

    @Override
    @Transactional
    public void updateUserDept(UserDept userDept) {
        this.saveOrUpdate(userDept);
    }

    @Override
    @Transactional
    public void deleteUserDept(UserDept userDept) {
        LambdaQueryWrapper<UserDept> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}

	@Override
	public List<Dept> getDeptByUserId(String userId) {
		return this.baseMapper.getDeptByUserId(userId);
	}
	
	@Override
	public Dept getDeptByUserIdAndDeptId(Long userId, Long deptId) {
		return this.baseMapper.getDeptByUserIdAndDeptId(userId, deptId);
	}

	@Override
	public boolean isPermission(String userId, Long deptId) {
		List<Long> parentDeptIds = deptService.getParentDeptIds(deptId);
		if(parentDeptIds.isEmpty())
			return false;
		List<Dept> depts = getDeptByUserId(userId);
		for(Dept dept: depts){
			if(parentDeptIds.contains(dept.getDeptId())){
				return true;
			}
		}	
		return false;
	}

}
