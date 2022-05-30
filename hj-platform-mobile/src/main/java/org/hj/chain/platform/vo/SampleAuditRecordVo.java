package org.hj.chain.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Date : 2022/3/17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleAuditRecordVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;

    private String auditUser;
    private String auditReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime auditTime;
    //审核状态：1-通过；2-不通过
    private String auditStatus;
    //0-复核；1-确认；2-审核
    private String auditType;
}
