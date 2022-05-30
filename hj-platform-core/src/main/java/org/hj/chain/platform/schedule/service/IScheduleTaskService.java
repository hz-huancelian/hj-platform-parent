package org.hj.chain.platform.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.entity.ScheduleTask;
import org.hj.chain.platform.tdo.schedule.ScheduleJobTdo;
import org.hj.chain.platform.vo.offer.OfferPlanFactorVo;
import org.hj.chain.platform.vo.schedule.*;

import java.util.List;

public interface IScheduleTaskService extends IService<ScheduleTask> {
    IPage<ScheduleTaskVo> findByCondition(PageVo pageVo, ScheduleTaskSearchVo sv);

    IPage<ScheduleJobVo> findJobForTask(PageVo pageVo, ScheduleJobSearchVo sv);

    List<ScheduleOfferPlanVo> findOfferPlanByOfferId(String offerId);

    List<OfferPlanFactorVo> findFactorsByOfferPlanId(Long offerPlanId);

    List<OfferPlanFactorVo> findFactorsByOfferPlanIds(List<Long> offerPlanIds);

    Result<Object> doScheduleJob(ScheduleJobTdo tdo, String jobStatus);

    List<ScheduleJobFactorVo> getJobFactorsByJobId(String jobId);

    Result<ScheduleJobVo> findJobByTaskId(Long taskId);

    IPage<ScheduleJobVo> findJobsByCondition(PageVo pageVo, ScheduleJobSearchVo sv);

}
