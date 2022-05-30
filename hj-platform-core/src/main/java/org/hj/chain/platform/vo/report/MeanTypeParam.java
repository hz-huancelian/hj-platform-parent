package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/26  8:22 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/26    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MeanTypeParam {

    //均值类型key
    private String meanType;

    //均值类型值
    private String meanName;
}