package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 登录传入信息
 * @Iteration : 1.0
 * @Date : 2021/4/28  3:16 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/28    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileLoginByUsernameTdo implements Serializable {
    private static final long serialVersionUID = 8113266876384244028L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空！")
    @Length(min = 6, message = "密码长度必须超过6位！")
    private String password;
}