package org.hj.chain.platform.common.service;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.MonitorFreqEnum;
import org.hj.chain.platform.common.MonitorFreqVo;
import org.hj.chain.platform.vo.UserParamVo;

import java.util.List;

public interface ICommonService {

    Result<List<UserParamVo>> findUsersByDeptId();

    Result<List<UserParamVo>> findSampLeaders();

    Result<List<UserParamVo>> findCheckEmps();

    /**
     * TODO 获取所有监测频次
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/16 9:07 下午
     */
    Result<List<MonitorFreqVo>> findMonitorFreqs();
}
