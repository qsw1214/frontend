package cc.mrbird.febs.search.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cc.mrbird.febs.search.entity.EsResource;

/**
 * 资源ES操作类
 * Created by lb on 2019/8/31.
 */
public interface EsResourceRepository extends ElasticsearchRepository<EsResource, Long>{
	/**
     * 搜索查询
     *
     * @param name              资源名称
     * @param keywords          资源关键字
     * @param page              分页信息
     * @return
     */
    Page<EsResource> findByResourceNameOrKeywords(String resourceName, String keywords,Pageable page);
   
}
