package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 客户合同基本信息
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CusContBaseModifyTdo extends CusContBaseAddTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;

    //主键ID
    @NotNull(message = "客户合同基本信息ID不能为空")
    private Long cusContBaseInfoId;

}
