package cc.mrbird.febs.search.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.search.entity.EsResource;
import cc.mrbird.febs.search.entity.KeywordCount;
import cc.mrbird.febs.search.service.IEsResourceService;
import cc.mrbird.febs.search.service.IKeywordCountService;


/**
 * 搜索资源管理Controller
 * Created by lb on 2019/8/31.
 */
@RestController
@RequestMapping("/esResource")
public class EsResourceController extends BaseController{
	@Autowired
    private IEsResourceService esResourceService;
	@Autowired
    private IKeywordCountService keywordCountService;
	private final long nd = 1000 * 24 * 60 * 60;
	
	private static final Logger keywordLog = LoggerFactory.getLogger("keyword");
	
	@RequestMapping(value = "/search/import", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse importAll() {
        return new FebsResponse().success().data(esResourceService.importAll());
    }
	
	/**
     * 获取资源数量(deptId为null时，返回所有资源总数)
     * @param deptId 指定部门
     */
	@RequestMapping(value = "/reource/count", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse getCount(@RequestParam(required = false) Long deptId) {
        return new FebsResponse().success().data(esResourceService.getCount(deptId));
    }
	
	/**
	 * 获取搜索热词
	 * @param k 热词数量
	 * @param date 指定日期
	 */
	@RequestMapping(value = "/search/keyword/date", method = RequestMethod.GET)
	@ResponseBody
	public FebsResponse keywordByDate(@RequestParam(required = false, defaultValue = "5") Integer k, Date date) {
		if (date == null) {
			KeywordCount latestRecord = keywordCountService.getLatestKeyword();
			if (latestRecord == null)
				return new FebsResponse().success().data(new ArrayList<>());
			date = latestRecord.getSearchDate();
		}
		return new FebsResponse().success().data(keywordCountService.findKeywordsByDate(k, date));
	}

	/**
	 * 获取搜索热词
	 * @param k 热词数量
	 * @param startDate 开始日期
	 * @param endDate 介绍日期
	 */
	@RequestMapping(value = "/search/keyword/period", method = RequestMethod.GET)
	@ResponseBody
	public FebsResponse keywordByPeriod(@RequestParam(required = false, defaultValue = "5") Integer k, Date startDate,
			Date endDate) {
		if (startDate == null || endDate == null)
			return new FebsResponse().fail().data("startDate or endDate is null");
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();
		if (diff < 0)
			return new FebsResponse().fail().data("开始日期不能小于结束日期");
		// 计算差多少天
		long day = diff / nd;
		if (day > 31)
			return new FebsResponse().fail().data("时间间隔不能超过31天");
		return new FebsResponse().success().data(keywordCountService.countKeywords(k, startDate, endDate));
	}
	
	/**
     * 获取资源详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse detail(Long id) {
    	EsResource r = esResourceService.get(id);
        return new FebsResponse().success().data(r);
    }

    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsResource> esResourcePage = esResourceService.search(keyword, pageNum, pageSize);
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse search(@RequestParam(required = false) String keyword, EsResource resource,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                      @RequestParam(required = false, defaultValue = "0") Integer sort) {
    	// 将搜索词记录到日志,便于统计热词
    	if(keyword != null && !keyword.equals("")){
    		String[] words = esResourceService.getAnalyzes("rms", keyword);  		
    		keywordLog.info(keyword + "|" + StringUtils.join(words, ","));
    	}
        Page<EsResource> esResourcePage = esResourceService.search(keyword, resource, pageNum, pageSize, sort);
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }

    @RequestMapping(value = "/recommend/{id}", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse recommend(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsResource> esResourcePage = esResourceService.recommend(id, pageNum, pageSize);
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }
    
    /*
    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
    @ResponseBody
    public FebsResponse importAllList() {
        int count = esResourceService.importAll();
        return new FebsResponse().success().data(count);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse delete(@PathVariable Long id) {
        esResourceService.delete(id);
        return new FebsResponse().success();
    }
    
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse deleteAll() {
        esResourceService.deleteAll();
        return new FebsResponse().success();
    }

    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public FebsResponse delete(@RequestParam("ids") List<String> ids) {
        esResourceService.delete(ids);
        return new FebsResponse().success();
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    @ResponseBody
    public FebsResponse create(@PathVariable Long id) {
        EsResource esResource = esResourceService.save(id);
        if (esResource != null) {
            return new FebsResponse().success().data(esResource);
        } else {
            return new FebsResponse().fail();
        }
    }
    */
}
