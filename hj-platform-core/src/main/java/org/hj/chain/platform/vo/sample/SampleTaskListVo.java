package org.hj.chain.platform.vo.sample;

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
 * @Date : 2022/3/14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskListVo implements Serializable {
    private static final long serialVersionUID = 4845353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //项目名称
    private String projectName;
    //任务单号
    private String jobId;
    //受检单位
    private String inspectionName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //采样任务状态：0-待合样；1-已合样；2-采样中；3-已完成
    private String sampTaskStatus;
    //采样组长
    private List<String> sampLeader;
    //任务计划总数
    private Integer taskCount;
    //任务计划调度完成总数
    private Integer taskSn;
    //样品总数
    private Integer sampCount;
    //已完成样品总数（样品负责人审核通过后）
    private Integer completeSampCount;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
}
