package org.hj.chain.platform.word;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  2:36 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HandoverTableData {

    //受检单位
    private String consignorName;

    //入库时间
    private String storeTime;


    //采样时间
    private String collectTime;

    //采样日期
    private String collectDate;

    //送样人
    private String sendUsers;

    //接样人
    private String recieveUsers;

    //样品列表
    private List<HandoverItemData> itemDataList;

}