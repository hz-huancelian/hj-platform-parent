package org.hj.chain.platform.vo.samplebak;

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
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskInfoVo implements Serializable {
    private static final long serialVersionUID = 4845353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //任务单号
    private String dispatchInfoId;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //委托联系人
    private String consignorLinker;
    //委托联系方式
    private String consignorLinkerPhone;
    //受检单位
    private String inspectionName;
    //受检联系人
    private String inspectionLinker;
    //受检联系人电话
    private String inspectionLinkerPhone;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //调度人
    private String scheduUser;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //任务状态0-待分配；1-采样中；2-已完成
    private String sampTaskStatus;
    //检测目的
    private String checkGoal;
    //任务备注
    private String jobRemark;
    //工作要求
    private String offerRemark;
    //报价单号
    private String offerId;
    //合同编号
    private String contCode;
    //项目地址
    private String projectAddress;
    /**
     * 采样任务负责人
     */
    private String managerUserId;
    //采样任务负责人
    private String managerUser;

}
