package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfferPlanFactorVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //监测计划因子ID
    private Long planFactorId;
    //报价单编号
    private String offerId;
    //监测计划ID
    private Long offerPlanId;
    //因子检测标准ID
    private String checkStandardId;
    //因子标志：0-因子；1-同系物
    private String isFactor;
    //检测因子
    private String factorName;
    //检测方法
    private String standardName;
    //一级分类ID
    private String classId;
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
    //单次费用 （总价（单价*总次数） 总次数=频次/天 * 天数）
    private BigDecimal costPerTime;
    //检测位置
    private String factorPoint;
    //动态扩展参数：日均值、风向等
    private String dynamicParam;
    //分包标志 0-自检；1-分包
    private String fbFlag;
    //因子点位分组key
    private String factorGroupKey;
    //因子备注
    private String remark;
    //因子所属机构(判断分包)
    private String organId;
    //机构名称
    private String organName;
    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;
    //标准号
    private String standardNo;
}
