package org.hj.chain.platform.tdo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.check.CheckFactorSubsetVo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckFactorInfoTdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    @NotNull(message = "检测列表ID不能为空！")
    //检测列表ID
    private Long checkFactorId;
    //检测结果 （同系物套餐因子不填）
    //检测结果(json对象){"v1":"", "v2":""} 注：v1：实数；v2：指数
    private String checkRes;
    //同系物因子检测结果
    private List<CheckFactorSubsetVo> factorSubsetVos;
    //备注
    private String remark;
    //检测设备
    private String checkEquipment;
}
