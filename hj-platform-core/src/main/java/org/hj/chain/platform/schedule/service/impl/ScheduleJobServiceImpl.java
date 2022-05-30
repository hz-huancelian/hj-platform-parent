package org.hj.chain.platform.schedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.mapper.ScheduleJobMapper;
import org.hj.chain.platform.schedule.service.IScheduleJobService;
import org.springframework.stereotype.Service;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements IScheduleJobService {
}
