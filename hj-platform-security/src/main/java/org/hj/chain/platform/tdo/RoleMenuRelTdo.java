package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/8/1  7:14 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/01    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RoleMenuRelTdo {

    //角色ID
    private Long roleId;

    //菜单集合
    private List<Long> menuIds;
}