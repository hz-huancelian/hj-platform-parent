package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
public class ContTRAuditTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    @NotBlank(message = "报价单ID不能为空！")
    private String offerId;
    @NotBlank(message = "评审标识不能为空！")
    //审核标志：1-通过；2-驳回
    private String auditFlag;
    //备注
    private String remark;
}
