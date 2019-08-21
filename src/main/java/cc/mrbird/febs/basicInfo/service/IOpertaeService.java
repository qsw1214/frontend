package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Operate;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 *  操作指南表 Service接口
 *  @author wsq
 */
public interface IOpertaeService extends IService<Operate> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param Operate operate
     * @return IPage<Operate>
     */
    IPage<Operate> findOperate(QueryRequest request, Operate operate);

    /**
     * 查询（所有）
     *
     * @param Operate operate
     * @return List<Operate>
     */
    List<Operate> findOperate(Operate operate);

    /**
     * 新增
     *
     * @param Operate operate
     */
    void createOperate(Operate operate);

    /**
     * 修改
     *
     * @param Operate operate
     */
    void updateOperate(Operate operate);

    /**
     * 删除
     *
     * @param Operate operate
     */
    void deleteOperate(String id);
}
