package org.hj.chain.platform.offer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 当报价单因子ID为同系物套餐ID时，此为同系物因子套餐内容ID（套餐可以修改，则需要复制当时套餐对象内容）
 * @Iteration : 1.0
 * @Date : 2022/3/7  4:12 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_offer_plan_factor_subset")
public class OfferPlanFactorSubset implements Serializable {
    private static final long serialVersionUID = -6392976586910571129L;

    //主键ID
    private Long id;

    //计划因子ID
    private Long planFactorId;

    /**
     * 报价单ID
     */
    private String offerId;

    //因子检测标准ID
    private String checkStandardId;

    private LocalDateTime createTime;
}