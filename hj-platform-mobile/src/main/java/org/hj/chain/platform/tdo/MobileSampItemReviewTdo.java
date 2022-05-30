package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampItemReviewTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotNull(message = "样品ID不能为空！")
    private Long sampItemId;
    @NotBlank(message = "复核标识不能为空！")
    //复核标志：1-复核通过；2-复核不通过
    private String auditFlag;
    //复核不通过原因
    private String auditReason;
}
