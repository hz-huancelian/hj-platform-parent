package org.hj.chain.platform.schedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_schedule_job_plan_factor_subset")
public class ScheduleJobPlanFactorSubset implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //任务号
    private String jobId;
    //调度任务监测计划因子ID
    private Long jobPlanFactorId;
    //因子检测标准ID
    private String checkStandardId;

}
