package org.hj.chain.platform.judge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.judge.entity.JudgeTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.tdo.judgetask.JudgeRecordAuditTdo;
import org.hj.chain.platform.tdo.judgetask.JudgeTaskAuditTdo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.judgetask.JudgeRecordSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeRecordVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskVo;

import java.util.List;

/**
 * <p>
 * 评审任务表：需要技术负责人处理；与合同签订是平行的，但是评审失败，整个合同失效 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IJudgeTaskService extends IService<JudgeTask> {

    Result<IPage<JudgeTaskVo>> findJudgeTaskForTecManagerByCondition(PageVo pageVo, JudgeTaskSearchVo sv);

    Result<Object> doAuditTask(JudgeTaskAuditTdo tdo);

    Result<IPage<JudgeRecordVo>> findJudgeTaskForDeptManagerByCondition(PageVo pageVo, JudgeRecordSearchVo sv);

    Result<Object> doAuditRecord(JudgeRecordAuditTdo tdo);

    Result<List<JudgeRecordVo>> getJudgeRecordByJudgeTaskId(Long judgeTaskId);

    Result<List<UserParamVo>> findAuditUser();
}
