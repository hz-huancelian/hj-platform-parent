package org.hj.chain.platform.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlanFactor;
import org.hj.chain.platform.vo.check.CheckTaskFactorVo;
import org.hj.chain.platform.vo.sample.JobFactorSearchVo;
import org.hj.chain.platform.vo.sample.JobFactorVo;
import org.hj.chain.platform.vo.samplebak.SampleCheckItemParam;
import org.hj.chain.platform.vo.schedule.ScheduleJobFactorVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleJobPlanFactorMapper extends BaseMapper<ScheduleJobPlanFactor> {
    List<ScheduleJobFactorVo> getJobFactorsByJobId(String jobId);

    List<JobFactorVo> getJobFactorsByCondition(@Param("sv") JobFactorSearchVo sv);

    List<JobFactorVo> getJobFactorsBySampItemId(Long sampItemId);


    /**
     * TODO 根据样品编号查看因子名称
     *
     * @param sampleNos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/8 4:19 下午
     */
    List<SampleCheckItemParam> findFactorStandardIdsBySampleNos(@Param("list") List<String> sampleNos,
                                                                @Param("organId") String organId);

    List<CheckTaskFactorVo> getOfferFactorsByJobId(String jobId);
}
