package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class OfferInfoVo implements Serializable {

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
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

//    /**
//     * 检测周期（0:无（单次） 1:周 2：月 3：季度 4：半年 5：年）
//     */
//    private String checkCircle;


//    //检测任务数
//    private Integer checkTaskCount;

    /**
     * 优惠价 拟定价
     */
    private BigDecimal draftAmount;


    //创建人
    private String createUserId;

    //备注
    private String remark;

    //说明
    private String explains;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 报价单驳回原因
     */
    private String auditReason;
}
