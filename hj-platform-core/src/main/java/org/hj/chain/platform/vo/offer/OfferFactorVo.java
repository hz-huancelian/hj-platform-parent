package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 检测因子项
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfferFactorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //报价单因子ID
    private Long offerFactorId;

    //因子检测标准ID
    private String checkStandardId;

    //检测因子
    private String factorName;

    //检测方法
    private String standardName;

    //一级类别
    private String className;

    //二级分类ID
    private String secdClassId;

    //二级分类
    private String secdClassName;

    //频次/天
    private Integer frequency;

    //天数
    private Integer dayCount;

    //单次价格
    private BigDecimal price;

    //总价（单价*总次数） 总次数=频次/天 * 天数
    private BigDecimal totalCost;

    //检测位置
    private String factorPoint;

    //动态扩展参数：日均值、风向等
    private String dynamicParam;

    //备注
    private String remark;

    //机构ID
    private String organId;

    //机构名称
    private String organName;

    //分包标志 0-自检；1-分包
    private String fbFlag;

    //默认检测数据单位名称
    private String defaultUnitName;

    /**
     * 因子分组key
     */
    private String factorGroupKey;

}
