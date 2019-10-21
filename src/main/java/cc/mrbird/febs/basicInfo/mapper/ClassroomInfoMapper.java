package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.ClassInfo;
import cc.mrbird.febs.basicInfo.entity.ClassroomInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Mapper
 *
 * @author psy
 * @date 2019-08-23 15:45:24
 */
public interface ClassroomInfoMapper extends BaseMapper<ClassroomInfo> {

	public List<ClassroomInfo> getClassroomInfoByCityCountry(@Param("map") Map<String, Integer> map);

	public Integer getClassroomCount(@Param("map") Map<String, Integer> map);

	public List<ClassroomInfo> findClassroomByMainSchoolId(@Param("schoolId") Integer schoolId);

	/**
	 * 按部门查询 
	 * @param page
	 * @param classroomInfo
	 * @param deptId
	 * @return
	 */
	IPage<ClassroomInfo> findClassroomInfosByDept(Page<?> page, @Param("classroomInfo") ClassroomInfo classroomInfo,
			@Param("deptId") Long deptId);

}
