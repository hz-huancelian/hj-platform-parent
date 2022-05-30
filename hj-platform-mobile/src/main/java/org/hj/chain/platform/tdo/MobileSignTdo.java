package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-12
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSignTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotNull(message = "样品ID不能为空！")
    //样品ID
    private Long sampItemId;
    @NotBlank(message = "签名图片不能为空！")
    //签名图片名称
    private String signImgName;
}
