package org.hj.chain.platform.vo.judgetask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 技术评审任务ID
     */
    private Long judgeTaskId;

    /**
     * 技术评审任务状态：0-待处理；1-需要评审；2-不需要评审；3-作废；4-评审中；5-评审完成；6-评审通过
     */
    private String judgeTaskStatus;

    /**
     * 参与评审人数
     */
    private Integer reviewNum;

    /**
     * 已评审人数
     */
    private Integer reviewedNum;

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
     * 委托单位
     */
    private String consignorName;

    /**
     * 计划数
     */
    private Integer planNum;

}
