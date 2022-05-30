package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SampleDataParam implements Serializable {
    private static final long serialVersionUID = -6795991452413432796L;

    /**
     * 样品编号
     */
    private String sampleNo;


    /**
     * 样品录样位置(详细地址)
     */
    private String collectLocation;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;

    //采样时间
    private LocalDateTime collectTime;

    //采样日期
    private String collectDate;
}