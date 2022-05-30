package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class OfferBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 委托联系人
     */
    private String consignorLinker;

    /**
     * 委托联系方式
     */
    private String consignorLinkerPhone;

    /**
     * 报价人
     */
    private String createUserName;

    //报价人ID
    private String createUserId;

    /**
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

//    /**
//     * 检测周期（0:无（单次） 1:周 2：月 3：季度 4：半年 5：年）
//     */
//    private String checkCircle;
//
//
//    //检测任务次数
//    private Integer checkTaskCount;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditUserId;

    private String auditUser;


    //备注
    private String remark;

    //说明
    private String explains;


}
