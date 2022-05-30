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
 *
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_cus_contract_base_info")
public class CusContractBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    /**
     * 创建人
     */
    private String createUserId;

    //删除状态：0未删除；1已删除
    private String delStatus;


    //部门ID
    private Long deptId;

    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateUserId;

    private LocalDateTime updateTime;


}
