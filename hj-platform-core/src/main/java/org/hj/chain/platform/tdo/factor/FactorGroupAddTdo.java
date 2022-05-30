package org.hj.chain.platform.tdo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子套餐新增实体
 * @Iteration : 1.0
 * @Date : 2021/5/6  11:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorGroupAddTdo implements Serializable {
    private static final long serialVersionUID = 6998672480853437618L;

    //套餐名称
    @NotBlank(message = "套餐名称不能为空")
    private String groupName;


    //认证类型 0-CMA;1-CNAS
    @NotBlank(message = "认证类型")
    private String authType;

    //套餐类型：0-自由因子；1-同系套餐

    @NotBlank(message = "套餐类型")
    private String groupType;

    //套餐价格：当为同系套餐时，价格不能为空
    private BigDecimal groupCost;


    @NotEmpty(message = "至少选择一个因子")
    private List<FactorItemTdo> factorStandardIds;

    //备注
    private String remark;
}
