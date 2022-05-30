package org.hj.chain.platform.tdo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleStoreTdo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    @NotBlank(message = "入库申请ID不能为空！")
    //批量入库时，入库申请ID“,”拼接
    private String storeApplyId;
    @NotBlank(message = "入库位置不能为空！")
    private String storeLocation;
    @NotBlank(message = "送样人不能为空！")
    private String sendUser;
}
