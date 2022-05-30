package org.hj.chain.platform.vo.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleJobSearchVo implements Serializable {
    private static final long serialVersionUID = -3245353614442144308L;
    private Long taskId;
    //任务单号
    private String jobId;
    //项目名称
    private String projectName;
}
