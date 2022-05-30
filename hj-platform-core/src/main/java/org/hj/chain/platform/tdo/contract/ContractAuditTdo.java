package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
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
public class ContractAuditTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    @NotNull(message = "合同ID不能为空！")
    private Long contId;
    @NotBlank(message = "审核标识不能为空！")
    //审核标志：1-通过；2-驳回
    private String auditFlag;
    //驳回原因
    private String auditReason;
}
