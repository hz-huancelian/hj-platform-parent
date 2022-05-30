package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单-监测计划
 * @Iteration : 1.0
 * @Date : 2022/3/7  10:45 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferPlanVo implements Serializable {
    private static final long serialVersionUID = -3988041435707876967L;

    //监测计划ID
    private Long offerPlanId;

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

    //监测计划关联的因子列表
    private List<OfferPlanFactorQryVo> planFactorVoList;


}