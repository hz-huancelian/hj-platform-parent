package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : zzl
 * @description 查询采样因子雨水入参条件
 * @Date : 2022.5.31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorRainSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //采样任务id
    private Long sampTaskId;

    //因子二级类别id
    private String secdClassId;

}
