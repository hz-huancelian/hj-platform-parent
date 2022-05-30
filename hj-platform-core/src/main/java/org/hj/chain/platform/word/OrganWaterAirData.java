package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/14  4:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrganWaterAirData {

    private String factorPoint;

    private String collectDate;


    //烟道截面积
    private String sectionalArea;
    //排气筒高度
    private String exhaustHeight;

    //含氧量
    private String oxygenContent;
    private String oxygenContent2;
    private String oxygenContent3;

    //烟气温度
    private String gasTemperature;
    private String gasTemperature2;
    private String gasTemperature3;

    //烟气含湿量
    private String flueGas;
    private String flueGas2;
    private String flueGas3;

    //烟气流速
    private String gasVelocity;
    private String gasVelocity2;
    private String gasVelocity3;

    //标态烟气流量
    private String smokeFlowRate;
    private String smokeFlowRate2;
    private String smokeFlowRate3;

    //折算系数
    private String conversionFactor;
    private String conversionFactor2;
    private String conversionFactor3;


    @Name("rep_table")
    private OrganWaterAirItemData itemData;
}