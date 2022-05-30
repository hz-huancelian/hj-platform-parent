package org.hj.chain.platform.check.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.check.CheckFactorInfoVo;
import org.hj.chain.platform.vo.check.CheckFactorSearchVo;
import org.hj.chain.platform.vo.check.CheckUserVo;
import org.hj.chain.platform.vo.check.ReportCheckDetailVo;
import org.hj.chain.platform.vo.statistics.SampleClassificationVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface CheckFactorInfoMapper extends BaseMapper<CheckFactorInfo> {

    IPage<CheckFactorInfoVo> findCheckFactorForCheckManagerByCondition(IPage<CheckFactorInfoVo> page,
                                                                       @Param("organId") String organId,
                                                                       @Param("sv") CheckFactorSearchVo sv);

    IPage<CheckFactorInfoVo> findCheckFactorForCheckUserByCondition(IPage<CheckFactorInfoVo> page,
                                                                    @Param("organId") String organId,
                                                                    @Param("userId") String userId,
                                                                    @Param("sv") CheckFactorSearchVo sv);

    IPage<CheckFactorInfoVo> getCheckFactorByCondition(IPage<CheckFactorInfoVo> page,
                                                       @Param("sv") CheckFactorSearchVo sv,
                                                       @Param("checkTaskId") Long checkTaskId);

    List<CheckUserVo> getCheckUserFactors();


    /**
     * TODO 根据样品ID查看检测信息
     *
     * @param sampleItemIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/25 5:31 下午
     */
    List<ReportCheckDetailVo> findCheckDetailBySampleItemIds(@Param("list") List<String> sampleItemIds,
                                                             @Param("organId") String organId);

    IPage<CheckFactorInfoVo> findCheckTaskDetailForManageByCondition(Page<CheckFactorInfoVo> page,
                                                                     @Param("organId") String organId,
                                                                     @Param("sv") CheckFactorSearchVo sv,
                                                                     @Param("list") List<String> statusList);

    IPage<CheckFactorInfoVo> findCheckTaskDetailForEmpByCondition(Page<CheckFactorInfoVo> page,
                                                                  @Param("userId") String userId,
                                                                  @Param("organId") String organId,
                                                                  @Param("sv") CheckFactorSearchVo sv,
                                                                  @Param("list") List<String> statusList);

    IPage<CheckFactorInfoVo> findCheckFactorForDeptByCondition(Page<CheckFactorInfoVo> page,
                                                               @Param("list") List<Long> deptIds,
                                                               @Param("organId") String organId,
                                                               @Param("sv") CheckFactorSearchVo sv);

    List<Map<String, Object>> sortCheckedSampleForCurrMonth(String organId);

    List<Map<String, Object>> sortCheckedSampleForCurrYear(String organId);

    List<SampleClassificationVo> checkedSampleClassificationCnt(String organId);

    List<CheckFactorInfo> ownerCheckFactorCntForCurrMonth(@Param("organId") String organId,
                                                          @Param("userId") String userId);

    List<CheckFactorInfo> ownerCheckFactorCntForCurrYear(@Param("organId") String organId,
                                                         @Param("userId") String userId);

    int selectCountPendingReviewCheckFactor(String organId);

    List<SampleClassificationVo> checkedSampleClassificationCntForCurrMonth(String organId);

    List<SampleClassificationVo> checkedSampleClassificationCntForCurrYear(String organId);

    List<CheckFactorInfo> findCompleteCheckFactorByJobId(String jobId);
}
