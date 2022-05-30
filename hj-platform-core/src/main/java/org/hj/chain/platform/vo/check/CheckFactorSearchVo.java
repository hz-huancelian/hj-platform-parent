package org.hj.chain.platform.vo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class CheckFactorSearchVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //检测任务ID
    private Long checkTaskId;
    //样品编号
    private String sampleNo;
    //因子名称
    private String factorName;
    //检测状态0-待领样；1-领样申请；2-待检测；3-已录入；4-待审核；5-审核通过；6-审核失败；
    private String checkStatus;
}
