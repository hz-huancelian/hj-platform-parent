package org.hj.chain.platform.offer.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("t_offer_info")
public class OfferInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //报价单编号
    @TableId(type = IdType.INPUT)
    private String id;

    //机构ID
    private String organId;

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
     * 报价单状态（0：草稿 1：待审核 2：审核未通过 3：审核通过 4：作废）
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
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

//    /**
//     * 检测周期（0:无（单次） 1:周 2：月 3：季度 4：半年 5：年）
//     */
//    private String checkCircle;
//
//
//    //检测任务数
    private Integer checkTaskCount;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人
     */
    private String auditUserId;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 提交人
     */
    private String submitUserId;

    /**
     * 删除状态：0-未删除；1-删除
     */
    private String delStatus;

    private LocalDateTime createTime;

    private String createUserId;

    //部门ID
    private Long deptId;

    //备注
    private String remark;


    //说明
    private String explains;

    private LocalDateTime updateTime;


}
