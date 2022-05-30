package org.hj.chain.platform.factor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 套餐信息
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factor_group")
public class FactorGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //机构ID
    private String organId;

    //套餐名称
    private String groupName;

    //套餐说明（因子名称“，”拼接）
    private String groupDesc;

    //认证类型 0-CMA;1-CNAS
    private String authType;

    /**
     * 因子个数
     */
    private Integer factorNum;

    //套餐类型：0-自由因子；1-同系套餐
    private String groupType;

    //套餐价格
    private BigDecimal groupCost;

    //状态：0-有效；1-删除
    private String status;

    //创建人
    private String createBy;


    //备注
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
