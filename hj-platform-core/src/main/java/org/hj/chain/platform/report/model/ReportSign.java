package org.hj.chain.platform.report.model;

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
 * @description TODO 报告签发
 * @Iteration : 1.0
 * @Date : 2021/5/23  6:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_report_sign")
public class ReportSign implements Serializable {
    private static final long serialVersionUID = 5171983298945975447L;

    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //报告主键ID
    private Long reportId;

    //签发状态
    private String signStatus;

    //机构ID
    private String organId;

    //签订时间
    private LocalDateTime signTime;

    //签订用户
    private String signUserId;

    //驳回原因
    private String rjctReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}