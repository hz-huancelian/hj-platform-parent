package org.hj.chain.platform.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_contract_info")
public class ContractInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
     * 机构ID
     */
    private String organId;

    /**
     * 初始制作方式（0：在线制作 1：本地上传）
     */
    private String makeType;

    /**
     * 合同状态：0：待完善 1:待审核，2审核失败，3:待制作，4：完成，5-已作废；6-待提交
     */
    private String contStatus;


    /**
     * 签定时间
     */
    private String signDate;

    /**
     * 签定地点
     */
    private String signLocation;

    /**
     * 有效期（年）
     */
    private String validity;

    /**
     * 合同文件ID
     */
    private String contFileId;

    /**
     * 支付方式（1：一次支付 2：分期支付 3：其他方式）
     */
    private String payMethod;

    /**
     * 支付详情描述
     */
    private String payMethodDesc;

    /**
     * 合同制作人 创建人
     */
    private String makeUserId;
    /**
     * 合同审核人
     */
    private String contCheckUserId;

    /**
     * 甲方信息ID
     */
    private Long cusContBaseInfoId;

    //合同生成PDF地址
    private String contGenUrl;

    //部门ID
    private Long deptId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 主合同编号
     */
    private String supContCode;

}
