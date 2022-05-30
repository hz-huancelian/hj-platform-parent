package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/24  11:42 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/24    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleItemParam implements Serializable {

    //键值对
    private String key;
    //描述
    private String name;
    //值
    private String value;
    //第一次
    private String value1;
    //第二次
    private String value2;
}