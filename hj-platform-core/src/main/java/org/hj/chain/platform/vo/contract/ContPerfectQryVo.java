package org.hj.chain.platform.vo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 合同完善信息查看
 * @Iteration : 1.0
 * @Date : 2021/5/15  5:11 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/15    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContPerfectQryVo implements Serializable {
    private static final long serialVersionUID = 2060380024261816725L;

    //合同编号
    private String contCode;
    /**
     * 项目名称
     */
    private String projectName;

    //委托人
    private String consignorName;

    //总花费
    private BigDecimal totalCost;

    //合同控制号
    private String contControlId;

    /**
     * 签定时间
     */
    private String signDate;

    /**
     * 签定地点
     */
    private String signLocation;

    /**
     * 支付方式（1：一次支付 2：分期支付 3：其他方式）
     */
    private String payMethod;

    /**
     * 支付详情描述
     */
    private String payMethodDesc;

    /**
     * 有效期（年）
     */
    private String validity;

    /**
     * 甲方信息ID
     */
    private Long cusContBaseInfoId;


    //乙方信息
    private OwnerContBaseInfoVo ownerContBaseInfoVo;

    //甲方
    private CusContBaseInfoVo cusContBaseInfoVo;

    /**
     * 主合同编号
     */
    private String supContCode;
}