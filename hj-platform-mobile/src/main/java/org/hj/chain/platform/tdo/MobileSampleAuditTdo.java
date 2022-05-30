package org.hj.chain.platform.tdo;

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
 * @Date : 2021-05-08
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleAuditTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotBlank(message = "样品ID不能为空！")
    //样品ID（批量以"，"拼接，单个以"，"结尾）
    private String sampItemId;
    @NotBlank(message = "审核标识不能为空！")
    //审核标志：1-审核通过；2-审核不通过
    private String auditFlag;
    //审核失败原因
    private String auditReason;
}
