package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

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
public class ContractInfoTdo implements Serializable {
    private static final long serialVersionUID = -7547321580149087734L;
    /**
     * 签定时间
     */
    @NotBlank(message = "签定时间不能为空！")
    private String signDate;

    /**
     * 签定地点
     */
    @NotBlank(message = "签定地点不能为空！")
    private String signLocation;


    /**
     * 支付方式（1：一次支付 2：分期支付 3：其他方式）
     */
    @NotBlank(message = "支付方式不能为空！")
    private String payMethod;

    /**
     * 支付详情描述
     */
    private String payMethodDesc;

    /**
     * 有效期（年）
     */
    @NotBlank(message = "有效期不能为空！")
    private String validity;


    /**
     * 主合同编号
     */
    private String supContCode;
}
