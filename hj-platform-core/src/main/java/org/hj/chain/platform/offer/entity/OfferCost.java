package org.hj.chain.platform.offer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("t_offer_cost")
public class OfferCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 检测费（单次任务检测费*检测次数）
     */
    private BigDecimal checkAmount;

    /**
     * 报告编制费（检测费*报告编制费率）
     */
    private BigDecimal reportAmount;

    /**
     * 报告编制费率，数据库转小数存储
     */
    private BigDecimal reportAmountRate;

    /**
     * 报告加急费（检测费*报告加急费率）
     */
    private BigDecimal expediteAmount;

//    /**
//     * 报告加急费率，数据库转小数存储
//     */
//    private BigDecimal expediteAmountRate;

    /**
     * 人工费（单次人工费*检测次数）
     */
    private BigDecimal laborAmount;

    /**
     * 差旅费（单次差旅费*检测次数）
     */
    private BigDecimal tripAmount;

    /**
     * 增值税金额（（检测费+报告编制费+报告加急费+人工费+差旅费+其他费用）*增值税率）
     */
    private BigDecimal taxAmount;

    /**
     * 增值税率
     */
    private BigDecimal taxAmountRate;

    /**
     * 系统报价（含税）（检测费+报告编制费+报告加急费+人工费+差旅费+其他费用+增值税金额）
     */
    private BigDecimal sysAmount;

    /**
     * 优惠价 拟定价
     */
    private BigDecimal draftAmount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
