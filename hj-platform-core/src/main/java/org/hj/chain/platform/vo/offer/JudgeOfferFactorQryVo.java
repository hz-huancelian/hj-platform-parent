package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
public class JudgeOfferFactorQryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //机构名称
    private String organName;

    //因子表
    private List<OfferPlanFactorVo> factorVos;

}
