package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JobFactorVo implements Serializable {
    private static final long serialVersionUID = -1L;
    //任务计划因子ID
    private Long jobPlanFactorId;
    private String secdClassId;
    //检测类别（二级类别名称）
    private String secdClassName;
    //检测因子
    private String factorName;
    //检测位置
    private String factorPoint;
    //频次/天
    private Integer frequency;
    //天数
    private Integer dayCount;
    //分包标志 0-自检；1-分包
    private String fbFlag;
    //因子点位归类标识
    private String factorGroupKey;
}
