package org.hj.chain.platform.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单审批记录
 * @Iteration : 1.0
 * @Date : 2021/7/8  10:58 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/08    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_offer_approval_record")
public class OfferApprovalRecord implements Serializable {

    private static final long serialVersionUID = -3486528834901226689L;
    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //报价单号
    private String offerId;

    //审批状态
    private Integer isPass;

    //备注
    private String remark;

    //审批用户ID
    private String approvalUserId;

    private LocalDateTime createTime;
}