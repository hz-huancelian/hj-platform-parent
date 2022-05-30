package org.hj.chain.platform.vo.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/7
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleOfferPlanVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long offerPlanId;
    //计划名称
    private String planName;
    //检测频次
    private String checkFreq;
    //监测因字数
    private Integer checkFactorCnt;
    //总调度次数
    private Integer scheduleTimes;
    //已调度次数
    private Integer scheduledTimes;
    //完成次数
    private Integer finishTimes;
    //上次调度日期
    private String lastScheduleDate;
}
