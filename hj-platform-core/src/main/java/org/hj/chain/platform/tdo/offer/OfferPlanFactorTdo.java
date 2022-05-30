package org.hj.chain.platform.tdo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * TODO 监测计划因子项
 *
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2022/3/7 11:10 下午
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferPlanFactorTdo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 因子检测标准ID
     */
    private String checkStandardId;

    //是否是因子（0-是；1-否（同系套餐时，是1，且 subCheckStandardIds 不为空））
    private String isFactor;

    //因子名称
    private String factorName;


    //二级分类
    private String secdClassId;

    /**
     * 频次/天
     */
    private Integer frequency;

    /**
     * 天数
     */
    private Integer dayCount;

    /**
     * 因子分组key
     */
    private String factorGroupKey;

    /**
     * 单次费用 （总价（单价*总次数） 总次数=频次/天 * 天数）
     */
    private BigDecimal costPerTime;

    /**
     * 检测位置
     */
    private String factorPoint;
    /**
     * 动态扩展参数：日均值、风向等
     */
    private String dynamicParam;

    //备注
    private String remark;


    //当选择为同系套餐时，该集合为子因子集合
    private List<String> subCheckStandardIds;


}
