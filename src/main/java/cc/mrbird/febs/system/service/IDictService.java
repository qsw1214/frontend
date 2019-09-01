package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Dict;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-08-17 19:44:52
 */
public interface IDictService extends IService<Dict> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param dict dict
     * @return IPage<Dict>
     */
    IPage<Dict> findDicts(QueryRequest request, Dict dict);

    /**
     * 查询（所有）
     *
     * @param dict dict
     * @return List<Dict>
     */
    List<Dict> findDicts(Dict dict);

    /**
     * 新增
     *
     * @param dict dict
     */
    void createDict(Dict dict);

    /**
     * 修改
     *
     * @param dict dict
     */
    void updateDict(Dict dict);

    /**
     * 删除
     *
     * @param String dictIds
     */
    void deleteDicts(String dictIds);

	List<Dict> findDictsByField(String field);
}
