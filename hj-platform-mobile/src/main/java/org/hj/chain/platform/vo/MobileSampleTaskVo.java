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
 * @Iteration : 1.0
 * @Date : 2021-05-05
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleTaskVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //采样任务ID
    private Long taskId;
    //采样任务状态：2-采样中；3-已完成
    private String taskStatus;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
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
    //任务单号
    private String jobId;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
    //任务地址
    private String taskAddress;
    //调度备注
    private String dispatchRemark;
    //厂方签字图片
    private String singImageUrl;
    //样品数量
    private Integer sampleCount;
}
