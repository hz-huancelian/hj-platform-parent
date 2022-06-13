package org.hj.chain.platform.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.schedule.entity.ScheduleTask;
import org.hj.chain.platform.vo.schedule.ScheduleTaskSearchVo;
import org.hj.chain.platform.vo.schedule.ScheduleTaskVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleTaskMapper extends BaseMapper<ScheduleTask> {
    IPage<ScheduleTaskVo> findByCondition(Page<ScheduleTaskVo> page,
                                          @Param("organId") String organId,
                                          @Param("sv") ScheduleTaskSearchVo sv);

    /**
     * 查询待调度任务总数
     * @param organId
     * @return
     */
    Integer countJob(String organId);

    /**
     * 按月/年统计已调度任务数
     * @param organId
     * @return
     */
    Integer coutscheduledTasks(@Param("organId") String organId,@Param("type") Integer type);
}
