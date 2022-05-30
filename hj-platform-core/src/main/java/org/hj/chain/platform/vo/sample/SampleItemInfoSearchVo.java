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
 * @Date : 2022/3/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleItemInfoSearchVo extends SampCommSearchVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //采样任务ID
    private Long sampTaskId;
    //样品编号
    private String sampleNo;
    //样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库;7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampStatus;
}
