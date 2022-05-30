package org.hj.chain.platform.judge.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.judge.entity.JudgeTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.judgetask.JudgeRecordSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeRecordVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskVo;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 评审任务表：需要技术负责人处理；与合同签订是平行的，但是评审失败，整个合同失效 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface JudgeTaskMapper extends BaseMapper<JudgeTask> {

    IPage<JudgeTaskVo> findJudgeTaskForTecManagerByCondition(IPage<JudgeTaskVo> page,
                                                             @Param("organId") String organId,
                                                             @Param("sv") JudgeTaskSearchVo sv);

    IPage<JudgeRecordVo> findJudgeTaskForDeptManagerByCondition(IPage<JudgeRecordVo> page,
                                                                @Param("userId") String userId,
                                                                @Param("sv") JudgeRecordSearchVo sv);

    int selectCountForUnJudgement(String organId);

    int selectCountForUnContractReview(@Param("organId") String organId,
                                       @Param("userId") String userId);
}
