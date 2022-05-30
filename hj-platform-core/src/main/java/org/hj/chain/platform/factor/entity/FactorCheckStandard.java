package org.hj.chain.platform.factor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 因子检测能力、费用表
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factor_check_standard")
public class FactorCheckStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    //机构编号
    private String organId;

    /**
     * 检测标准编号，下发到各个机构
     */
    private String standardCode;

    //因子名称
    private String factorName;

    //第一分类id
    private String classId;

    /**
     * CMA能力(0:无 1:有)
     */
    private String cmaFlg;

    /**
     * CNAS能力(0:无 1:有)
     */
    private String cnasFlg;

    private BigDecimal price;

    /**
     * 外部协助检测标识（0：自检 1：外协支持）
     */
    private String extAssistFlg;

    private Date createTime;

    private String updateUserId;

    private Date updateTime;


}
