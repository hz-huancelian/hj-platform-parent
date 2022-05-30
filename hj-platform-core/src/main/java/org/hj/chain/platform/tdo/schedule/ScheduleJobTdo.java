package org.hj.chain.platform.tdo.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScheduleJobTdo implements Serializable {
    private static final long serialVersionUID = -1L;
    @NotNull(message = "任务调度ID不能为空！")
    private Long taskId;
    //任务号
    private String jobId;
//    @NotBlank(message = "受检单位不能为空！")
    private String inspectionName;
//    @NotBlank(message = "受检联系人不能为空！")
    private String inspectionLinker;
//    @NotBlank(message = "受检联系人电话不能为空！")
    private String inspectionLinkerPhone;
//    @NotBlank(message = "开始日期不能为空！")
    private String startDate;
//    @NotBlank(message = "结束日期不能为空！")
    private String endDate;
//    @NotBlank(message = "项目地址不能为空！")
    private String projectAddress;
    //检测目的
    private String checkGoal;
    //调度任务备注
    private String jobRemark;
    //省份
    private String province;
    //城市
    private String city;
    //区县
    private String county;
    /* 冗余数据 */
    @NotBlank(message = "委托单位不能为空！")
    private String consignorName;
    @NotBlank(message = "委托联系人不能为空！")
    private String consignorLinker;
    @NotBlank(message = "委托联系方式不能为空！")
    private String consignorLinkerPhone;
    @NotBlank(message = "项目名称不能为空！")
    private String projectName;

    /* 监测因子数据 */
    @NotEmpty(message = "检测因子不能为空！")
    private List<ScheduleJobPlanFactorTdo> factorTdos;

}
