package org.hj.chain.platform.statistics.service;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.vo.SysUserDeptVo;
import org.hj.chain.platform.vo.statistics.*;

import java.util.List;

public interface IStatisticsService {
    Result<ValidContractVo> validContracts(String type);

//    Result<List<CompleteTaskVo>> completeTask(String type);

    Result<Object> issueReport(String type);

    Result<Object> sampleClassificationCnt(String type, Integer limit);

    Result<List<SysUserDeptVo>> deptUserCnt(Integer limit);

    Result<Object> offerDispatchTaskCnt(String type);

    Result<Object> validContractsForPass11Month();

    Result<List<TaskCityVo>> taskCity(String type);

    Result<Object> staffTurnover(String type);

    Result<Object> managerToDoList();

    Result<Object> sortContAmount(String type, Integer limit);

    Result<Object> sortContNum(String type, Integer limit);

    Result<Object> ownerValidContractsForPass11Month();

    Result<Object> dispatchToDoList();

    Result<Object> offerJudgeInfoCnt(String type);

    Result<Object> offerDispatchInfoCnt(String type);

    Result<Object> cyTaskCnt(String type);

    Result<Object> sortSampledNum(String type, Integer limit);

    Result<Object> allSampledItemsForPass11Month();

    Result<Object> ownerSampledItemsForPass11Month();

    Result<Object> equipmentCnt();

    Result<Object> equipmentStatsuCnt();

    Result<Object> sampleManagementCnt(String type);

    Result<Object> storedSampleClassificationCnt(Integer limit);

    Result<Object> issueReportForPass11Month();

    Result<Object> reportStatusCnt();

    Result<Object> checkTaskCnt(String type);

    Result<Object> sortCheckedSample(String type, Integer limit);

    Result<Object> checkedSampleClassificationCnt(String type, Integer limit);

    Result<Object> ownerCheckFactorCnt(String type);

    Result<Object> cyManagerToDoList();

    Result<Object> technicalDirectorToDoList();

    Result<Object> checkManagerToDoList();

    Result<Object> sampleManagerToDoList();

    Result<Object> gmTopAggregateIndicators();

    Result<Object> sampleLeaderToDoList();
}
