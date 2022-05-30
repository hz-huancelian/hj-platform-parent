package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 客户合同基本信息修改
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
public class CusContBaseAddTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;

    /**
     * 单位名称
     */
    @NotBlank(message = "单位名称不能为空")
    private String companyName;

    /**
     * 通讯地址
     */
    @NotBlank(message = "通讯地址不能为空")
    private String address;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 法人
     */
    @NotBlank(message = "法人不能为空")
    private String jurPerson;

    /**
     * 委托代理人
     */
    @NotBlank(message = "委托代理人不能为空")
    private String agentPerson;

    /**
     * 电话/传真
     */
    private String telFax;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 税号
     */
    private String taxNumber;
}
