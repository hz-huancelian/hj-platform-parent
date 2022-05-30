package org.hj.chain.platform.judge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 评审任务表：需要技术负责人处理；与合同签订是平行的，但是评审失败，整个合同失效
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_judge_task")
public class JudgeTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评审任务编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务状态：0-待处理；1-需要评审；2-不需要评审；3-作废；4-评审中；5-评审完成；6-评审通过
     */
    private String taskStatus;

    /**
     * 报价单号
     */
    private String offerId;

    /**
     * 评审判定人
     */
    private String operUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    //参与评审人数
    private Integer reviewNum;
    //已评审人数
    private Integer reviewedNum;
    /**
     * 备注
     */
    private String remark;


}
