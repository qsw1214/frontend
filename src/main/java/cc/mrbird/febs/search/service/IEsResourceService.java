package cc.mrbird.febs.search.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cc.mrbird.febs.search.entity.EsResource;

/**
 * 资源搜索管理Service
 * Created by lb on 2019/8/31.
 */
public interface IEsResourceService {
	/**
     * 从数据库中导入所有资源到ES
     */
    int importAll();

    /**
     * 根据id删除资源
     */
    void delete(Long id);

    /**
     * 根据id保存资源
     */
    EsResource save(Long id);

    /**
     * 批量删除资源
     */
    void delete(List<String> ids);

    /**
     * 根据关键字或名称
     */
    Page<EsResource> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 复合查询
     */
    Page<EsResource> search(String keyword, EsResource resource, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据资源id推荐相关资源
     */
    Page<EsResource> recommend(Long id, Integer pageNum, Integer pageSize);

	void deleteAll();
}
