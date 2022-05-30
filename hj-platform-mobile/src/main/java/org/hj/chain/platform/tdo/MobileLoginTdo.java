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
 * @Date : 2021-05-07
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileLoginTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotBlank(message = "微信用户唯一标识不能为空！")
    private String openId;
    @NotBlank(message = "会话密钥不能为空！")
    private String sessionKey;
    @NotBlank(message = "加密数据不能为空！")
    private String encryptedData;
    @NotBlank(message = "加密算法的初始向量不能为空！")
    private String iv;
}
