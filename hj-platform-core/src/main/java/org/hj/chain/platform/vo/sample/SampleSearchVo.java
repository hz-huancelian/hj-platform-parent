package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleSearchVo extends SampCommSearchVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //样品编号
    private String sampleNo;
    //样品状态 6-已入库;7-部分出库；8-已出库
    private String sampStatus;
    //入库日期
    private String date;
}
