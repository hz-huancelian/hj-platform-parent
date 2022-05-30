package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferDispatchStatisVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;
    /**
     * 已调度任务数
     */
    private Integer dipatchedCnt;
    /**
     * 待调度任务数
     */
    private Integer unDispatchCnt;

}
