package org.hj.chain.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.tdo.RoleMenuRelTdo;
import org.hj.chain.platform.tdo.SysRoleTdo;
import org.hj.chain.platform.vo.RoleQueryVo;
import org.hj.chain.platform.vo.SysRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/26  5:25 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/26    create
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;


    /**
     * TODO 分页查询角色信息
     *
     * @param pageVo
     * @param queryVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:29 下午
     */
    @RequestMapping(value = "/findRolesByCondition", method = RequestMethod.GET)
    public Result<IPage<SysRoleVo>> findRolesByCondition(@ModelAttribute PageVo pageVo, @ModelAttribute RoleQueryVo queryVo) {


        IPage<SysRoleVo> page = sysRoleService.selectRoleListByCondition(pageVo, queryVo);

        return ResultUtil.data(page);
    }


    /**
     * TODO 查询所有有效角色列表信息
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/2 11:50 上午
     */
    @RequestMapping(value = "/selectRoleAll", method = RequestMethod.GET)
    public Result<List<SysRoleVo>> selectRoleAll() {
        List<SysRoleVo> page = sysRoleService.selectRoleAll();
        return ResultUtil.data(page);
    }

    /**
     * TODO 根据角色ID查询角色信息
     *
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:32 下午
     */
    @RequestMapping(value = "/findRoleByRoleId/{roleId}", method = RequestMethod.GET)
    public Result<SysRoleVo> findRoleByRoleId(@PathVariable Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        SysRoleVo sysRoleVo = sysRoleService.selectRoleById(roleId);

        return ResultUtil.data(sysRoleVo);
    }


    /**
     * TODO 根据角色ID删除角色信息
     *
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:32 下午
     */
    @RequestMapping(value = "/delByRoleId/{roleId}", method = RequestMethod.GET)
    public Result<Object> delByRoleId(@PathVariable Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        boolean res = sysRoleService.deleteRoleById(roleId);

        if (res) {
            return ResultUtil.success("删除成功！");
        }

        return ResultUtil.error("删除失败！");
    }


    /**
     * TODO 根据角色ID批量删除角色信息
     *
     * @param roleIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:32 下午
     */
    @RequestMapping(value = "/delByRoleIds/{roleIds}", method = RequestMethod.GET)
    public Result<Object> delByRoleIds(@PathVariable String roleIds) {

        if (StrUtil.isBlank(roleIds)) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        int count = sysRoleService.deleteRoleByIds(roleIds);

        if (count > 0) {
            return ResultUtil.success("删除成功！");
        }

        return ResultUtil.error("删除失败！");
    }


    /**
     * TODO 新增角色信息
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:41 下午
     */
    @RequestMapping(value = "/insertRole", method = RequestMethod.POST)
    public Result<Object> insertRole(@RequestBody SysRoleTdo tdo) {

            int count = sysRoleService.insertRole(tdo);
        if (count > 0) {
            return ResultUtil.success("新增成功！");
        } else if (count == -1) {
            return ResultUtil.busiError("权限名库中已存在！");
        } else if (count == -2) {
            return ResultUtil.busiError("权限字符中已存在！");
        }


        return ResultUtil.error("新增失败！");
    }


    /**
     * TODO 更新角色信息
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:41 下午
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public Result<Object> updateRole(@RequestBody SysRoleTdo tdo) {

        Long roleId = tdo.getRoleId();
        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        int count = sysRoleService.updateRole(tdo);
        if (count > 0) {
            return ResultUtil.success("更新成功！");
        } else if (count == -1) {
            return ResultUtil.busiError("权限名库中已存在！");
        } else if (count == -2) {
            return ResultUtil.busiError("权限字符中已存在！");
        }

        return ResultUtil.error("更新失败！");
    }


    /**
     * TODO 添加角色权限关系
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:41 下午
     */
    @RequestMapping(value = "/saveRoleMenuRel", method = RequestMethod.POST)
    public Result<Object> saveRoleMenuRel(@RequestBody RoleMenuRelTdo tdo) {

        Long roleId = tdo.getRoleId();
        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        List<Long> menuIds = tdo.getMenuIds();
        if (menuIds == null || menuIds.isEmpty()) {
            return ResultUtil.validateError("菜单ID不能为空！");
        }
        int count = sysRoleService.saveRoleMenuRel(tdo);
        if (count > 0) {
            return ResultUtil.success("更新成功！");
        }

        return ResultUtil.error("更新失败！");
    }


    /**
     * TODO 修改角色权限范围
     *
     * @param roleId
     * @param dataScope
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:46 下午
     */
    @RequestMapping(value = "/authDataScope", method = RequestMethod.GET)
    public Result<Object> authDataScope(@RequestParam Long roleId, @RequestParam String dataScope) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        if (StrUtil.isBlank(dataScope)) {
            return ResultUtil.validateError("数据权限范围不能为空！");
        }

        int count = sysRoleService.authDataScope(roleId, dataScope);
        if (count > 0) {
            return ResultUtil.success("更新成功！");
        }

        return ResultUtil.error("更新失败！");
    }


    /**
     * TODO 删除授权用户
     *
     * @param userId
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:49 下午
     */
    @RequestMapping(value = "/deleteAuthUser", method = RequestMethod.GET)
    public Result<Object> deleteAuthUser(@RequestParam String userId, @RequestParam Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        if (userId == null) {
            return ResultUtil.validateError("用户ID不能为空！");
        }

        int count = sysRoleService.deleteAuthUser(userId, roleId);
        if (count > 0) {
            return ResultUtil.success("取消授权成功！");
        }

        return ResultUtil.error("取消授权失败！");
    }


    /**
     * TODO 批量取消授权用户角色
     *
     * @param userIds
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:51 下午
     */
    @RequestMapping(value = "/deleteAuthUsers", method = RequestMethod.GET)
    public Result<Object> deleteAuthUsers(@RequestParam String userIds, @RequestParam Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        if (StrUtil.isBlank(userIds)) {
            return ResultUtil.validateError("用户ID不能为空！");
        }

        int count = sysRoleService.deleteAuthUsers(roleId, userIds);
        if (count > 0) {
            return ResultUtil.success("取消授权成功！");
        }

        return ResultUtil.error("取消授权失败！");
    }


    /**
     * TODO 给用户授权
     *
     * @param userIds
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:53 下午
     */
    @RequestMapping(value = "/insertAuthUsers", method = RequestMethod.GET)
    public Result<Object> insertAuthUsers(@RequestParam String userIds, @RequestParam Long roleId) {

        if (roleId == null) {
            return ResultUtil.validateError("角色ID不能为空！");
        }

        if (StrUtil.isBlank(userIds)) {
            return ResultUtil.validateError("用户ID不能为空！");
        }

        int count = sysRoleService.insertAuthUsers(roleId, userIds);
        if (count > 0) {
            return ResultUtil.success("授权成功！");
        }

        return ResultUtil.error("授权失败！");
    }
}