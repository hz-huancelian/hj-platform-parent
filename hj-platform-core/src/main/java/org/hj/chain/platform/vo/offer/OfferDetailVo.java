package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报单详情Vo
 * @Iteration : 1.0
 * @Date : 2021/5/9  2:32 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/09    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferDetailVo implements Serializable {
    private static final long serialVersionUID = -6845353614442144308L;

    //报价单号
    private String offerId;

    //报单基本信息
    private OfferBaseVo offerBaseVo;

    //监测计划列表
    private List<OfferPlanVo> offerPlanVos;

    //费用总计信息
    private OfferCostVo costVo;

    //自定义费用项
    private List<OfferSelfCostVo> selfCostVos;
}