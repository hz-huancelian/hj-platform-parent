package org.hj.chain.platform.check.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("t_check_report")
public class CheckReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报告编号
     */
    private String id;

    /**
     * 报告状态（-1：生成中 0：待生成 1：已生成（未提交审核） 2：待审核 3：审核未通过 4：待签发 5：已签发 6：已重新上传
     */
    private String reportStatus;

    /**
     * 报采单ID
     */
    private String offerId;

    /**
     * 报告类型（0：初始报告 1：编辑报告 2：签发报告（有水印的PDF文档））
     */
    private String reportType;

    /**
     * 报告文件id（资源文件信息表id）
     */
    private String reportFileId;

    private String genUserId;

    /**
     * 生成时间
     */
    private LocalDateTime genTime;

    /**
     * 签发用户ID
     */
    private String signUserId;

    /**
     * 签发时间
     */
    private LocalDateTime signTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
