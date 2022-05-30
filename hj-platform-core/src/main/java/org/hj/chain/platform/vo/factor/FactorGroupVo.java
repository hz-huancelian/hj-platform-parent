package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子套餐展示Vo
 * @Iteration : 1.0
 * @Date : 2021/5/7  8:43 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorGroupVo implements Serializable {
    private static final long serialVersionUID = -679644454617696479L;

    //套餐ID
    private Long groupId;

    //套餐类型：0-自由因子；1-同系套餐
    private String groupType;

    //套餐名称
    private String groupName;

    //套餐认证类型0-CMA;1-CNAS
    private String authType;

    //套餐说明（因子名称“，”拼接）
    private String groupDesc;

    //因子个数
    private Integer factorNum;

    //套餐价格
    private BigDecimal groupCost;

    //备注
    private String remark;

    //创建时间
    private LocalDateTime createTime;
}