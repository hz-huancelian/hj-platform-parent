package org.hj.chain.platform.sample.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.tdo.sample.SampleItemAuditTdo;
import org.hj.chain.platform.vo.record.RecordRenderData;
import org.hj.chain.platform.vo.sample.*;
import org.hj.chain.platform.word.SampleTableData;

import java.util.List;
import java.util.Map;


/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
public interface ISampleItemService extends IService<SampleItem> {

    IPage<SampleTaskListVo> findSampTaskForManagerByCondition(PageVo pageVo, SampleTaskSearchVo sv);

    IPage<SampleTaskListVo> findSampleTaskForLeaderByCondition(PageVo pageVo, SampleTaskSearchVo sv);

    IPage<SampleItemInfoVo> findAuditSampItemForManageByCondition(PageVo pageVo, SampleItemInfoSearchVo sv);

    IPage<SampleItemInfoVo> findAuditSampItemForLeaderByCondition(PageVo pageVo, SampleItemInfoSearchVo sv);

    Result<Object> doAuditSampItemForLeader(SampleItemAuditTdo tdo);

    Result<Object> batchDoAuditSampItemForLeader(SampleItemAuditTdo tdo);

    Result<Object> doAuditSampItemForManager(SampleItemAuditTdo tdo);

    Result<Object> batchDoAuditSampItemForManager(SampleItemAuditTdo tdo);

    IPage<SampleVo> getSamplesBySampTaskId(PageVo pageVo, SampleSearchVo sv);

    Result<SampleItemVo> getSampleDataBySampItemId(Long sampItemId);

    Result<SampleDetailVo> getSampleDetailBySampItemId(Long sampItemId);

    /**
     * TODO 根据任务单号+类别查看样品现场检测渲染数据
     *
     * @param taskId
     * @param secdClassId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 11:39 下午
     */
    RecordRenderData findSampleRenderDataByTaskIdAndClassId(Long taskId, String secdClassId);

    /**
     * TODO 根据任务单号查看样品现场检测渲染数据,并根据二级类别分组
     *
     * @param taskId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 11:42 下午
     */
    Map<String, RecordRenderData> findSampleRenderDataByTaskId(Long taskId);


}
