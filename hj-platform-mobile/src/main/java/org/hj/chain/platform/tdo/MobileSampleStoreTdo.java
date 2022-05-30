package org.hj.chain.platform.tdo;

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
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleStoreTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotBlank(message = "入库申请ID不能为空！")
    //样品ID
    private Long storeApplyId;
    //送样人
    @NotBlank(message = "送样人不能为空！")
    private String sendUser;
    //库存位置
    @NotBlank(message = "库存位置不能为空！")
    private String storeLocation;
}
