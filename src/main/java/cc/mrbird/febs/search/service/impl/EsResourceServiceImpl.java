package cc.mrbird.febs.search.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cc.mrbird.febs.search.entity.EsResource;
import cc.mrbird.febs.search.mapper.EsResourceMapper;
import cc.mrbird.febs.search.repository.EsResourceRepository;
import cc.mrbird.febs.search.service.IEsResourceService;

/**
 * 资源搜索管理Service实现类 Created by lb on 2019/8/31.
 */
@Service
public class EsResourceServiceImpl implements IEsResourceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EsResourceServiceImpl.class);
	@Autowired
	private EsResourceMapper esResourceDao;
	@Autowired
	private EsResourceRepository resourceRepository;
	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;


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
	public EsResource get(Long id) {
		return resourceRepository.findById(id).get();
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
	public EsResource save(EsResource esResource) {
		return resourceRepository.save(esResource);
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
	public Page<EsResource> search(String keyword, EsResource resource, Integer pageNum, Integer pageSize,
			Integer sort) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		
		// 分页
		nativeSearchQueryBuilder.withPageable(pageable);
		// 过滤
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (resource.getDeptId() != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("deptId", resource.getDeptId()));
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

		// 搜索
		if (StringUtils.isEmpty(keyword)) {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
		} else {
			List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("resourceName", keyword), ScoreFunctionBuilders.weightFactorFunction(10)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("keywords", keyword), ScoreFunctionBuilders.weightFactorFunction(2)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("gradeName", keyword), ScoreFunctionBuilders.weightFactorFunction(2)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("subjectName", keyword), ScoreFunctionBuilders.weightFactorFunction(2)));
			FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders
					.size()];
			filterFunctionBuilders.toArray(builders);
			FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
					.scoreMode(FunctionScoreQuery.ScoreMode.SUM).setMinScore(2);
			nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
		}
		// 排序
		if (sort == 1) {
			// 按新品从新到旧
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
		} else if (sort == 2) {
			// 按阅读量从高到低
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("readCount").order(SortOrder.DESC));
		} else if (sort == 3) {
			// 按评分从高到低
			nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("star").order(SortOrder.DESC));
		} else {
			// 按相关度
			nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
		}
		
		//String[] fields = {"resourceId","resourceName","creator",};
		//SourceFilter sf = new FetchSourceFilter(fields, null);
		//nativeSearchQueryBuilder.withSourceFilter(sf);
		NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();		
		// LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
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

			List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("resourceName", keyword), ScoreFunctionBuilders.weightFactorFunction(8)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("keywords", keyword), ScoreFunctionBuilders.weightFactorFunction(2)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("gradeId", gradeId), ScoreFunctionBuilders.weightFactorFunction(10)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("subjectId", subjectId), ScoreFunctionBuilders.weightFactorFunction(10)));
			filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
					QueryBuilders.matchQuery("categoryId", categoryId), ScoreFunctionBuilders.weightFactorFunction(6)));
			FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders
					.size()];
			filterFunctionBuilders.toArray(builders);
			FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
					.scoreMode(FunctionScoreQuery.ScoreMode.SUM).setMinScore(2);
			NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
			builder.withQuery(functionScoreQueryBuilder);
			builder.withPageable(pageable);
			NativeSearchQuery searchQuery = builder.build();
			LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
			return resourceRepository.search(searchQuery);
		}
		return new PageImpl<>(null);
	}

	/**
	 * 默认使用中文ik_smart分词
	 * @param index 索引index
	 * @param text 需要被分析的词语
	 * @return
	 */
	@Override
	public String[] getAnalyzes(String index, String text) {
		// 调用ES客户端分词器进行分词
		AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
				AnalyzeAction.INSTANCE, index, text).setAnalyzer("ik_smart");
		List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();

		// 赋值
		List<String> searchTermList = new ArrayList<>();
		ikTokenList.forEach(ikToken -> {
			searchTermList.add(ikToken.getTerm());
		});

		return searchTermList.toArray(new String[searchTermList.size()]);
	}

	/**
	 * 搜索建议
	 * @param clazz 指定的索引index实体类类型
	 * @param text 搜索建议关键词
	 * @return
	 */
	@Override
	public String[] getSuggestion(Class clazz, String text) {
		// 构造搜索建议语句
		SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("resourceName")
				.prefix(text, Fuzziness.AUTO);

		// 根据
		final SearchResponse suggestResponse = elasticsearchTemplate
				.suggest(new SuggestBuilder().addSuggestion("my-suggest", completionSuggestionFuzzyBuilder), clazz);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("my-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		System.err.println(options);
		System.out.println(options.size());
		System.out.println(options.get(0).getText().string());

		List<String> suggestList = new ArrayList<>();
		options.forEach(item -> {
			suggestList.add(item.getText().toString());
		});

		return suggestList.toArray(new String[suggestList.size()]);
	}

}
