package org.hj.chain.platform.vo.approval;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 分包判断Vo
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfferJudgeApprovalSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //报价单号
    private String offerId;

    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 分包状态（0-未处理；1-已处理）
     */
    private String status;


}
