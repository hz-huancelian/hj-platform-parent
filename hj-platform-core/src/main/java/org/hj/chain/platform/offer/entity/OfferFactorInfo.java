package org.hj.chain.platform.offer.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("t_offer_factor_info")
public class OfferFactorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

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

    /**
     * 频次/天
     */
    private Integer frequency;

    /**
     * 天数
     */
    private Integer dayCount;

    /**
     * 总价（单价*总次数） 总次数=频次/天 * 天数
     */
    private BigDecimal totalCost;

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
