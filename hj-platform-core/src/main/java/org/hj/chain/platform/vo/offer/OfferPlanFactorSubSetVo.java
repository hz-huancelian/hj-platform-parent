package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 当选择同系因子套餐时，此类为因子套餐中的子因子
 * @Iteration : 1.0
 * @Date : 2022/3/7  11:13 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferPlanFactorSubSetVo implements Serializable {
    private static final long serialVersionUID = 4593949462529365896L;

    //因子检测标准ID
    private String checkStandardId;

    //因子名称
    private String factorName;

    //检测对象
    private String classId;

    //标准号
    private String standardNo;

    //标准名称
    private String standardName;

    //标准分析方法
    private String analysisMethod;

    //标准状态
    private String methodStatus;

    //费用
    private BigDecimal price;
}