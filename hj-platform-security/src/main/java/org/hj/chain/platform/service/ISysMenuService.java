package org.hj.chain.platform.service;

import org.hj.chain.platform.tdo.SysMenuTdo;
import org.hj.chain.platform.vo.RouterVo;
import org.hj.chain.platform.vo.SysMenuVo;
import org.hj.chain.platform.vo.TreeSelect;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface ISysMenuService {
    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户信息
     * @return 菜单列表
     */
    List<SysMenuVo> selectMenusByUser(String userId);


    /**
     * TODO 根据用户ID查看树形菜单
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 11:10 下午
     */
    List<TreeSelect> findMenusTreeByUser(String userId);

    /**
     * 查询系统菜单列表
     *
     * @param menuName 菜单名称
     * @param status   菜单状态
     * @param userId   用户ID
     * @return 菜单列表
     */
    List<SysMenuVo> selectMenuList(String userId, String menuName, String status);


    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectPermsByUserId(String userId);


    /**
     * 查询系统所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Map<String, String> selectPermsAll(String userId);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenuVo selectMenuById(Long menuId);


    /**
     * TODO 根据roleID查询关联的菜单信息
     *
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 4:21 下午
     */
    List<Long> selectMenuListByRoleId(Long roleId);


    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenuTdo menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(SysMenuTdo menu);

    /**
     * TODO 根据登录用户获取路由信息
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 10:17 下午
     */
    List<RouterVo> getRouters();
}
