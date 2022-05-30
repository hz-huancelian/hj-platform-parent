package org.hj.chain.platform.sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
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
