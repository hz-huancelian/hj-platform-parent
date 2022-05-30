package org.hj.chain.platform.tdo.report;

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
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportInfoAuditTdo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    @NotNull
    private Long reportId;
    @NotBlank(message = "审核标识不能为空！")
    //审核标志：1-审核通过；2-审核不通过
    private String auditFlag;
    //审核失败原因
    private String auditReason;
}
