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
 * 样品出库申请：可以申请多个样品
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_draw_apply")
public class SampleDrawApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 审批状态：0-待审核；1-审核通过；2-审核失败
     */
    private String approvalStatus;

    /**
     * 取样ID
     */
    private Long sampleItemId;

    /**
     * 申请人
     */
    private String applyUserId;

    /**
     * 审核人
     */
    private String approvalUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
