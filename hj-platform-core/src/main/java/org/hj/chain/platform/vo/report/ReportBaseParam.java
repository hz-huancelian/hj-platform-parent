package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告基础信息
 * @Iteration : 1.0
 * @Date : 2022/4/14  9:16 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportBaseParam implements Serializable {
    private static final long serialVersionUID = 4789521818368020559L;

    //调度ID
    private String jobId;

    //功能名称
    private String projectName;

    //报价单ID
    private String offerId;

    //报告主键ID
    private Long reportId;
}