package org.hj.chain.platform.tdo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfferDispatchInfoTdo implements Serializable {
    private static final long serialVersionUID = -1L;
    @NotNull(message = "任务调度ID不能为空！")
    private Long dispatchTaskId;

    @NotBlank(message = "受检单位不能为空！")
    private String inspectionName;

    @NotBlank(message = "受检联系人不能为空！")
    private String inspectionLinker;

    @NotBlank(message = "受检联系人电话不能为空！")
    private String inspectionLinkerPhone;

    @NotBlank(message = "开始日期不能为空！")
    private String startDate;

    @NotBlank(message = "结束日期不能为空！")
    private String endDate;

    @NotBlank(message = "任务地址不能为空！")
    private String taskAddress;

    //检测目的
    private String checkGoal;

    //任务备注
    private String remark;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 委托单位
     */
    private String consignorName;

    /**
     * 委托联系人
     */
    private String consignorLinker;

    /**
     * 委托联系方式
     */
    private String consignorLinkerPhone;
}
