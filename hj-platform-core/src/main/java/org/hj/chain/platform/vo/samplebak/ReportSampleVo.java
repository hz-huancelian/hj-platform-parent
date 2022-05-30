package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告现场采样记录信息
 * @Iteration : 1.0
 * @Date : 2021/5/25  5:13 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportSampleVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;

    //样品ID
    private String sampItemId;

    //二级类别
    private String secdClassId;

    //检测点位
    private String factorPoint;

    //业务分组key（技术字段）
    private String groupKey;

    //业务分组原始key（技术字段）
    private String originGroupKey;

    //因子分组key
    private String factorGroupKey;

    //频次
    private Integer frequency;


    //样品类别 0-自检；1-外包
    private String fbFlag;

    //性状
    private String sampleProperties;

    /**
     * 样品采集开始时间
     */
    private String sampleStartDate;

    /**
     * 样品采集结束时间
     */
    private String sampleEndDate;

    //采集时间
    private LocalDateTime collectTime;

    //采集日期
    private String collectDate;

    /**
     * 采样数据-二级类别 json存储
     */
    private String sampleData;

    //采样员
    private String collectUserId;
}