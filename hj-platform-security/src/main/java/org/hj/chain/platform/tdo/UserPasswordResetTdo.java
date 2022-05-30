package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/31  3:06 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/31    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserPasswordResetTdo implements Serializable {

    private String userId;

    private String oldPassword;

    private String newPassword;
}