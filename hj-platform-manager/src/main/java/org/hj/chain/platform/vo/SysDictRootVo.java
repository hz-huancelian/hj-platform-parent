package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : chem-erp
 * @Description : TODO
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/3/31  2:16 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/03/31    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysDictRootVo implements Serializable {
    private static final long serialVersionUID = -784618570862682552L;

    //主键ID
    private Long id;

    //label
    private String dictVal;

    //字典值
    private Long dictKey;

    //字典描述
    private String dictDesc;
}