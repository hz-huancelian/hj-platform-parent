package org.hj.chain.platform.vo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckTaskFactorVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //二级分类名称
    private String secdClassName;
    //检测因子
    private String factorName;
    //调度任务计划因子ID
    private String jobPlanFactorId;
    //分包标志 0-自检；1-分包
    private String fbFlag;

    //数据录入环节 1-现场 2-实验室
    private String dataEntryStep;
    //二级分类ID
    private String secdClassId;
}
