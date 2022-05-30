package com.hj.chin.platform.sys;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.constants.BusiConstants;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/1  9:44 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/01    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AdminUserGenerate {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        String encode = BusiConstants.ENCRYPT_PREFIX + passwordEncoder.encode("FL123456");
        System.out.println(encode);
//
//        String uuid = StrUtil.uuid();
//        String uids = uuid.replace("-", "");
//        System.out.println(uids);

//        Map<Long, List<Long>> map = new HashMap<>();
//        List<Long> subKeys = new ArrayList<>();
//        subKeys.add(100301L);
//        subKeys.add(100302L);
//        subKeys.add(100303L);
//        subKeys.add(100304L);
//        map.put(1003L,subKeys);
//
//        System.out.println(map);
//        System.out.println(map.get(1003L));//{1003=[100301, 100302, 100303, 100304, 100305]}

    }
}