package org.hj.chain.platform.vo.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleJobVo implements Serializable {
    private static final long serialVersionUID = -3245353614442144308L;
    //调度任务ID
    private String jobId;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //受检单位
    private String inspectionName;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
    //调度任务状态：0-待确认；1：未完成；2:完成
    private String jobStatus;
    //任务创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    /* 任务进度状态 */
    //是否采样 0-否 1-是
    private String isSampled;
    //是否检测 0-否 1-是
    private String isChecked;
    //任务报告状态 0-待分配；1-待制作；2-已制作；3-待审批；4-待签发；5-完成；6-驳回
    private String reportStatus;

    private String inspectionLinker;
    private String inspectionLinkerPhone;
    private String projectAddress;
    private String checkGoal;
    private String jobRemark;
    private String province;
    private String city;
    private String county;
    private String consignorLinker;
    private String consignorLinkerPhone;

    //调度任务关联的监测计划ID
    private List<Long> offerPlanIds;
}
