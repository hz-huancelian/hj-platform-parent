package org.hj.chain.platform.vo.record;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class RecordRenderData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 委托单位
     */
    private String consignorName;

    //检测人：最多保留两个
    private String checkEmps;

    //审核人
    private String reviewerEmp;

    //样品记录
    private List<RecordRenderItemData> itemDatas;


}