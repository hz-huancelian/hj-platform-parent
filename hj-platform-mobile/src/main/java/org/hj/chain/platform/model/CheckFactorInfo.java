package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_check_factor_info")
public class CheckFactorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 检测任务ID
     */
    private Long checkTaskId;

    /**
     * 检测结果
     */
    private String checkRes;

    /**
     * 检测状态：0-待领样；1-领样申请；2-待检测；3-已录入；4-待审核；5-审核通过；6-审核失败
     */
    private String checkStatus;

    /**
     * 样品ID
     */
    private Long sampItemId;

    /**
     * 采集因子信息ID
     */
    private Long offerFactorId;

    /**
     * 检测开始日期
     */
    private String checkStartDate;

    /**
     * 检测结束日期
     */
    private String checkEndDate;

    /**
     * 分配的检测员
     */
    private String assignUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 数据录入环节（1：采样 2：检样）
     */
    private String dataEntryStep;


}
