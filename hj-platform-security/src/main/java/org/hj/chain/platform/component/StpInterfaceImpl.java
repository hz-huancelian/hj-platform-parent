package org.hj.chain.platform.component;

import cn.dev33.satoken.stp.StpInterface;
import org.hj.chain.platform.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 自定义权限验证接口扩展
 * 保证此类被SpringBoot扫描，完成sa-token的自定义权限验证扩展
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/4/27  10:12 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/27    create
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 返回一个账号所拥有的权限Url集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> resources = sysUserService.selectUserPermsGroup((String) loginId);
        return resources;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> roleKeyList = sysUserService.selectUserRoleGroup((String) loginId);
        return roleKeyList;
    }

}