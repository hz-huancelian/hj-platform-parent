package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskVo implements Serializable {
    private static final long serialVersionUID = 4845353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //项目名称
    private String projectName;
    //任务单号
    private String dispatchInfoId;
    //受检单位
    private String inspectionName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //采样状态 1-未完成；2-已完成
    private String sampTaskStatus;
    //采样组长
    private List<String> sampLeader;
    //任务总数
    private Integer taskCount;
    //任务序号
    private Integer taskSn;
    //样品总数
    private Integer sampCount;
    //已完成样品总数（样品负责人审核通过后）
    private Integer completeSampCount;
}
