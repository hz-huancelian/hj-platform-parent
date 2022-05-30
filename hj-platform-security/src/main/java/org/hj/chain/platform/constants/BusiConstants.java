package org.hj.chain.platform.constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 业务常量
 * @Iteration : 1.0
 * @Date : 2021/4/28  4:12 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/28    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BusiConstants {


    public static final String login_key = "login";


    //用户存放到session的key
    public static final String SESSION_USER_KEY = "user";


    //加密前缀
    public static final String ENCRYPT_PREFIX = "{bcrypt}";
}