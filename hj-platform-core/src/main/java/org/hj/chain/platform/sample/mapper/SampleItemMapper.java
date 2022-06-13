package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.vo.sample.*;
import org.hj.chain.platform.vo.samplebak.ReportSampleFactorDataVo;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;
import org.hj.chain.platform.vo.statistics.SampleClassificationVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Repository
public interface SampleItemMapper extends BaseMapper<SampleItem> {

    List<SampleItem> findCompleteSampleByJobId(String jobId);

    SampleItem fuzzyQrySampItemByCondition(@Param("sampleNo") String sampleNo,
                                           @Param("organId") String organId);

    Page<SampleTaskItemVo> getSampTaskItemByTaskIdAndUserId(Page<SampleTaskItemVo> page,
                                                            @Param("sampTaskId") Long sampTaskId,
                                                            @Param("userId") String userId);

    Page<SampleTaskItemVo> getSampTaskItemById(Page<SampleTaskItemVo> page,
                                               @Param("sampTaskId") Long sampTaskId);

    Page<SampleTaskDetailVo> findSampTaskDetailByCondition(Page<SampleTaskDetailVo> page,
                                                           @Param("sv") SampleTaskDetailSearchVo sv);

    Page<SampleTaskListVo> findSampTaskForManagerByCondition(Page<SampleTaskListVo> page,
                                                             @Param("organId") String organId,
                                                             @Param("sv") SampleTaskSearchVo sv);

    Page<SampleTaskListVo> findSampleTaskForLeaderByCondition(Page<SampleTaskListVo> page,
                                                              @Param("userId") String userId,
                                                              @Param("sv") SampleTaskSearchVo sv);

    Page<SampleItemInfoVo> findAuditSampItemForManageByCondition(Page<SampleItemInfoVo> page,
                                                                 @Param("sv") SampleItemInfoSearchVo sv);

    Page<SampleItemInfoVo> findAuditSampItemForLeaderByCondition(Page<SampleItemInfoVo> page,
                                                                 @Param("userId") String userId,
                                                                 @Param("sv") SampleItemInfoSearchVo sv);

    Page<SampleVo> getSamplesBySampTaskId(Page<SampleVo> page,
                                          @Param("sv") SampleSearchVo sv);


    /**
     * TODO 根据调度任务ID获取报告采样信息
     *
     * @param jobId
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/14 9:53 上午
     */
    List<ReportSampleVo> findReportSampleInfoByJobId(@Param("jobId") String jobId, @Param("organId") String organId);

    /**
     * TODO 根据调度任务ID获取报告现场采样数据
     *
     * @param jobId
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/29 9:19 下午
     */
    List<ReportSampleFactorDataVo> findReportSampleFactorDataByJobId(@Param("jobId") String jobId, @Param("organId") String organId);


    List<SampleClassificationVo> sampleClassificationCntForCurrMonth(@Param("organId") String organId,
                                                                     @Param("list") List<String> sampleStatus);


    List<SampleClassificationVo> sampleClassificationCntForCurrYear(@Param("organId") String organId,
                                                                    @Param("list") List<String> sampleStatus);

    int selectCountForPending(String organId);

    Integer selectCountForStoreListByOrganId(String organId);

    List<SampleClassificationVo> storedSampleClassificationCnt(@Param("organId") String organId,
                                                               @Param("list") List<String> sampleStatus);
}
