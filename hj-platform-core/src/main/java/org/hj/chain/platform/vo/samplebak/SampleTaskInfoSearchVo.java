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
public class SampleTaskInfoSearchVo implements Serializable {
    private static final long serialVersionUID = 3845353614442144308L;
    //任务单号
    private String dispatchInfoId;
    //采样任务状态 0-待分配；1-采样中；2-已完成
    private String sampTaskStatus;
    //项目名称
    private String projectName;
}
