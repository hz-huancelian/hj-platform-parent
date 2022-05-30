package org.hj.chain.platform.vo.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleTaskVo implements Serializable {
    private static final long serialVersionUID = -3245353614442144308L;
    //任务调度ID
    private Long taskId;
    //合同编号
    private String contCode;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //委托联系人
    private String consignorLinker;
    //委托联系人电话
    private String consignorLinkerPhone;
    //上次调度日期
    private String latestScheduleDate;
    //报价单所有计划总调度数
    private Integer planNum;
    //报价单所有计划调度完成次数
    private Integer scheduledPlanNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //调度标志 0-可调度;1-不可调度（存在未确认的任务）
    private String scheduleFlag;

}
