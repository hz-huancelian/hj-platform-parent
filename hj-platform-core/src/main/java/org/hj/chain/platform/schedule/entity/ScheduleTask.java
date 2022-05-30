package org.hj.chain.platform.schedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_schedule_task")
public class ScheduleTask implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //调度状态 0-待调度；1-调度中；2-调度完成
    private String scheduleStatus;
    //报价单ID
    private String offerId;
    //报价单所有计划总调度数
    private Integer planNum;
    //报价单所有计划调度完成次数
    private Integer scheduledPlanNum;
    //上次调度日期
    private String latestScheduleDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    //调度标志 0-可调度;1-不可调度（存在未确认的任务）
    private String scheduleFlag;

    //合同编号
    private String contCode;
    /**
     * 删除状态：0-未删除；1-删除
     */
    private String delStatus;
}
