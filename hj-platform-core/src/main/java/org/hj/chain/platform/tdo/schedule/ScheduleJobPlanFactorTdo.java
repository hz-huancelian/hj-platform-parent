package org.hj.chain.platform.tdo.schedule;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Iteration : 3.0
 * @Date : 2022/3/7
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScheduleJobPlanFactorTdo implements Serializable {
    private static final long serialVersionUID = -1L;
    //监测计划ID
    private Long offerPlanId;
    @NotBlank(message = "因子二级类别ID不能为空！")
    private String secdClassId;
    @NotBlank(message = "因子检测标准ID/同系物套餐ID不能为空！")
    private String checkStandardId;
    //因子名称
    @NotBlank(message = "因子名称不能为空！")
    private String factorName;
    //分包标志 0-自检；1-分包
    @NotBlank(message = "分包标志不能为空！")
    private String fbFlag;
    //因子标志 0-因子；1-同系物套餐
    @NotBlank(message = "因子标志不能为空！")
    private String isFactor;
    //合样标志 0-未合样；1-已合样
    private String hyFlag;
    @NotNull(message = "频次/天不能为空！")
    private Integer frequency;
    @NotNull(message = "天数不能为空！")
    private Integer dayCount;
    @NotBlank(message = "检测位置不能为空！")
    private String factorPoint;
    //因子动态参数
    private String dynamicParam;
    //因子分组key
    private String factorGroupKey;
    //监测因子备注
    private String factorRemark;
    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;

}
