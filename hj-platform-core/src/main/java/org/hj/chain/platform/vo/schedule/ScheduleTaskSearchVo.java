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
public class ScheduleTaskSearchVo implements Serializable {
    private static final long serialVersionUID = -3245353614442144308L;
    //合同编号
    private String contCode;
    //项目名称
    private String projectName;
    //调度状态 0-待调度；1-调度中；2-调度完成
    private String scheduleStatus;

}
