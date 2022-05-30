package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告现场采样记录信息
 * @Iteration : 1.0
 * @Date : 2021/5/25  5:13 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportSampleFactorDataVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;

    //样品ID
    private String sampItemId;

    //采样数据-因子 json存储
    private String factorData;

}