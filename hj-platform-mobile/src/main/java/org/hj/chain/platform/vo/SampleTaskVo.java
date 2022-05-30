package org.hj.chain.platform.vo;

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
 * @Date : 2022/4/3
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //采样任务ID
    private Long taskId;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //任务单号
    private String jobId;
    //样品数量
    private Integer sampleCount;
    //待审批样品数量
    private Integer toApproveSampleCount;
    //待采集样品数
    private Integer toCollectSampleCount;
    //待确认样品数
    private Integer toConfirmSampleCount;
    //已确认样品数
    private Integer confirmedSampleCount;
    //待入库样品数
    private Integer toStockSampleCount;
    //已入库样品数
    private Integer stockedSampleCount;
    //待复核样品数
    private Integer toReviewSampleCount;
}
