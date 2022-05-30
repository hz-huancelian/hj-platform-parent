package org.hj.chain.platform.check.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 检测任务信息
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_check_task")
public class CheckTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //调度任务ID（任务编号）
    private String jobId;

    //机构ID
    private String organId;

    //检测任务状态：0-待分配；1-检测中；2-已完成
    private String taskStatus;

    //任务完成时间
    private LocalDateTime finshTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long deptId;

}
