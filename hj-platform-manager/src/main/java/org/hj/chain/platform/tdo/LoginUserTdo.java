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
 * @description TODO 登录用户信息
 * @Iteration : 1.0
 * @Date : 2021/5/8  8:14 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginUserTdo implements Serializable {
    private static final long serialVersionUID = -4602788802822179759L;


    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空！")
    @Length(min = 6, max = 8, message = "密码必须是6-8位的数字和字符串！")
    private String password;
}