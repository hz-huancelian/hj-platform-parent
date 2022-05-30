package org.hj.chain.platform.vo.record;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 固体污染物
 * @Iteration : 1.0
 * @Date : 2021/8/20  12:19 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/20    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Pollutant implements Serializable {
    private static final long serialVersionUID = -2041987131796968552L;
    //样品编号
    private String sampItemId;
    //污染物名称
    private String pollutant;
    //采气时段
    private String gasProduction;
    //采气流体
    private String gasFlow;
    //采气体积
    private String gasVolume;
    //标态采气体积
    private String standardVolume;
    //计温
    private String thermometers;
    //计压
    private String pressureGauge;
}