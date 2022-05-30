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
 * @Date : 2022/3/30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleJobVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //受检单位
    private String inspectionName;
    //受检单位联系人
    private String inspectionLinker;
    //受检单位联系方式
    private String inspectionLinkerPhone;
    //任务地址
    private String taskAddress;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
    //调度任务备注
    private String jobRemark;
}
