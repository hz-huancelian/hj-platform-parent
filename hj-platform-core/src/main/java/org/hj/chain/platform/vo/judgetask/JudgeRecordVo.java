package org.hj.chain.platform.vo.judgetask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeRecordVo implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * 技术评审记录ID
     */
    private Long judgeRecordId;
    /**
     * 技术评审任务ID
     */
    private Long judgeTaskId;

    /**
     * 技术评审任务状态：0-待处理；1-通过；2-驳回
     */
    private String judgeRecordStatus;

    /**
     * 评审时间
     */
    private LocalDateTime judgeTime;

    /**
     * 技术评审人
     */
    private String judgeUser;

    /**
     * 备注
     */
    private String remark;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 认证类型（1：CMA 2：CNAS
     */
    private String certificationType;

    /**
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;

    /**
     * 检测任务数
     */
    private Integer checkTaskCount;

    /**
     * 委托单位
     */
    private String consignorName;
}
