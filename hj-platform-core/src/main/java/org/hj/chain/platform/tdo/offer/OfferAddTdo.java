package org.hj.chain.platform.tdo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单新增
 * @Iteration : 1.0
 * @Date : 2021/5/6  4:15 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferAddTdo implements Serializable {
    private static final long serialVersionUID = -7547321580149087734L;

    //基本信息
    @NotNull(message = "报采基本信息不能为空！")
    private OfferInfoTdo offerInfoTdo;

    //监测计划集合
    @NotEmpty(message = "请至少选择一个监测计划！")
    private List<OfferPlanTdo> offerPlanTdos;

    //基本收费
    @NotNull(message = "收费信息不能为空！")
    private OfferCostTdo offerCostTdo;

    //其他收费
    private List<OfferSelfCostTdo> selfCostTdos;
}