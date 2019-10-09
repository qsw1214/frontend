package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author psy
 * @date 2019-08-21 10:38:49
 */
public interface SchoolTimetableMapper extends BaseMapper<SchoolTimetable> {

    IPage<SchoolTimetable> findSchoolTimetables(Page page, @Param("schoolTimetable") SchoolTimetable schoolTimetable);

    Integer selectMainSchoolId(Integer schoolId);
}
