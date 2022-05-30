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
 * @Date : 2022/3/31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorSecdClassVo implements Serializable {
    private static final long serialVersionUID = 5406787532736789836L;
    //二级分类ID
    private String secdClassId;
    //二级分类名称
    private String secdClassName;
    //一级分类名称
    private String className;
    //一级分类ID
    private String classId;
}
