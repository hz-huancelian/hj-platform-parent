package org.hj.chain.platform.vo.approval;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 分包判断Vo
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OfferJudgeApprovalVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //报价单号
    private String offerId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 0-委托检测；1-样品送检
     */
    private String checkType;

    /**
     * 委托单位
     */
    private String consignorName;

    //报价人
    private String createUserId;


    /**
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

    //检测任务数
    private Integer checkTaskCount;

    /**
     * 分包状态（0-未处理；1-已处理）
     */
    private String status;

    private String remark;

    //创建时间
    private Date createTime;


}
