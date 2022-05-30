package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 合同完善信息
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContractSaveTdo implements Serializable {
    private static final long serialVersionUID = -7547321580149087734L;
    @NotNull(message = "合同ID不能为空！")
    private Long contId;
    @NotNull(message = "合同基本信息不能为空！")
    private ContractInfoTdo contractInfoTdo;
    //甲方ID：为空为新增；不为空为更新
    private Long cusContBaseInfoId;
    @NotNull(message = "甲方信息不能为空！")
    private CusContBaseAddTdo addTdo;
}
