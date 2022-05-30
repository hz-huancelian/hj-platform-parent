package org.hj.chain.platform;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Description: TODO 密码加密验证
 * @Param:
 * @return:
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2021/4/27 9:24 下午
 */
public class BPwdEncoderUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 匹配密码
     *
     * @param rawPassword     未加密密码
     * @param encodedPassword 加密密码
     * @return
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
