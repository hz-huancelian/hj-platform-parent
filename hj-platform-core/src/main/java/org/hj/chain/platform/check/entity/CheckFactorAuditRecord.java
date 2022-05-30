package org.hj.chain.platform.check.entity;

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
 * @Description : TODO 检测列表审核记录表
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_check_factor_audit_record")
public class CheckFactorAuditRecord implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkFactorId;
    private String auditUserId;
    private String auditReason;
    private LocalDateTime auditTime;
    private String auditStatus;

}
