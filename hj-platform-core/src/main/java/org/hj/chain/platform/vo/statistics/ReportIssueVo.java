package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportIssueVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;
    /**
     * 已签发
     */
    private Integer IssuedCnt;

    /**
     * 总报告数（不包含“驳回”的）
     */
    private Integer totalIssuedCnt;
}
