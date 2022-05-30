package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子查询Vo
 * @Iteration : 1.0
 * @Date : 2021/5/5  11:30 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorSearchVo implements Serializable {
    private static final long serialVersionUID = -1847646127305822261L;

    //检测因子
    private String factorName;

    //一级类别
    private String classId;

    //二级类别
    private String secdClassId;

    //套餐认证类型0-CMA;1-CNAS
    private String authType;

    //标准号
    private String standardNo;

    //标准名称
    private String standardName;

    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;
}