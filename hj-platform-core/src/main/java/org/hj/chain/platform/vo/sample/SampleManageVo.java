package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleManageVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //任务单号
    private String jobId;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //样品总数
    private Integer totalSamples;
    //入库样品数（样品状态：已入库、部分出库）
    private Integer inBoundSamples;
    //出库样品数（样品状态：已出库）
    private Integer outBoundSamples;
}
