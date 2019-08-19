package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.resource.entity.Resource;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
public interface IResourceService extends IService<Resource> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param resource resource
     * @return IPage<Resource>
     */
    IPage<Resource> findResources(QueryRequest request, Resource resource);

    /**
     * 查询（所有）
     *
     * @param resource resource
     * @return List<Resource>
     */
    List<Resource> findResources(Resource resource);

    /**
     * 新增
     *
     * @param resource resource
     */
    void createResource(Resource resource);

    /**
     * 修改
     *
     * @param resource resource
     */
    void updateResource(Resource resource);

    /**
     * 删除
     * @param resourceIds
     */
    void deleteResources(String resourceIds);
    
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
}
