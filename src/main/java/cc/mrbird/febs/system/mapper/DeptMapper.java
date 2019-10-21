package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
    void deleteDept(Long deptId);

    //根据父级部门id查询所在等级
    Long findGradeByParentId (Long parentId);

   /* List<Dept> getNameByDeptId( Long provinceDeptId, Long cityDeptId, Long countryDeptId);*/
}
