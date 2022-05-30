package org.hj.chain.platform.vo.approval;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/8  11:48 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/08    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContractAuditRecordVo implements Serializable {
    private static final long serialVersionUID = 1931680020857952583L;
    //审核标志：1-通过；2-驳回
    private String auditFlag;
    //驳回原因
    private String auditReason;

    private String auditUserName;

    private LocalDateTime createTime;

}