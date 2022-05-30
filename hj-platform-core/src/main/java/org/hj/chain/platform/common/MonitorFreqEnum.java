package org.hj.chain.platform.common;

import java.math.BigDecimal;

/**
 * TODO 监测频次枚举类型
 *
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2022/3/8 3:21 下午
 */
public enum MonitorFreqEnum {
    SINGLE("单次", new BigDecimal(1), new BigDecimal(1), "none"),
    ONECE_MONTH("1次/每月", new BigDecimal(1), new BigDecimal(1), "月"),
    ONECE_QUARTERLY("1次/每季", new BigDecimal(1), new BigDecimal(1), "季度"),
    ONECE_YEAR("1次/每年", new BigDecimal(1), new BigDecimal(1), "年"),
    ONECE_HALF_YEAR("1次/半年", new BigDecimal(1), new BigDecimal(0.5), "年"),
    ONECE_WEEK("1次/周", new BigDecimal(1), new BigDecimal(1), "周"),
    ONECE_DAY("1次/日", new BigDecimal(1), new BigDecimal(1), "日"),
    ONECE_12HOUR("1次/12小时", new BigDecimal(1), new BigDecimal(12), "小时"),
    HALF_3YEAR("3次/半年", new BigDecimal(3), new BigDecimal(0.5), "年"),
    ONECE_2YEAR("1次/两年", new BigDecimal(1), new BigDecimal(2), "年"),
    NONE("无固定频次", BigDecimal.ONE, BigDecimal.ONE, "none"),
    ;


    //描述
    private final String desc;

    //计算值
    private final BigDecimal calVal;

    //基值
    private final BigDecimal calBaseVal;

    //单位
    private final String unit;


    MonitorFreqEnum(String desc, BigDecimal calVal, BigDecimal calBaseVal, String unit) {
        this.desc = desc;
        this.calVal = calVal;
        this.calBaseVal = calBaseVal;
        this.unit = unit;
    }


    public String getDesc() {
        return desc;
    }

    public BigDecimal getCalVal() {
        return calVal;
    }

    public BigDecimal getCalBaseVal() {
        return calBaseVal;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "MonitorFreqEnum{" +
                "desc='" + desc + '\'' +
                ", calVal=" + calVal +
                ", calBaseVal=" + calBaseVal +
                ", unit='" + unit + '\'' +
                '}';
    }
}
