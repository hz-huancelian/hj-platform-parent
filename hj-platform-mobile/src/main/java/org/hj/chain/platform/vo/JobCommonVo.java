package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/4/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JobCommonVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //受检单位
    private String inspectionName;
    //样品数量
    private Integer sampleCount;
    //任务单号
    private String jobId;
}
