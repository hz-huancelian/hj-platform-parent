package org.hj.chain.platform.vo.record;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/8/10  11:22 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/10    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecordValItem implements Serializable {
    private static final long serialVersionUID = 1L;

    //数据项填写数据类型
    private String numericType;

    //值（参考RecordItemValParam）
    private String value;

}