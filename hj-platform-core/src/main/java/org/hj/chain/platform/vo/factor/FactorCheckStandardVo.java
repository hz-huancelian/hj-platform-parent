package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class FactorCheckStandardVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 检测标准编号，下发到各个机构
     */
    private String standardCode;

    //检测因子名称
    private String factorName;

    //因子单位
    private String factorUnit;

    //标准检测名称
    private String standardName;

    //标准号
    private String standardNo;

    //标准分析方法
    private String analysisMethod;

    //方法状态:0-现行；1-自定义标准 2试行；3暂行；4废止
    private String methodStatus;

    //第一分类ID
    private String classId;

    //一级分类名称
    private String className;

    /**
     * CMA能力(0:无 1:有)
     */
    private String cmaFlg;

    /**
     * CNAS能力(0:无 1:有)
     */
    private String cnasFlg;

    private BigDecimal price;

    //    /**
//     * 外部协助检测标识（0：自检 1：外协支持）
//     */
    private String extAssistFlg;

    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;

}
