package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysRoleTdo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID（更新时不能为空）
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色排序
     */
    private String roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    private String dataScope;

    //状态（0-有效；1-停用）（更新时必填）
    private String status;

    //备注
    private String remark;

    //菜单集合
    private List<Long> menuIds;

}
