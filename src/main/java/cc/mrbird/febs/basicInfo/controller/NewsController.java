package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.News;
import cc.mrbird.febs.basicInfo.service.INewsService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

/**
 *  Controller
 *
 * @author psy
 * @date 2019-11-16 08:37:08
 */
@Slf4j
@RestController
@RequestMapping("news")
public class NewsController extends BaseController {

    @Autowired
    private INewsService newsService;

    @GetMapping("listImg")
    //@RequiresPermissions("news:listImg")
    public FebsResponse listImg(QueryRequest request) {
        News news = new News();
        news.setClassify("pic");
        IPage<News> newsLists = this.newsService.findNews(request, news);
        Map<String, Object> dataTable = getDataTable(newsLists);
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("listJyxw")
    //@RequiresPermissions("news:listJyxw")
    public FebsResponse listJyxw(QueryRequest request) {
        News news = new News();
        news.setClassify("gzdt");
        Map<String, Object> dataTable = getDataTable(this.newsService.findNews(request,news));
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("listZcjd")
    //@RequiresPermissions("news:listZcjd")
    public FebsResponse newsList(QueryRequest request) {
        News news = new News();
        news.setClassify("zcjd");
        Map<String, Object> dataTable = getDataTable(this.newsService.findNews(request,news));
        return new FebsResponse().success().data(dataTable);
    }
}
