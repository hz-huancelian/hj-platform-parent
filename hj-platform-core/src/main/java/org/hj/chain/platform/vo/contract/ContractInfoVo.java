package org.hj.chain.platform.vo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContractInfoVo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;

    /**
     * 合同ID
     */
    private Long id;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 合同编号
     */
    private String contCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 初始制作方式（0：在线制作 1：本地上传）
     */
    private String makeType;

    /**
     * 甲方
     */
    private String partA;

    //委托机构(当甲方为空时，默认是委托单位)
    private String consignorName;


    /**
     * 合同状态：状态（0：待制作 1:已制作，2:待审核；3审核通过；4:审核失败5：已作废）
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
     * 合同制作人
     */
    private String makeUserId;

    /**
     * 合同文件ID
     */
    private String contFileId;

    /**
     * 合同驳回原因
     */
    private String auditReason;

    /**
     * 主合同编号
     */
    private String supContCode;

}
