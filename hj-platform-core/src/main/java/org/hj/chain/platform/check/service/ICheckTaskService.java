package org.hj.chain.platform.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.check.entity.CheckTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.vo.check.*;

import java.util.List;

/**
 * <p>
 * 检测任务信息 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface ICheckTaskService extends IService<CheckTask> {

    Result<IPage<CheckTaskInfoVo>> findByCondition(PageVo pageVo, CheckTaskInfoSearchVo sv, String reqPath);

    Result<List<CheckTaskFactorVo>> getOfferFactorsByTaskId(Long checkTaskId);

    Result<Object> assignmentsFactor(String param);

    Result<IPage<CheckFactorInfoVo>> getCheckFactorByCondition(PageVo pageVo, CheckFactorSearchVo sv, Long checkTaskId);

    Result<List<CheckUserVo>> getCheckUsers();

    Result<IPage<CheckTaskVo>> findCheckTaskByCondition(PageVo pageVo, CheckTaskSearchVo sv, String reqPath);

    Result<IPage<CheckFactorInfoVo>> findCheckTaskDetailByCondition(PageVo pageVo, CheckFactorSearchVo sv, String reqPath, String type);
}
