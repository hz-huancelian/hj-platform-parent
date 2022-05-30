package org.hj.chain.platform.offer.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 计划因子详情表
 * @Iteration : 1.0
 * @Date : 2022/3/7  3:49 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_offer_plan_factor")
public class OfferPlanFactor implements Serializable {
    private static final long serialVersionUID = -8733224341666504620L;

    //主键ID
    private Long id;

    /**
     * 计划ID
     */
    private Long offerPlanId;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 因子检测标准ID
     */
    private String checkStandardId;

    //二级分类
    private String secdClassId;


    //是否是因子：0-是；1-同系套餐
    private String isFactor;

    //因子名称（冗余）
    private String factorName;

    /**
     * 频次/天
     */
    private Integer frequency;

    /**
     * 天数
     */
    private Integer dayCount;

    //单次费用 （总价（单价*总次数） 总次数=频次/天 * 天数）
    private BigDecimal costPerTime;

    /**
     * 检测位置
     */
    private String factorPoint;

    /**
     * 因子分组key
     */
    private String factorGroupKey;
    /**
     * 动态扩展参数：日均值等
     */
    private String dynamicParam;

    //备注
    private String remark;

    private LocalDateTime createTime;

}
