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
public class CusContBaseInfoVo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    private Long id;

    /**
     * 机构ID
     */
    private String organId;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 法人
     */
    private String jurPerson;

    /**
     * 委托代理人
     */
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
