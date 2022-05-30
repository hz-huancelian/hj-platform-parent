package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  7:19 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportVo implements Serializable {
    private static final long serialVersionUID = -1628222544669199548L;

    //主键ID
    private Long id;

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