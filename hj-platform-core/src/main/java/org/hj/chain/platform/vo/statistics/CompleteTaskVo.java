package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CompleteTaskVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;

    /**
     * 任务名称 (采样任务、检测任务、报告编制任务)
     */
    private String name;
    /**
     * 总任务数
     */
    private Integer totalCnt;
    /**
     * 已完成任务数
     */
    private Integer completeCnt;
}
