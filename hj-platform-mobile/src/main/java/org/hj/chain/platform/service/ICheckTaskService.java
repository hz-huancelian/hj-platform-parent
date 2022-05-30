package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.MobileCheckFactorTdo;
import org.hj.chain.platform.vo.MobileCheckFactorInfoVo;
import org.hj.chain.platform.vo.MobileSampleItemDetailVo;

public interface ICheckTaskService {
    IPage<MobileCheckFactorInfoVo> findCheckTaskByCondition(PageVo pageVo, String userId, String taskStatus);

    Result<Object> batchSampDrawApply(String ids);

    Result<Object> sampDrawApply(String id);

    Result<Object> saveCheckFactorData(MobileCheckFactorTdo tdo);

    Result<MobileSampleItemDetailVo> getCheckSampItemDetail(Long id);
}
