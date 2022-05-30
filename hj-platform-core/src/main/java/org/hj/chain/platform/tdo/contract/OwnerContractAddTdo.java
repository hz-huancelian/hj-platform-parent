package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
public class OwnerContractAddTdo implements Serializable {

    private static final long serialVersionUID = 1L;


    //合同控制号
//    @NotBlank(message = "合同控制号不能为空")
    private String contControlId;

    /**
     * 机构名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String organName;

    /**
     * 通讯地址
     */
    @NotBlank(message = "通讯地址不能为空")
    private String address;

    /**
     * 邮编
     */
    @NotBlank(message = "邮编不能为空")
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
    @NotBlank(message = "电话/传真不能为空")
    private String telFax;

    /**
     * 开户行
     */
    @NotBlank(message = "开户行不能为空")
    private String bankName;

    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    private String bankNo;

    /**
     * 税号
     */
    @NotBlank(message = "税号不能为空")
    private String taxNumber;


}
