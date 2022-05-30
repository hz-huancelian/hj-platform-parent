package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 采集任务表
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_task")
public class SampleTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调度任务ID
     */
    private String jobId;
    /**
     * 机构ID
     */
    private String organId;

    /**
     * 任务状态：0-待合样；1-已合样；2-采样中；3-已完成
     */
    private String taskStatus;

    /**
     * 合样时间
     */
    private LocalDateTime mergeTime;

    /**
     * 采集完成时间
     */
    private LocalDateTime finishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long deptId;

    /**
     * 采样任务负责人
     */
    private String managerUserId;

    //合样备注
    private String combinedRemark;


}
