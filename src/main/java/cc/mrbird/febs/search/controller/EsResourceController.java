package cc.mrbird.febs.search.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.service.RedisService;
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
	@Autowired
    private RedisService redisService;
	
	private final long nd = 1000 * 24 * 60 * 60;
	
	private static final Logger searchLog = LoggerFactory.getLogger("search");
	
	@RequestMapping(value = "/search/import", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse importAll() {
        return new FebsResponse().success().data(esResourceService.importAll());
    }
	
	/**
     * 获取资源详情接口访问量
     */
	@RequestMapping(value = "/detail/visit", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse getApiVisit() {
		Map<String, String> map = new HashMap<>();
		for(int i=0; i<24; i++){
			String key = ""+i;
			if(i<10)
				key = "0" + i;
			if(redisService.hget(FebsConstant.RES_DETAIL_API_VISIT, key) != null)
				map.put(key, redisService.hget(FebsConstant.RES_DETAIL_API_VISIT, key));
			else
				map.put(key, "0");
		}
		return new FebsResponse().success().data(map);
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
    public FebsResponse detail(@RequestParam(required = true)  Long id) {
    	EsResource r = esResourceService.get(id);
    	//获取整小时
    	Date date =new Date(System.currentTimeMillis()); 
    	SimpleDateFormat formatter = new SimpleDateFormat("HH"); 
    	String time = formatter.format(date);
    	redisService.hincrby(FebsConstant.RES_DETAIL_API_VISIT, time, 1L);
    	if(r != null){
    		redisService.hincrby(FebsConstant.RES_VISIT, id.toString(), 1L);
    		String num = redisService.hget(FebsConstant.RES_VISIT, id.toString());
			r.setReadCount(r.getReadCount() + Long.valueOf(num));
    	}
        return new FebsResponse().success().data(r);
    }

    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    	pageNum = pageNum - 1;
    	// 将搜索词记录到日志,便于统计热词
    	if(keyword != null && !keyword.equals("")){  		
    		searchLog.info("keyword|" + keyword);
    	}
    	Page<EsResource> esResourcePage = esResourceService.search(keyword, pageNum, pageSize);
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }


	// "排序字段:0->按相关度；1->按新品；2->按阅读量；3->评分从高到低"
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse search(@RequestParam(required = false) String keyword, EsResource resource,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                      @RequestParam(required = false, defaultValue = "0") Integer sort) {
    	pageNum = pageNum - 1;
    	if(pageNum < 0)
    		pageNum = 0;
    	// 将搜索词记录到日志,便于统计热词
    	if(keyword != null && !keyword.equals("")){  		
    		searchLog.info("keyword|" + keyword);
    	}
        Page<EsResource> esResourcePage = esResourceService.search(keyword, resource, pageNum, pageSize, sort); 
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }

    @RequestMapping(value = "/recommend/{id}", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse recommend(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
    	pageNum = pageNum - 1;
    	if(pageNum < 0)
    		pageNum = 0;
    	Page<EsResource> esResourcePage = esResourceService.recommend(id, pageNum, pageSize);
        Map<String, Object> dataTable = getDataTable(esResourcePage);
        return new FebsResponse().success().data(dataTable);
    }
    
    /**
     * 获取每月新增资源数量
     */
	@RequestMapping(value = "/coutByMonth", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse coutByMonth() {
		return new FebsResponse().success().data(esResourceService.countByMonth());
    }
    
}
