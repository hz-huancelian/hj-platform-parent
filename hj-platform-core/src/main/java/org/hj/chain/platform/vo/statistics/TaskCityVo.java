package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TaskCityVo implements Serializable {
    private static final long serialVersionUID = -5304699473300983590L;
    /**
     * 城市
     */
    private String city;
    /**
     * 调度任务数
     */
    private Integer taskNum;
}
