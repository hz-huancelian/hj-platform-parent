package org.hj.chain.platform.schedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.T;

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
@TableName("t_schedule_job_plan_factor")
public class ScheduleJobPlanFactor implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //调度任务ID
    private String jobId;
    //调度任务监测计划ID
    private Long jobPlanId;
    //因子二级类别ID
    private String secdClassId;
    //因子检测标准ID/同系物套餐ID
    private String checkStandardId;
    //因子名称
    private String factorName;
    //分包标志 0-自检；1-分包
    private String fbFlag;
    //因子标志 0-因子；1-同系物套餐
    private String isFactor;
    //合样标志 0-未合样；1-已合样
    private String hyFlag;
    //频次/天
    private Integer frequency;
    //天数
    private Integer dayCount;
    //检测位置
    private String factorPoint;
    //因子动态参数
    private String dynamicParam;
    //因子分组key
    private String factorGroupKey;
    //监测因子备注
    private String factorRemark;
    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;

}
