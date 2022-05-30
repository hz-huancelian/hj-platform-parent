package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleFactorVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //样品ID
    private String sampItemId;
    //检测标准ID
    private String checkStandardId;
    //报价单因子ID
    private Long offFactorId;
    //二级类别ID
    private String secdClassId;
    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;
    //因子单位
    private String unitName;
    //因子名称
    private String factorName;
}
