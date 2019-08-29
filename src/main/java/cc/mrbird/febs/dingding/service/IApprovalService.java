package cc.mrbird.febs.dingding.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.job.entity.Job;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author psy
 */
public interface IApprovalService {

    public void dealSchoolApprovalData(String processInstance);

}
