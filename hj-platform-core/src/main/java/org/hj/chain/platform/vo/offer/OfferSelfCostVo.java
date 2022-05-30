package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfferSelfCostVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自定义费用名称
     */
    private String selfName;

    /**
     * 自定义费用（单次自定义费用*检测次数）
     */
    private BigDecimal amount;

}
