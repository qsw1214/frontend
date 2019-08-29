package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author MrBird
 */
public interface DeptMapper extends BaseMapper<Dept> {
    //新增部门
    void insertDept(Map params);

    //更新部门
    void updateDept(Map map);

    //删除部门
    void deleteDept(String deptId);
}
