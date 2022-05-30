package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/8
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DynamicParamConfSearchVo implements Serializable {
    private static final long serialVersionUID = -8619668536370323337L;
    //因子二级分类ID
    private String secdClassId;
    //属性键
    private String pKey;
    //属性名
    private String pName;
}
