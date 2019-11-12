package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.News;
import cc.mrbird.febs.basicInfo.entity.News;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-11-16 08:37:08
 */
public interface INewsService extends IService<News> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param News News
     * @return IPage<News>
     */
    IPage<News> findNews(QueryRequest request, News News);

    /**
     * 查询（所有）
     *
     * @param News News
     * @return List<News>
     */
    List<News> findNews(News News);
}
