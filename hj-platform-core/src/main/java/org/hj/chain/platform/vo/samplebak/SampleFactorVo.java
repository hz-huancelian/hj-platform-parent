package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-13
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleFactorVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //样品ID
    private String sampItemId;

    //报价单因子ID
    private Long offerFactorId;

    //检测方法
    private String standardName;

    //一级类别
    private String className;

    //二级分类ID
    private String secdClassId;

    //二级分类
    private String secdClassName;
    /**
     * 频次/天
     */
    private Integer frequency;

    /**
     * 天数
     */
    private Integer dayCount;

    /**
     * 检测位置
     */
    private String factorPoint;

    /**
     * 检测标准ID
     */
    private String checkStandardId;
}
