package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/26  3:26 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/26    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorParam implements Serializable {
    private static final long serialVersionUID = -5675000560848888469L;

    //检测因子ID
    private String checkStandardId;

    //因子名称
    private String factorName;

    //因子单位
    private String unitVal;

    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;
}