package org.hj.chain.platform.tdo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子信息
 * @Iteration : 1.0
 * @Date : 2021/5/16  7:06 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/16    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorItemTdo implements Serializable {
    private static final long serialVersionUID = 98635562032601508L;

    /**
     * 因子检测ID(平台ID)
     */
    private String checkStandardId;

    //二级分类
    private String secdClassId;

    //因子名称
    private String factorName;
}