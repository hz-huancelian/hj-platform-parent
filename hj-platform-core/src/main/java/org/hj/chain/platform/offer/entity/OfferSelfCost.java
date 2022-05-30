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
@TableName("t_offer_self_cost")
public class OfferSelfCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 自定义费用名称
     */
    private String selfName;

    /**
     * 自定义费用（单次自定义费用*检测次数）
     */
    private BigDecimal amount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
