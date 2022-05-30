package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 样品数据参数
 * @Iteration : 1.0
 * @Date : 2022/4/7  5:52 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleFactorDataParam implements Serializable {
    private static final long serialVersionUID = -6795991452413432796L;

    //样品编号
    private String sampleNo;

    //二级类别
    private String secdClassId;

    //监测因子号
    private String checkStandardId;
    //因子名称
    private String factorName;
    //是否是同系物
    private String isFactor;
    //因子数据
    private String factorData;

    //现场检测设备列表
    private String checkEquipment;
    //校准设备列表
    private String calibrationEquipment;
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
}