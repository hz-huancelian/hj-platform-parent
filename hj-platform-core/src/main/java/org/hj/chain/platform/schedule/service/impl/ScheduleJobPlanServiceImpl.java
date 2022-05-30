package org.hj.chain.platform.schedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlan;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanMapper;
import org.hj.chain.platform.schedule.service.IScheduleJobPlanService;
import org.springframework.stereotype.Service;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/8
 */
@Service
public class ScheduleJobPlanServiceImpl extends ServiceImpl<ScheduleJobPlanMapper, ScheduleJobPlan>
        implements IScheduleJobPlanService {
}
