package org.hj.chain.platform.vo.check;

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
public class CheckTaskInfoSearchVo implements Serializable {
    private static final long serialVersionUID = 3845353614442144308L;
    //任务单号
    private String jobId;
    //检测任务状态0-待分配；1-检测中；2-已完成
    private String checkTaskStatus;
    //项目名称
    private String projectName;
}
