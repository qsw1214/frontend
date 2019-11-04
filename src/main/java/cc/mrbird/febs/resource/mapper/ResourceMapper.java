package cc.mrbird.febs.resource.mapper;

import cc.mrbird.febs.resource.entity.Resource;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 *  Mapper
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
public interface ResourceMapper extends BaseMapper<Resource> {
	/**
     * 通过id查找资源详细信息
     *
     * @param resourceId 资源id
     * @return 资源
     */
    Resource findDetailById(Long resourceId);
    
    /**
     * 查找资源详细信息
     *
     * @param page 分页对象
     * @param resource 资源对象，用于传递查询条件
     * @return Ipage
     */
    IPage<Resource> findDetails(Page<?> page, @Param("resource") Resource resource);
    
	/**
	 * 增加评论数
	 * @param resourceId
	 * @param num
	 */
	void increaseCommentCount(@Param("resourceId") Long resourceId, @Param("num") Integer num);
	
	/**
	 * 增加浏览数
	 * @param resourceId
	 * @param num
	 */
	void increaseReadCount(@Param("resourceId") Long resourceId, @Param("num") Integer num);

	public Integer getResourceCount(@Param("deptId")Integer deptId, @Param("timeTo") String timeTo);
}
