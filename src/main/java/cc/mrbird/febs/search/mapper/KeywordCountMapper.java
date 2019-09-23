package cc.mrbird.febs.search.mapper;

import cc.mrbird.febs.search.entity.KeywordCount;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 搜索热词统计表 Mapper
 *
 * @author lb
 * @date 2019-09-22 23:15:01
 */
public interface KeywordCountMapper extends BaseMapper<KeywordCount> {
	
	List<Map<String, Object>> findKeywordsByDate(@Param("k") Integer k, @Param("searchDate") Date searchDate);
	
	List<Map<String, Object>> countKeywords(@Param("k") Integer k, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
