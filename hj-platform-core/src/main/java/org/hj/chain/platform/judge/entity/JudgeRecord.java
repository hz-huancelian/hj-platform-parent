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
 *
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_judge_record")
public class JudgeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评分任务记录
     */
    private Long judgeTaskId;

    private String judgeUserId;

    /**
     * 评审状态：0-待处理；1-通过；2-驳回
     */
    private String judgeStatus;


    private String remark;
    /**
     * 评审时间
     */
    private LocalDateTime judgeTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
