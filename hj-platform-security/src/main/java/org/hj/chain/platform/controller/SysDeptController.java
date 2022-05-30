package org.hj.chain.platform.controller;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.tdo.SysDeptTdo;
import org.hj.chain.platform.vo.SysDeptVo;
import org.hj.chain.platform.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/26  6:04 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/26    create
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;


    /**
     * TODO 条件查询部门列表信息
     *
     * @param deptName
     * @param status
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:07 下午
     */
    @RequestMapping(value = "/selectDeptList", method = RequestMethod.GET)
    public Result<List<SysDeptVo>> selectDeptList(@RequestParam String deptName, @RequestParam String status) {
        List<SysDeptVo> sysDeptVos = sysDeptService.selectDeptList(deptName, status);
        return ResultUtil.data(sysDeptVos);
    }



    /**
     * TODO 查询所有部门列表
      * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:26 下午
     */
    @RequestMapping(value = "/selectAllDepts", method = RequestMethod.GET)
    public Result<List<SysDeptVo>> selectAllDepts() {
        List<SysDeptVo> sysDeptVos = sysDeptService.selectAllDepts();
        return ResultUtil.data(sysDeptVos);
    }


    /**
     * TODO 查询所有部门列表
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:26 下午
     */
    @RequestMapping(value = "/selectDeptTrees", method = RequestMethod.GET)
    public Result<List<TreeSelect>> selectDeptTrees() {
        List<TreeSelect> sysDeptVos = sysDeptService.selectDeptTrees();
        return ResultUtil.data(sysDeptVos);
    }



    /**
     * TODO 根据部门ID查看部门信息详情
     *
     * @param deptId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:21 下午
     */
    @RequestMapping(value = "/selectDeptById/{deptId}", method = RequestMethod.GET)
    public Result<SysDeptVo> selectDeptById(@PathVariable Long deptId) {
        if (deptId == null) {
            return ResultUtil.validateError("部门ID不能为空！");
        }
        SysDeptVo sysDeptVos = sysDeptService.selectDeptById(deptId);
        return ResultUtil.data(sysDeptVos);
    }

    /**
     * TODO 新增部门信息
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:13 下午
     */
    @RequestMapping(value = "/insertDept", method = RequestMethod.POST)
    public Result<Object> insertDept(@RequestBody SysDeptTdo tdo) {
        int count = sysDeptService.insertDept(tdo);

        if (count > 0) {
            return ResultUtil.success("新增成功！");
        }
        return ResultUtil.error("新增失败！");
    }

    /**
     * TODO 更新部门信息
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:13 下午
     */
    @RequestMapping(value = "/updateDept", method = RequestMethod.POST)
    public Result<Object> updateDept(@RequestBody SysDeptTdo tdo) {
        int count = sysDeptService.updateDept(tdo);

        if (count > 0) {
            return ResultUtil.success("更新成功！");
        }
        return ResultUtil.error("更新失败！");
    }

    @RequestMapping(value = "/deleteDeptById/{deptId}", method = RequestMethod.GET)
    public Result<Object> deleteDeptById(@PathVariable Long deptId) {
        if (deptId == null) {
            return ResultUtil.validateError("部门ID不能为空！");
        }
        int count = sysDeptService.deleteByDeptId(deptId);
        if (count > 0) {
            return ResultUtil.success("删除成功！");
        }
        return ResultUtil.error("删除失败！");
    }

}