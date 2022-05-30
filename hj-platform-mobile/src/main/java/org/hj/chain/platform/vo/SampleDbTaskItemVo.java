package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/4/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleDbTaskItemVo extends JobCommonVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //待采集样品数
    private Integer toCollectSampleCount;
    //待确认样品数
    private Integer toConfirmSampleCount;
    //已确认样品数
    private Integer confirmedSampleCount;
    //待入库样品数
    private Integer toStockSampleCount;
    //已入库样品数
    private Integer stockedSampleCount;
    //待复核样品数
    private Integer toReviewSampleCount;
    //样品集合
    private List<SampleListVo> vos;
}
