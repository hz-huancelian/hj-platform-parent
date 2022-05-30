package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告审核Vo
 * @Iteration : 1.0
 * @Date : 2022/4/12  11:11 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/12    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportCheckVo implements Serializable {
    private static final long serialVersionUID = -8002688773139696557L;

    //主键ID
    private Long id;

    private Long reportId;

    //报告编号
    private String reportCode;

    //任务单号
    private String jobId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 委托单位
     */
    private String consignorName;

    //受检单位
    private String inspectionName;

    //提交审核时间
    private String commitCheckTime;

    //报告状态
    private String reportStatus;

    //报告制作人
    private String reportMakeUserId;

    private String reportMakeUser;

    //报告制作时间
    private LocalDateTime reportMakeTime;
    //报告文件ID
    private String reportFileId;
}