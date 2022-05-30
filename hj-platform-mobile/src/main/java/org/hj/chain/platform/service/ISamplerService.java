package org.hj.chain.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.MobileSampItemDetailTdo;
import org.hj.chain.platform.tdo.MobileSampItemReviewTdo;
import org.hj.chain.platform.tdo.MobileSampleAuditTdo;
import org.hj.chain.platform.tdo.MobileSignTdo;
import org.hj.chain.platform.vo.*;

import java.util.List;

public interface ISamplerService {
    IPage<MobileSampleTaskVo> getSampleTasksForTeamLeader(PageVo pageVo, String userId);

    Result<SampleTaskItemVo> getSampleItemsBySampleTaskId(Long taskId, String userId);

    Result<SampleItemVo> getSampleDetailBySampItemId(Long sampItemId);

    Result<Object> auditSampleItem(MobileSampleAuditTdo auditVo);

    Result<Object> saveSampItemDetail(MobileSampItemDetailTdo detailTDo);

    Result<Object> submitSampItemDetail(Long sampItemId);

    Result<Object> sampStoreApply(String sampItemId);

    Result<Object> doSignForSamp(MobileSignTdo tdo);

    Result<SampleItemVo> getSampleDetailBySampleNo(String sampleNo);

    Result<List<EquipmentInfoVo>> listSampEquipments(String sampItemId);

    Result<List<EquipmentInfoVo>> listCheckEquipments(String sampItemId);

    Result<List<EquipmentInfoVo>> listCalibrationEquipments(String sampItemId);

    Result<List<UserParamVo>> listReviewUsers();

    Result<Object> reviewSampleItem(MobileSampItemReviewTdo tdo);

    Result<List<SampleAuditRecordVo>> getSampleAuditRecordBySampItemId(Long sampItemId);

    Result<IPage<SampleTaskVo>> getSampleTasksByCondition(PageVo pageVo, String sampleStatus);

    Result<SampleDbTaskItemVo> getSampleListByCondition(Long taskId, String sampleStatus);

    Result<IPage<SampleListVo>> getRejectedSampleList(PageVo pageVo);

    Result<IPage<SampleListVo>> getSampleItemsForCollectUser(PageVo pageVo, String sampleStatus);

    Result<IPage<SampleTaskVo>> getSampleTasksForCollectUser(PageVo pageVo);

    Result<SampleDbTaskItemVo> getSampleItemsForCollectUserByTaskId(Long taskId);

    Result<Object> batchReviewSampleItem(String sampleItemId);

    Result<JSONArray> checkEquimentTree();

    Result<JSONArray> calibrationEquipmentTree();

    Result<Object> checkSampleNo(String sampleNo);
}
