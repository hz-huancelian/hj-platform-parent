package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 因子套餐查看
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FactorGroupItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检测标准编号，下发到各个机构
     */
    private String standardCode;


    //检测因子名称
    private String factorName;

    //标准号
    private String standardNo;

    //检测名称
    private String standardName;

    //标准分析方法
    private String analysisMethod;

    //一级分类ID
    private String classId;

    //一级分类名称
    private String className;

    //二级分类ID
    private String secdClassId;
    //二级分类名称
    private String secdClassName;

    private BigDecimal price;

    //标准状态
    private String methodStatus;

    /**
     * CMA能力(0:无 1:有)
     */
    private String cmaFlg;

    /**
     * CNAS能力(0:无 1:有)
     */
    private String cnasFlg;

    /**
     * 外部协助检测标识（0：自检 1：外协支持）
     */
    private String extAssistFlg;

}
