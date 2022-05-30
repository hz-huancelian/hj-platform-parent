package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子检测方法Vo
 * @Iteration : 1.0
 * @Date : 2021/5/5  9:52 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorMethodInfoVo implements Serializable {
    private static final long serialVersionUID = 3912186381158352636L;

    //检测方法ID
    private String id;

    //因子名称
    private String factorName;

    //所属一级分类
    private String classId;

    //一级分类名称
    private String className;

    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;

    //检出限（能检测出的最小颗粒度）
    private String detectionLimit;

    //检测标准名称
    private String standardName;

    //标准号
    private String standardNo;

    //0-国内；1-国外
    private String standardSource;

    //标准分析方法大类
    private String analysisMethodCgy;

    //标准分析方法
    private String analysisMethod;

    //默认检测数据单位ID
    private String defaultUnitId;

    //默认检测数据单位名称
    private String defaultUnitName;

    //检测数据可用单位分组id
    private String unitGroupId;

    //分组下单位集合
    private List<UnitInfoVo> unitVos;

    //0-现行；1-自定义标准 2试行；3暂行；4废止
    private String status;
}