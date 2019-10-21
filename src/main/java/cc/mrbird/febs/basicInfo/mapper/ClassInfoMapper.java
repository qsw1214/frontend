package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Mapper
 *
 * @author psy
 * @date 2019-08-22 17:35:44
 */
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {
	/**
	 * 按部门查询
	 * @param page
	 * @param classInfo
	 * @param deptId
	 * @return
	 */
	IPage<ClassInfo> findClassInfosByDept(Page<?> page, @Param("classInfo") ClassInfo classInfo, 
			@Param("deptId") Long deptId);
}
