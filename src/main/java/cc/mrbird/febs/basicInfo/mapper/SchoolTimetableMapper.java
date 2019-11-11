package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Mapper
 *
 * @author psy
 * @date 2019-08-21 10:38:49
 */
public interface SchoolTimetableMapper extends BaseMapper<SchoolTimetable> {

	IPage<SchoolTimetable> findSchoolTimetables(Page page, @Param("schoolTimetable") SchoolTimetable schoolTimetable);

	Integer selectMainSchoolId(Integer schoolId);

	/**
	 * 按部门查询
	 */
	IPage<SchoolTimetable> findSchoolTimetableByDept(Page<?> page,
			@Param("schoolTimetable") SchoolTimetable schoolTimetable, @Param("deptId") String deptId);

	/**
	 * 查询所有的课程信息
	 * @param courseId
	 * @return
	 */
    SchoolTimetable selectSchooltimetableInfo(Integer courseId);

	/**
	 * 根据课程id删除掉关联表里面的数据
	 * @param courseId
	 */
    void deleteRelateSchooltimetableInfo(Integer courseId);
	/**
	 * 循环添加数据
	 * @param param
	 */
	void insertRelateSchooltimetableInfo(Map param);

	/**
	 * 循环添加数据
	 * @param param
	 */
    void insertRelateClassInfo(Map param);

	/**
	 * 删除
	 * @param courseId
	 */
    void deleteRelateClassInfo(Integer courseId);
}
