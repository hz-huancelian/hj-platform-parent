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
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单监测计划
 * @Iteration : 1.0
 * @Date : 2022/3/7  1:18 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_offer_plan")
public class OfferPlan implements Serializable {
    private static final long serialVersionUID = -3692890936344837042L;

    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 报价单ID
     */
    private String offerId;

    //计划名称
    private String planName;

    //监测频次
    private String checkFreq;

    //计划执行次数
    private Integer execTimes;

    //计划时长
    private String planTime;

    //监测因子数
    private Integer checkFactorCnt;

    //监测费用
    private BigDecimal checkFee;

    //调度次数
    private Integer scheduleTimes;

    //已调度次数
    private Integer scheduledTimes;

    //完成次数
    private Integer finishTimes;

    //上次调度日期
    private String lastScheduleDate;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;
}
