package org.hj.chain.platform.contract.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分包合同
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_subcontract_info")
public class SubcontractInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 合同编号
     */
    private String contCode;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 机构ID
     */
    private String organId;


    //分包机构ID
    private String judgeOrganId;


    /**
     * 合同状态：状态（0：待制作 1:已制作）
     */
    private String contStatus;


    /**
     * 签定时间
     */
    private String signDate;

    /**
     * 有效期（年）
     */
    private String validity;

    /**
     * 合同文件ID
     */
    private String contFileId;

    /**
     * 合同制作人
     */
    private String makeUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
