package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.dingding.util.AddressListUtil;
import cc.mrbird.febs.dingding.vo.Department;
import cc.mrbird.febs.dingding.vo.DepartmentListIFVO;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.mapper.DeptMapper;
import cc.mrbird.febs.system.service.IDeptService;
import cc.mrbird.febs.system.service.IUserDeptService;
import net.sf.json.JSONArray;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author lb
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Autowired
    private DeptMapper deptMapper;
    
    @Autowired
	private IUserDeptService userDeptService;
    
    private int maxDeptGrade = 4;

    @Override
    public List<DeptTree<Dept>> findDepts() {
    	LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
    	queryWrapper.lt(Dept::getDeptGrade, maxDeptGrade);
        List<Dept> depts = this.baseMapper.selectList(queryWrapper);
        List<DeptTree<Dept>> trees = this.convertDepts(depts);
        return TreeUtil.buildDeptTree(trees, "1");
    }

    @Override
    public List<DeptTree<Dept>> getLimitDeptTree(Long userId) {
    	// 获取所有父部门
    	String arrays = AddressListUtil.getUserParentDepts(userId);
    	if(arrays == null)
    		return new ArrayList<DeptTree<Dept>>();
		List<Long> parentIds = new ArrayList<>();
		JSONArray jsonArray = JSONArray.fromObject(arrays);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray array = jsonArray.getJSONArray(i);
			for (int j = 0; j < array.size(); j++){
				if(!parentIds.contains(array.getLong(j)))
					parentIds.add(array.getLong(j));
			}
		}
		LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getDeptId, parentIds);
        queryWrapper.lt(Dept::getDeptGrade, maxDeptGrade);
        queryWrapper.orderByAsc(Dept::getOrderNum);
        List<Dept> parentDepts = this.baseMapper.selectList(queryWrapper);
		
		// 获取用户所属部门
		List<Dept> userDepts = userDeptService.getDeptByUserId(userId);
		List<Long> deptIds1 = new ArrayList<>(); // 一级部门id
		List<Long> deptIds2 = new ArrayList<>(); // 二级部门id
		long deptGrade;
		for(int i=0; i<userDepts.size(); i++){
			deptGrade = userDepts.get(i).getDeptGrade();
			if(deptGrade == 1)
				deptIds1.add(userDepts.get(i).getDeptId());
			else if(deptGrade == 2)
				deptIds2.add(userDepts.get(i).getDeptId());
		}
		
		List<Dept> depts2 = new ArrayList<>();
		List<Dept> depts3 = new ArrayList<>();		
		// 获取二级部门
		if(!deptIds1.isEmpty()){
			queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.in(Dept::getParentId, deptIds1);
			queryWrapper.orderByAsc(Dept::getOrderNum);
			depts2 = this.baseMapper.selectList(queryWrapper);
			for(int i=0; i<depts2.size(); i++)
				if(!deptIds2.contains(depts2.get(i).getDeptId()))
					deptIds2.add(depts2.get(i).getDeptId());
		}	
		// 获取三级部门
		if(!deptIds2.isEmpty()){
			queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.in(Dept::getParentId, deptIds2);
			queryWrapper.orderByAsc(Dept::getOrderNum);
			depts3 = this.baseMapper.selectList(queryWrapper);
		}
		
		// 合并所有部门
		List<Dept> depts = new ArrayList<>();
		depts.addAll(depts2);
		depts.addAll(depts3);
		for(int i=0; i<parentDepts.size(); i++){
			if(!depts.contains(parentDepts.get(i)))
				depts.add(parentDepts.get(i));
		}
		
        List<DeptTree<Dept>> trees =  this.convertDepts(depts);
        return TreeUtil.buildDeptTree(trees, "1");
    }

    @Override
    public List<Dept> findDepts(Dept dept, QueryRequest request) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName()))
            queryWrapper.lambda().eq(Dept::getDeptName, dept.getDeptName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDept(Dept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null)
            dept.setParentId(0L);      
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional
    public void updateDept(Dept dept) {
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional
    public void deleteDepts(String[] deptIds) {
       this.delete(Arrays.asList(deptIds));
    }

    private List<DeptTree<Dept>> convertDepts(List<Dept> depts){
        List<DeptTree<Dept>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            DeptTree<Dept> tree = new DeptTree<>();
            tree.setId(String.valueOf(dept.getDeptId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getDeptName());
            tree.setData(dept);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getParentId, deptIds);
        List<Dept> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }

    public long findGradeByParentId(long parentId){

        return this.deptMapper.findGradeByParentId(parentId);
    }

    /**
     * 同步钉钉部门数据
     */
    public void synchDingDeptData(){
        DepartmentListIFVO departmentListIFVO = AddressListUtil.synchDingDeptData();
        List<Department> departments = departmentListIFVO.getDepartment();
        for (int i = 0; i < departments.size(); i++){
            Department department = departments.get(i);
            Dept dept = new Dept();
            dept.setDeptId(department.getId());
            dept.setDeptName(department.getName());
            dept.setParentId(department.getParentid());
            dept.setOrderNum(department.getId());
            dept.setModifyTime(DateUtil.getNowDateTime());
            dept.setDeptGrade(0l);
            this.saveOrUpdate(dept);
        }
        // 设置部门级别值
        for (int i = 0 ; i < departments.size(); i++) {
            Department department = departments.get(i);
            long deptId = department.getId();
            long deptGrade = 0;
            long parentId = department.getParentid();
            if(parentId != 0){
                long parentDeptGrade = checkSynchParentDeptInfo(parentId);
                Dept dept = new Dept();
                dept.setDeptId(department.getId());
                dept.setDeptGrade(parentDeptGrade + 1l);
                this.saveOrUpdate(dept);
            }
        }
    }

    public long checkSynchParentDeptInfo(long deptId){
        Dept dept = this.getById(deptId);
        long parentId = dept.getParentId();
        if(parentId == 0){ //网络联校部门
            return 0l;
        }else{
            long parentDeptGrade = checkSynchParentDeptInfo(parentId);
            return parentDeptGrade + 1;
        }
    }
}
