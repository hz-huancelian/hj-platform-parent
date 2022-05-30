package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 样品因子相关实体
 * @Iteration : 1.0
 * @Date : 2021/8/18  4:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/18    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleFactorIdParam implements Serializable {
    private static final long serialVersionUID = -9205855716818042274L;

    //检测因子
    private String checkStandardId;

    //样品ID
    private String sampItemId;
}