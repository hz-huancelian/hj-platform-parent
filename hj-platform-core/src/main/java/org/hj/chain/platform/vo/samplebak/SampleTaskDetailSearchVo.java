package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
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
public class SampleTaskDetailSearchVo implements Serializable {
    private static final long serialVersionUID = 3845353614442144308L;
    //采样任务ID
    @NotNull(message = "采样任务ID不能为空！")
    private Long sampTaskId;
    //样品编号
    private String sampItemId;
    //样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampStatus;
}
