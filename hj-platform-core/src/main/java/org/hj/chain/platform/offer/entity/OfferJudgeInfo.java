package org.hj.chain.platform.offer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 因子检测分包信息
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_offer_judge_info")
public class OfferJudgeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报价单ID
     */
    @TableId(value = "offer_id", type = IdType.INPUT)
    private String offerId;

    /**
     * 分包状态:0-未完成；1-完成
     */
    private String status;

    /**
     * 创建人
     */
    private String createUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
