package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.resource.entity.Resource;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-08-17 19:44:02
 */
public interface IResourceService extends IService<Resource> {
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
    IPage<Resource> findDetails(Resource resource, QueryRequest request);
	
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
     * 新增
     *
     * @param resources List<Resource>
     */
    void createResources(List<Resource> resources);

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
    void deleteResources(List<String> resourceIds);
    
    /**
	 * 增加评论数
	 * @param resourceId
	 * @param num
	 */
	void increaseCommentCount(Long resourceId, Integer num);
	
	/**
	 * 增加浏览数
	 * @param resourceId
	 * @param num
	 */
	void increaseReadCount(Long resourceId, Integer num);

	/**
	 * 检查创建者是否为当前用户
	 * @param resourceIds
	 * @param username
	 * @return
	 */
	boolean checkCreator(List<String> resourceIds, String username);
	
	int updateStatus(List<String> resourceIds, Integer status);

	int getResourceCount(Integer deptId);
	
	/**
	 * 获取每个月1号的资源总数
	 * @return
	 */
	Map<String, Integer> getResourceCountEveryMonth(Integer deptId);
}
