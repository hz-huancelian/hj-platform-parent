package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 数据单位分组Vo
 * @Iteration : 1.0
 * @Date : 2021/5/5  9:57 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UnitGroupInfoVo implements Serializable {
    private static final long serialVersionUID = 5327782116449339552L;

    //数据单位分组ID
    private String id;

    //数据单位分组名称
    private String name;

    //分组下单位集合
    private List<UnitInfoVo> vos;
}