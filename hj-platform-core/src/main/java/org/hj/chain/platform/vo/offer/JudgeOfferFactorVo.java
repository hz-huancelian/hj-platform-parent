package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 检测因子项
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JudgeOfferFactorVo extends OfferPlanFactorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //0-有能力；1-无能力
    private String extAssistFlg;


}
