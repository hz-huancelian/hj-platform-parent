package org.hj.chain.platform.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.service.ISysMenuService;
import org.hj.chain.platform.tdo.SysMenuTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.SysMenuVo;
import org.hj.chain.platform.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/25  7:15 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/25    create
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * TODO 查询系统菜单
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 7:19 下午
     */
    @RequestMapping(value = "/findMenusByUser", method = RequestMethod.GET)
    public Result<List<SysMenuVo>> findMenusByUser() {

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        List<SysMenuVo> sysMenuVos = sysMenuService.selectMenusByUser(loginOutputVo.getUserId());

        return ResultUtil.data(sysMenuVos);

    }


    /**
     * TODO 查询系统树形菜单
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 7:19 下午
     */
    @RequestMapping(value = "/findMenusTreeByUser", method = RequestMethod.GET)
    public Result<List<TreeSelect>> findMenusTreeByUser() {

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        List<TreeSelect> treeSelect = sysMenuService.findMenusTreeByUser(loginOutputVo.getUserId());

        return ResultUtil.data(treeSelect);

    }


    /**
     * TODO 根据角色ID查看关联的菜单信息
     *
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 4:25 下午
     */
    @RequestMapping(value = "/roleMenuTreeselect/{roleId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> roleMenuTreeselect(@PathVariable Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        List<TreeSelect> treeSelect = sysMenuService.findMenusTreeByUser(loginOutputVo.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("treeSelect", treeSelect);
        List<Long> checkedKeys = sysMenuService.selectMenuListByRoleId(roleId);
        map.put("checkedKeys", checkedKeys);
        return ResultUtil.data(map);

    }

    /**
     * TODO 查询系统菜单列表(系统操作列表)
     *
     * @param menuName
     * @param status
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:16 下午
     */
    @RequestMapping(value = "/selectMenuList", method = RequestMethod.GET)
    public Result<List<SysMenuVo>> selectMenuList(@RequestParam String menuName, @RequestParam String status) {

        menuName = StrUtil.trimToNull(menuName);
        status = StrUtil.trimToNull(status);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        List<SysMenuVo> sysMenuVos = sysMenuService.selectMenuList(loginOutputVo.getUserId(), menuName, status);

        return ResultUtil.data(sysMenuVos);

    }


    /**
     * TODO 新增菜单
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:22 下午
     */
    @RequestMapping(value = "/insertMenu", method = RequestMethod.POST)
    public Result<Object> insertMenu(@RequestBody SysMenuTdo tdo) {
        int res = sysMenuService.insertMenu(tdo);
        if (res > 0) {
            return ResultUtil.success("新增成功！");
        } else if (res == -1) {
            return ResultUtil.busiError("同一父菜单下菜单名称重复！");
        }
        return ResultUtil.busiError("新增失败！");
    }


    /**
     * TODO 更新菜单
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:22 下午
     */
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public Result<Object> updateMenu(@RequestBody SysMenuTdo tdo) {
        Long menuId = tdo.getMenuId();
        if (menuId == null) {
            return ResultUtil.validateError("菜单ID不能为空！");
        }
        int res = sysMenuService.updateMenu(tdo);
        if (res > 0) {
            return ResultUtil.success("新增成功！");
        } else if (res == -1) {
            return ResultUtil.busiError("同一父菜单下菜单名称重复！");
        }
        return ResultUtil.busiError("新增失败！");
    }


    /**
     * TODO 删除菜单
     *
     * @param menuId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:24 下午
     */
    @RequestMapping(value = "/deleteMenuById/{menuId}", method = RequestMethod.GET)
    public Result<Object> deleteMenuById(@PathVariable Long menuId) {
        if (menuId == null) {
            return ResultUtil.validateError("菜单ID不能为空！");
        }
        int res = sysMenuService.deleteMenuById(menuId);
        if (res > 0) {
            return ResultUtil.success("删除成功！");
        }
        return ResultUtil.busiError("删除失败！");
    }

}