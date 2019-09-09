package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Abutment;
import cc.mrbird.febs.basicInfo.entity.School;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ThirdAppAbutmentService extends IService<Abutment> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param abutment school
     * @return IPage<School>
     */
    IPage<Abutment> selectAbutments(QueryRequest request, Abutment abutment);

    /**
     * 查询（所有）
     *
     * @param abutment school
     * @return List<School>
     */
    List<Abutment> selectAbutments(Abutment abutment);

    /**
     * 修改第三方应用申请
     *
     * @param abutment abutment
     */
    void updateAbutment(Abutment abutment);
}
