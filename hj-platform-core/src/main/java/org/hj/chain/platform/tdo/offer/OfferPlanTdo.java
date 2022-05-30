package org.hj.chain.platform.tdo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 检测计划
 * @Iteration : 1.0
 * @Date : 2022/3/7  4:46 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferPlanTdo implements Serializable {
    private static final long serialVersionUID = 6831358830401975606L;

    //计划名称
    private String planName;

    //监测频次
    private String checkFreq;

    //调度次数
    private Integer scheduleTimes;

    //计划执行次数
    private Integer execTimes;

    //计划时长
    private String planTime;

    //监测因子数
    private Integer checkFactorCnt;

    //监测费用
    private BigDecimal checkFee;

    //监测因子项
    @NotEmpty(message = "请至少选择一个监测因子！")
    private List<OfferPlanFactorTdo> factorTdoList;

}