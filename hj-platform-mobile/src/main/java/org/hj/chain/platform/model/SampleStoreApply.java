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
 * 样品入库申请
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_store_apply")
public class SampleStoreApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 样品ID
     */
    private Long sampleItemId;

    /**
     * 审批状态：0-待审核；1-审核通过；2-审核失败
     */
    private String approvalStatus;

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
