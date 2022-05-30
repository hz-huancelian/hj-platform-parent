package org.hj.chain.platform.word;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:15 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HandoverItemData {

    //检测项目
    private String factorNames;

    //样品编号
    private String sampItemId;


    //检测位置（点位）
    private String collectLocation;

    //样品数量
    private String sampleCount;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;
}