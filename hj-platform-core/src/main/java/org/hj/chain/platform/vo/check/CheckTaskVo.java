package org.hj.chain.platform.vo.check;

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
 * @Iteration : 1.0
 * @Date : 2021-05-14
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckTaskVo implements Serializable {
    private static final long serialVersionUID = 4845353614442144308L;
    //检测任务ID
    private Long checkTaskId;
    //任务单号
    private String jobId;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //任务状态：1-待完成；2-已完成
    private String checkTaskStatus;
    //检测因子数
    private Integer checkFactorCount;
    //已完成检测因子数
    private Integer completeCheckFactorCount;
}
