package org.hj.chain.platform.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 监测频次Vo
 * @Iteration : 1.0
 * @Date : 2022/3/17  7:39 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/17    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MonitorFreqVo implements Serializable {
    private static final long serialVersionUID = 2322845866286129391L;

    //描述
    private  String desc;

    //计算值
    private  BigDecimal calVal;

    //基值
    private  BigDecimal calBaseVal;

    //单位
    private  String unit;
}