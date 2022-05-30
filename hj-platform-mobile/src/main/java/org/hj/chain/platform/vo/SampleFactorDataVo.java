package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleFactorDataVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    private Long id;
    //监测因子
    private String factorName;
    //因子编号
    private String checkStandardId;
    //因子数据
    private String factorData;
    //现场检测设备列表
    private String checkEquipment;
    //校准设备列表
    private String calibrationEquipment;
    //数据录入环节（1：现场 2：实验室）
    private String dataEntryStep;
    //因子单位
    private String unitName;
    //因子数据结果集
    private String factorDataValue;

    //标准缓冲液I定位值
    private String positioningOne;
    //标准缓冲液II定位值
    private String positioningTwo;
    //标准缓冲液II理论值
    private String positioningThree;
    // 理论值
    private String theoreticalVal;
    // 测定值
    private String groundConditions;

    //因子计算结果集
    private String measuredFormVal;
}
