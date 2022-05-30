package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报单合同相关
 * @Iteration : 1.0
 * @Date : 2021/5/15  5:24 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/15    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferContVo implements Serializable {
    private static final long serialVersionUID = 3829843770359489738L;

    /**
     * 项目名称
     */
    private String projectName;

    //委托人
    private String consignorName;

    //总花费
    private BigDecimal totalCost;
}