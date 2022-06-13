package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleTask;
import org.hj.chain.platform.vo.sample.*;
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
public interface SampleTaskMapper extends BaseMapper<SampleTask> {

    IPage<SampleTaskVo> findForManageByCondition(IPage<SampleTaskVo> page,
                                                 @Param("organId") String organId,
                                                 @Param("sv") SampleTaskSearchVo sv);

    IPage<SampleTaskVo> findForLeaderByCondition(IPage<SampleTaskVo> page,
                                                     @Param("userId") String userId,
                                                     @Param("sv") SampleTaskSearchVo sv);

    IPage<SampleTaskVo> findByCondition(Page<SampleTaskVo> page,
                                            @Param("organId") String organId,
                                            @Param("list") List<Long> deptIds,
                                            @Param("sv") SampleTaskSearchVo sv);

    SampleTaskPointVo getSampleTaskPointBySampTaskIdAndFactorPoint(@Param("sampTaskId") Long sampTaskId,
                                                                   @Param("factorPoint") String factorPoint);


    IPage<SampleTaskListVo> findSampTaskForLeaderByCondition(Page<SampleTaskListVo> page,
                                                             @Param("userId") String userId,
                                                             @Param("sv") SampleTaskSearchVo sv);

    IPage<SampleTaskListVo> findSampTaskForManageByCondition(Page<SampleTaskListVo> page,
                                                             @Param("organId") String organId,
                                                             @Param("sv") SampleTaskSearchVo sv);

    IPage<SampleTaskListVo> findSampTasksByCondition(Page<SampleTaskListVo> page,
                                                     @Param("organId") String organId,
                                                     @Param("list") List<Long> deptIds,
                                                     @Param("sv") SampleTaskSearchVo sv);

    IPage<SampleManageVo> findSamplesForJobByCondition(Page<SampleManageVo> page,
                                                       @Param("organId") String organId,
                                                       @Param("sv") SampCommSearchVo sv);


    int selectCountForAssign(String organId);

    List<SampleTask> selectListByOrganIdForCurrMonth(String organId);

    List<SampleTask> selectListByOrganIdForCurrYear(String organId);

}
