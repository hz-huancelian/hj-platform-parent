package org.hj.chain.platform.vo.samplebak;

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
 * @Date : 2021/5/24  10:44 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/24    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleDataParam implements Serializable {
    private static final long serialVersionUID = -6795991452413432796L;

    //样品编号
    private String sampItemId;

    //采样数据-因子 json存储
    private String sampleDetails;
    /**
     * 采样数据-二级类别 json存储
     */
    private String sampleData;

    //采样设备
    private String sampEquipment;

    //检测点位
    private String factorPoint;

    //采样依据
    private String sampBasis;

    //采样时间
    private String collectTime;

    //采样日期
    private String collectDate;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;

    //采样备注
    private String collectRemark;

    private Integer sampleNum;

    //分组key
    private String groupKey;

    //采样人
    private String collectUserId;

    //复核人
    private String reviewUserId;

}