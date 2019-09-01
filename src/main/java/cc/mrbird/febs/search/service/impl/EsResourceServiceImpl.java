package cc.mrbird.febs.search.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cc.mrbird.febs.search.entity.EsResource;
import cc.mrbird.febs.search.mapper.EsResourceMapper;
import cc.mrbird.febs.search.repository.EsResourceRepository;
import cc.mrbird.febs.search.service.IEsResourceService;

/**
 * 资源搜索管理Service实现类
 * Created by lb on 2019/8/31.
 */
@Service
public class EsResourceServiceImpl implements IEsResourceService{
	 private static final Logger LOGGER = LoggerFactory.getLogger(EsResourceServiceImpl.class);
	    @Autowired
	    private EsResourceMapper esResourceDao;
	    @Autowired
	    private EsResourceRepository resourceRepository;

	    @Override
	    public int importAll() {
	    	resourceRepository.deleteAll();
	        List<EsResource> esResourceList = esResourceDao.getAllEsResourceList(null);
	        Iterable<EsResource> esResourceIterable = resourceRepository.saveAll(esResourceList);
	        Iterator<EsResource> iterator = esResourceIterable.iterator();
	        int result = 0;
	        while (iterator.hasNext()) {
	            result++;
	            iterator.next();
	        }
	        return result;
	    }

	    @Override
	    public void delete(Long id) {
	        resourceRepository.deleteById(id);
	    }
	    
	    @Override
	    public void deleteAll() {
	        resourceRepository.deleteAll();
	    }

	    @Override
	    public EsResource save(Long id) {
	        EsResource result = null;
	        List<EsResource> esResourceList = esResourceDao.getAllEsResourceList(id);
	        if (esResourceList.size() > 0) {
	            EsResource esResource = esResourceList.get(0);
	            result = resourceRepository.save(esResource);
	        }
	        return result;
	    }

	    @Override
	    public void delete(List<String> ids) {
	        if (!CollectionUtils.isEmpty(ids)) {
	            List<EsResource> esResourceList = new ArrayList<>();
	            for (String id : ids) {
	                EsResource esResource = new EsResource();
	                esResource.setResourceId(Long.valueOf(id));
	                esResourceList.add(esResource);
	            }
	            resourceRepository.deleteAll(esResourceList);
	        }
	    }

	    @Override
	    public Page<EsResource> search(String keyword, Integer pageNum, Integer pageSize) {
	        Pageable pageable = PageRequest.of(pageNum, pageSize);
	        return resourceRepository.findByResourceNameOrKeywords(keyword, keyword, pageable);
	    }

	    @Override
	    public Page<EsResource> search(String keyword, EsResource resource, Integer pageNum, Integer pageSize,Integer sort) {
	        Pageable pageable = PageRequest.of(pageNum, pageSize);
	        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
	        //分页
	        nativeSearchQueryBuilder.withPageable(pageable);
	        //过滤
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (resource.getSchoolId() != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("schoolId", resource.getSchoolId()));
            }
            if (resource.getGradeId() != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("gradeId", resource.getGradeId()));
            }
            if (resource.getSubjectId() != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("subjectId", resource.getSubjectId()));
            }
            if (resource.getFileType() != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("fileType", resource.getFileType()));
            }
            if (resource.getCategoryId() != null) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", resource.getCategoryId()));
            }
            nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

	        //搜索
	        if (StringUtils.isEmpty(keyword)) {
	            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
	        } else {
	            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("resourceName", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(10)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("keywords", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(2)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("gradeName", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(2)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("subjectName", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(2)));
	            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
	            filterFunctionBuilders.toArray(builders);
	            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
	                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
	                    .setMinScore(2);
	            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
	        }
	        //排序
	        if(sort==1){
	            //按新品从新到旧
	            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
	        }else if(sort==2){
	            //按阅读量从高到低
	            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("readCount").order(SortOrder.DESC));
	        }else if(sort==3){
	            //按评分从高到低
	            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("star").order(SortOrder.DESC));
	        }else{
	            //按相关度
	            nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
	        }
	        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
	        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
//	        LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
	        return resourceRepository.search(searchQuery);
	    }

	    @Override
	    public Page<EsResource> recommend(Long id, Integer pageNum, Integer pageSize) {
	        Pageable pageable = PageRequest.of(pageNum, pageSize);
	        List<EsResource> esResourceList = esResourceDao.getAllEsResourceList(id);
	        if (esResourceList.size() > 0) {
	            EsResource esResource = esResourceList.get(0);
	            String keyword = esResource.getResourceName();
	            Integer gradeId = esResource.getGradeId();
	            Integer subjectId = esResource.getSubjectId();
	            Long categoryId = esResource.getCategoryId();
	            //根据商品标题、学校、分类进行搜索
	            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("resourceName", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(8)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("keywords", keyword),
	                    ScoreFunctionBuilders.weightFactorFunction(2)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("gradeId", gradeId),
	                    ScoreFunctionBuilders.weightFactorFunction(10)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("subjectId", subjectId),
	                    ScoreFunctionBuilders.weightFactorFunction(10)));
	            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("categoryId", categoryId),
	                    ScoreFunctionBuilders.weightFactorFunction(6)));
	            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
	            filterFunctionBuilders.toArray(builders);
	            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
	                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
	                    .setMinScore(2);
	            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
	            builder.withQuery(functionScoreQueryBuilder);
	            builder.withPageable(pageable);
	            NativeSearchQuery searchQuery = builder.build();
	            LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
	            return resourceRepository.search(searchQuery);
	        }
	        return new PageImpl<>(null);
	    }    
	  
}
