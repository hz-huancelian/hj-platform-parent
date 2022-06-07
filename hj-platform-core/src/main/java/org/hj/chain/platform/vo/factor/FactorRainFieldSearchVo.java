package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : zzl
 * @description 查询二级分类字段条件
 * @Date : 2022.6.6
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorRainFieldSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //二级类别id
    private String secdClassId;

}
