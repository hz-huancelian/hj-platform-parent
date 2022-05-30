package org.hj.chain.platform.vo.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/7  9:28 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DictParam implements Serializable {
    private static final long serialVersionUID = -7683190419067926794L;

    private Long dictKey;

    //字典值
    private String dictVal;

    //字典状态
    private String status;
}