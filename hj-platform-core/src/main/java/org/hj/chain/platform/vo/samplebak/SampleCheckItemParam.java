package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 合样后关联的因子信息
 * @Iteration : 1.0
 * @Date : 2021/12/16  11:37 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/12/16    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleCheckItemParam implements Serializable {
    private static final long serialVersionUID = 8415275348278076520L;

    //检测因子名称集合
    private String factorNames;

    //样品编号
    private String sampleNo;
}