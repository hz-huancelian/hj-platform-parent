package org.hj.chain.platform.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {

    Integer getCount(@Param("organId") String organId, @Param("date") String date);

    String getContCodeByJobId(String jobId);


    /**
     * TODO 根据采样任务ID获取委托单位；
     *
     * @param taskId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/8 2:25 下午
     */
    String findCondignorNameBySampleTaskId(Long taskId);
}
