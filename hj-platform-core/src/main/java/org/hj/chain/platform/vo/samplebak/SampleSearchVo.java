package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleSearchVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //样品编号
    private String sampItemId;
    //任务单号
    private String dispatchInfoId;
    //样品状态 6-已入库;7-部分出库；8-已出库
    private String sampStatus;
    //入库日期
    private String date;
}
