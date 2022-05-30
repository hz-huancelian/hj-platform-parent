package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告检测设备信息
 * @Iteration : 1.0
 * @Date : 2022/5/17  3:25 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/17    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportEquipVo implements Serializable {
    private static final long serialVersionUID = -4452615122940682977L;

    //检测因子标准号
    private String checkStandardId;

    //是否是同系套餐：0-非同系；1-同系
    private String factorFlag;

    //因子名称
    private String factorName;

    //分析方法
    private String analysisMethod;

    //仪器设备及编号
    private String equipModeAndCode;

    //检出限
    private String detectionLimit;
}