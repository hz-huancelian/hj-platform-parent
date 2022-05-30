package org.hj.chain.platform.tdo.approval;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单分包信息
 * @Iteration : 1.0
 * @Date : 2021/5/11  11:57 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/11    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferJudgeItemTdo implements Serializable {
    private static final long serialVersionUID = 7533485422192819806L;

    /**
     * 报价单因子ID
     */

    private Long planFactorId;

    /**
     * 组织机构ID
     */
    private String organId;
}