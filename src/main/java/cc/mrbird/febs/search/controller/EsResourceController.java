package cc.mrbird.febs.search.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.search.entity.EsResource;
import cc.mrbird.febs.search.service.IEsResourceService;


/**
 * 搜索资源管理Controller
 * Created by lb on 2019/8/31.
 */
@RestController
@RequestMapping("/api/esResource")
public class EsResourceController extends BaseController{
	@Autowired
    private IEsResourceService esResourceService;

    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public FebsResponse search(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
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
