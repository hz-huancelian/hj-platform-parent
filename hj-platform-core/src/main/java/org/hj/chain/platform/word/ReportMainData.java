package org.hj.chain.platform.word;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告主数据
 * @Iteration : 1.0
 * @Date : 2021/5/24  11:38 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/24    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportMainData implements Serializable {
    private static final long serialVersionUID = 5453758211338808230L;


    //地址
    private String address;

    //委托人单位
    private String consignorName;

    /**
     * 委托联系人
     */
    private String consignorLinker;

    /**
     * 委托联系方式
     */
    private String consignorLinkerPhone;


    /**
     * 检测周期
     */
    private String checkPeriod;

    //检测类型
    private String checkType;

    //样品类别
    private String secdClassNames;

    //检测人员
    private String checkEmpNames;

    //检测目的
    private String checkGoal;

    //检测内容（二级类别：因子名称的组合）
    private String checkDetails;

    //采样日期
    private String collectDate;

    //第一次审核
    private String firstAuditUserName;

    //签发
    private String signUserName;

    //编制人
    private String makeUserName;
}