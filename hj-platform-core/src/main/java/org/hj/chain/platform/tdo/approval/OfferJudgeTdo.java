package org.hj.chain.platform.tdo.approval;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
public class OfferJudgeTdo implements Serializable {
    private static final long serialVersionUID = 7533485422192819806L;

    //报价单号
    @NotBlank(message = "报价单号不能为空")
    private String offerId;

    //机构因子关系
    @NotEmpty(message = "分包因子不能为空")
    private List<OfferJudgeItemTdo> itemTdoList;
}