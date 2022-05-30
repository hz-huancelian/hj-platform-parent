package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SampleTask;
import org.hj.chain.platform.vo.MobileSampleTaskVo;
import org.hj.chain.platform.vo.SampleTaskVo;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 采集任务表 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface SampleTaskMapper extends BaseMapper<SampleTask> {

    /**
      * TODO 分页查询采样任务列表
      * @Author chh
      * @param page
     * @param userId
      * @Date 2021-05-08 17:54
      * @Iteration 1.0
      */
    IPage<MobileSampleTaskVo> getSampleTasksByTeamLeader(IPage<MobileSampleTaskVo> page,
                                                         @Param("userId") String userId);

    IPage<SampleTaskVo> getSampleTasksByCondition(Page<SampleTaskVo> page,
                                                 @Param("userId") String userId,
                                                 @Param("sampleStatus") String sampleStatus);

    IPage<SampleTaskVo> getSampleTasksForCollectUser(Page<SampleTaskVo> page,
                                                     @Param("userId") String userId);
}
