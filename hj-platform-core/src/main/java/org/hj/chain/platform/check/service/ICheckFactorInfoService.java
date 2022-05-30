package org.hj.chain.platform.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.check.BatchCheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorInfoTdo;
import org.hj.chain.platform.vo.check.CheckFactorInfoVo;
import org.hj.chain.platform.vo.check.CheckFactorSearchVo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface ICheckFactorInfoService extends IService<CheckFactorInfo> {

    Result<Object> saveCheckFactorData(CheckFactorInfoTdo tdo);

    Result<Object> submitCheckFactor(Long checkFactorId);

    Result<Object> auditCheckFactor(CheckFactorAuditTdo tdo);

    Result<IPage<CheckFactorInfoVo>> findAuditCheckFactorByCondition(PageVo pageVo, CheckFactorSearchVo sv, String reqPath);

    Result<Object> sampDrawApply(Long checkFactorId);

    Result<Object> batchAuditCheckFactor(BatchCheckFactorAuditTdo tdo);

    Result<Object> batchSampDrawApply(String checkFactorId);
}
