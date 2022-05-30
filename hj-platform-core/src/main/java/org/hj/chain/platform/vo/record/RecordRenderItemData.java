package org.hj.chain.platform.vo.record;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 采样数据渲染
 * @Iteration : 1.0
 * @Date : 2021/8/18  10:48 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/18    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecordRenderItemData implements Serializable {
    private static final long serialVersionUID = 1L;

    //样品编号
    private String sampItemId;

    //检测点位
    private String factorPoint;

    //采样时间
    private LocalDateTime collectTime;

    //采样日期
    private String collectDate;

    //采样备注
    private String collectRemark;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;

    //分组key
    private String groupKey;

    //污染物集合
    private List<Pollutant> pollist;


    //属性map
    private Map<String, Object> dataMap;

    //现场因子
    private Map<String, SampleFactorDataParam> factorMap;

    //分析项目
    private String factorItems;


}