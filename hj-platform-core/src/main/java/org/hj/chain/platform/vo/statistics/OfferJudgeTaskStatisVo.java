package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferJudgeTaskStatisVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;
    /**
     * 总任务数
     */
    private Integer totalCnt;
    /**
     * 已完成任务数
     */
    private Integer finishCnt;
    /**
     * 未完成任务数
     */
    private Integer unFinishCnt;

}
