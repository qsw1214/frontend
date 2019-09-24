package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<DeptTree<Dept>> findDepts();

    /**
     * 获取部门列表（树形列表）
     */
    List<DeptTree<Dept>> getLimitDeptTree(Long userId);

    /**
     * 获取部门树（供Excel导出）
     *
     * @param dept    部门对象（传递查询参数）
     * @param request QueryRequest
     * @return List<Dept>
     */
    List<Dept> findDepts(Dept dept, QueryRequest request);

    /**
     * 新增部门
     *
     * @param dept 部门对象
     */
    void createDept(Dept dept);

    /**
     * 修改部门
     *
     * @param dept 部门对象
     */
    void updateDept(Dept dept);

    /**
     * 删除部门
     *
     * @param deptIds 部门 ID集合
     */
    void deleteDepts(String[] deptIds);

    long findGradeByParentId(long deptId);

    /**
     * 同步钉钉部门数据至本地
     */
    void synchDingDeptData();
    
    /**
     * 获取用户所有父部门
     * @param userId
     * @return
     */
    List<List<Dept>> getAllParentDept(Long userId);
}
