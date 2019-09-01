package cc.mrbird.febs.search.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cc.mrbird.febs.search.entity.EsResource;

public interface EsResourceMapper {
	List<EsResource> getAllEsResourceList(@Param("resourceId") Long resourceId);
}
