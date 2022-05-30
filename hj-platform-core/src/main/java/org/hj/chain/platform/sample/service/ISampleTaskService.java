package org.hj.chain.platform.sample.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.entity.SampleList;
import org.hj.chain.platform.sample.entity.SampleTask;
import org.hj.chain.platform.vo.sample.*;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
public interface ISampleTaskService extends IService<SampleTask> {

    Result<IPage<SampleTaskVo>> findByCondition(PageVo pageVo, SampleTaskSearchVo sv, String reqPath);

    List<JobFactorVo> getJobFactorsByJobId(String jobId);

    List<String> getFactorPointsByJobId(String jobId);

    List<JobFactorVo> getJobFactorsByCondition(JobFactorSearchVo sv);

    List<SampleList> getSampleListByCondition(Long sampTaskId, String fbFlag);

    Result<Object> saveSampleList(List<SampleList> list);

    Result<Object> deleteSampleListById(Long sampleListId);

    Result<Object> confirmSampleList(Long sampTaskId);

    Result<Object> assignFactorPoint(String param);

    List<SampleTaskPointVo> getSampleTaskPointBySampTaskId(Long sampTaskId);

    Result<Object> saveCombinedRemark(String param);

    IPage<SampleTaskItemVo> getSampleItemBySampTaskId(PageVo pageVo, Long sampTaskId, String reqPath);

    IPage<SampleTaskListVo> findSampTaskByCondition(PageVo pageVo, SampleTaskSearchVo sv, String reqPath);

    IPage<SampleTaskDetailVo> findSampTaskDetailByCondition(PageVo pageVo, SampleTaskDetailSearchVo sv);

    IPage<SampleManageVo> findSamplesForJobByCondition(PageVo pageVo, SampCommSearchVo sv);
}
