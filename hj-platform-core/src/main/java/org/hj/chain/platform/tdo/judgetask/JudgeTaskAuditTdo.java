package org.hj.chain.platform.tdo.judgetask;

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
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeTaskAuditTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    @NotNull(message = "技术评审任务ID不能为空！")
    private Long judgeTaskId;
    @NotBlank(message = "评审标识不能为空！")
    //审核标志：1-需要评审；2-不需要评审；3-作废；6-评审通过
    private String auditFlag;
    //备注
    private String remark;
    //参与评审人员（用户ID逗号拼接，需要评审时不能为空！）
    private String auditUsers;
}
