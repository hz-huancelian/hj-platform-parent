package org.hj.chain.platform.tdo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 检测因子修改实体
 * 有自检或者外泄支持的，两个能量值必须有一个为正
 * @Iteration : 1.0
 * @Date : 2021/5/5  11:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorCheckStandardModifyTdo implements Serializable {
    private static final long serialVersionUID = -990396875707961295L;

    //主键ID
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * CMA能力(0:无 1:有)
     */
    @NotEmpty(message = "CMA能力不能为空")
    private String cmaFlg;

    /**
     * CNAS能力(0:无 1:有)
     */
    @NotEmpty(message = "CNAS能力不能为空")
    private String cnasFlg;

    //价格
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    /**
     * 外部协助检测标识（0：自检 1：外协支持 2：无能力）
     */
//    @NotEmpty(message = "检测标识不能为空")
//    private String extAssistFlg;
}