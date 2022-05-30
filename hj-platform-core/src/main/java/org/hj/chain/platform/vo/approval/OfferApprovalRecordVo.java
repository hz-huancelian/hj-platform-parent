package org.hj.chain.platform.vo.approval;

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
public class OfferApprovalRecordVo implements Serializable {

    private static final long serialVersionUID = -3486528834901226689L;

    //是否通过
    private Integer isPass;

    //备注
    private String remark;

    //审批用户ID
    private String approvalUserName;

    private LocalDateTime createTime;
}