package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleClassificationVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;

    /**
     * 样品一级类别
     */
    private String classId;

    /**
     * 计数
     */
    private Integer cnt;
}
