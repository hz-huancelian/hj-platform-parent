package org.hj.chain.platform.vo.approval;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

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
public class OfferApprovalVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 0-委托检测；1-样品送检
     */
    private String checkType;

    /**
     * 认证类型（1：CMA 2：CNAS
     */
    private String certificationType;

    /**
     * 报价单状态（0：草稿 1：待审核 2：审核未通过 3：审核通过）
     */
    private String status;

    /**
     * 委托单位
     */
    private String consignorName;

    /**
     * 报价人
     */
    private String offerer;

    /**
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

    //检测任务数
    private Integer checkTaskCount;

    /**
     * 优惠价 拟定价
     */
    private BigDecimal draftAmount;


    //创建人
    private String createUserId;

    private String remark;

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 报价单驳回原因
     */
    private String auditReason;

}
