package org.hj.chain.platform.vo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/25  5:27 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportCheckDetailVo implements Serializable {
    private static final long serialVersionUID = 2856205318440906289L;
    //检测因子详情ID
    private Long checkFactorId;
    //样品ID
    private String sampItemId;
    //样品编号
    private String sampleNo;
    //二级类别
    private String secdClassId;
    //检测因子ID
    private String checkStandardId;
    //因子名称
    private String factorName;
    //因子标志 0-因子；1-同系物套餐
    private String factorFlag;
    //动态扩展参数：日均值等
    private String dynamicParam;
    //因子检测结果
    private String checkRes;
    //子因子检测结果
//    private String checkSubRes;
    //检测人
    private String assignUserId;

    //采样员工
    private String collectUserId;

    //因子分组key
    private String factorGroupKey;

    //采集时间
//    private LocalDateTime collectTime;

    //采样日期
    private String collectDate;

    //检测设备
    private String checkEquipment;
}