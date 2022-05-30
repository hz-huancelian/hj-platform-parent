package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 平台用户
 * @Iteration : 1.0
 * @Date : 2021/5/8  7:49 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_platform_user")
public class PlatformUser implements Serializable {
    private static final long serialVersionUID = -1083713243137373641L;

    private Long id;

    //用户名
    private String username;

    //密码
    private String password;

    //用户类型
    private String userType;

    //联系电话
    private String phone;

    //员工名称
    private String empName;

    //状态
    private String status;

    //头像地址
    private String avatarPath;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}