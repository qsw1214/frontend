package cc.mrbird.febs.search.service;

import cc.mrbird.febs.search.entity.KeywordCount;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 搜索热词统计表 Service接口
 *
 * @author lb
 * @date 2019-09-22 23:15:01
 */
public interface IKeywordCountService extends IService<KeywordCount> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param keywordCount keywordCount
     * @return IPage<KeywordCount>
     */
    IPage<KeywordCount> findKeywordCounts(QueryRequest request, KeywordCount keywordCount);

    /**
     * 获取最热的k个词
     * @param k 数量
     * @param countDate 日期
     * @return
     */
    List<Map<String, Object>> findKeywordsByDate(Integer k, Date countDate);
    
    /**
     * 按时间段统计热词搜索次数，获取前k个词
     * @param k
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> countKeywords(Integer k, Date startDate, Date endDate);

    /**
     * 新增
     *
     * @param keywordCount keywordCount
     */
    void createKeywordCount(KeywordCount keywordCount);

    /**
     * 修改
     *
     * @param keywordCount keywordCount
     */
    void updateKeywordCount(KeywordCount keywordCount);

    /**
     * 删除
     *
     * @param keywordCount keywordCount
     */
    void deleteKeywordCount(KeywordCount keywordCount);
    
    /**
     * 获取最新的记录
     * @return
     */
    KeywordCount getLatestKeyword();
    
}
