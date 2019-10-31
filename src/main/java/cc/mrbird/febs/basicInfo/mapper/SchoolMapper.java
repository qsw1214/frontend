package cc.mrbird.febs.basicInfo.mapper;

import cc.mrbird.febs.basicInfo.entity.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 学校表 Mapper
 *
 * @author MrBird
 * @date 2019-08-18 01:39:43
 */
public interface SchoolMapper extends BaseMapper<School> {

    public List<HashMap<String,Object>> getLast12MonthSchoolCount(@Param("school") School school);

    IPage<School> findSchool(Page page, @Param("school") School school);

}
