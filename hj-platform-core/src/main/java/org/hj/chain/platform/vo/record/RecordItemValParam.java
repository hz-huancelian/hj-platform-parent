package org.hj.chain.platform.vo.record;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 数据值实体
 * @Iteration : 1.0
 * @Date : 2022/4/7  7:03 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecordItemValParam implements Serializable {
    private static final long serialVersionUID = -3396773059101791908L;

    //描述
    private String desc;

    //值
    private String val;
}