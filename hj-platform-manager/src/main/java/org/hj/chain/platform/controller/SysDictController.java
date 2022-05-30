package org.hj.chain.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.service.ISysDictService;
import org.hj.chain.platform.vo.SysDictRootVo;
import org.hj.chain.platform.vo.SysDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 字典服务
 * @Iteration : 1.0
 * @Date : 2021/5/8  6:19 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;


    @RequestMapping(value = "/findPageByCondition", method = RequestMethod.GET)
    public Result<Page<SysDictRootVo>> findPageByCondition(@ModelAttribute PageVo pageVo, @RequestParam String dictVal) {
        Result<Page<SysDictRootVo>> page = sysDictService.findPageByCondition(pageVo, dictVal);
        return page;
    }


    /**
     * TODO 添加父节点
     *
     * @param dictVal
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:40 上午
     */
    @RequestMapping(value = "/addDictTree", method = RequestMethod.GET)
    public Result<Object> addDictTree(@RequestParam String dictVal, @RequestParam(required = false) String dictDesc) {

        if (StrUtil.isBlank(dictVal)) {
            return ResultUtil.validateError("字典值不能为空！");
        }

        int res = sysDictService.addTreeDict(dictVal, dictDesc);
        if (res > 0) {
            return ResultUtil.success("添加成功！");
        } else {
            return ResultUtil.busiError("添加失败！");
        }
    }

    /**
     * TODO 更新父节点
     *
     * @param id
     * @param dictVal
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:39 上午
     */
    @RequestMapping(value = "/updateTreeById", method = RequestMethod.GET)
    public Result<Object> updateTreeById(@RequestParam Long id,
                                         @RequestParam String dictVal,
                                         @RequestParam(required = false) String dictDesc) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }

        if (StrUtil.isBlank(dictVal)) {
            return ResultUtil.validateError("字典值不能为空！");
        }
        int res = sysDictService.updateTreeDict(id, dictVal, dictDesc);
        if (res > 0) {
            return ResultUtil.success("修改成功！");
        } else {
            return ResultUtil.busiError("修改失败！");
        }
    }


    /**
     * TODO 根据父节点获取子节点集合
     *
     * @param parentDictKey
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 8:09 上午
     */
    @RequestMapping(value = "/findDictVosById/{parentDictKey}", method = RequestMethod.GET)
    public Result<List<SysDictVo>> findDictListBySupDictKey(@PathVariable Long parentDictKey) {
        if (parentDictKey == null) {
            return ResultUtil.validateError("字典key不能为空！");
        }

        List<SysDictVo> vos = sysDictService.findDictVosById(parentDictKey);
        return ResultUtil.data(vos);
    }


    /**
     * TODO 添加叶子节点
     *
     * @param parentDictKey
     * @param dictVal
     * @param dictDesc
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:39 上午
     */
    @RequestMapping(value = "/addLeafDict", method = RequestMethod.GET)
    public Result<Object> addLeafDict(@RequestParam Long parentDictKey,
                                      @RequestParam String dictVal,
                                      @RequestParam String dictDesc) {
        if (parentDictKey == null) {
            return ResultUtil.validateError("父节点不能为空！");
        }

        if (StrUtil.isBlank(dictVal)) {
            return ResultUtil.validateError("字典值不能为空！");
        }

        int res = sysDictService.addLeafDict(parentDictKey, dictVal, dictDesc);
        if (res > 0) {
            return ResultUtil.success("添加成功！");
        } else {
            return ResultUtil.busiError("添加失败！");
        }
    }

    /**
     * TODO 修改叶子节点
     *
     * @param id
     * @param dictVal
     * @param dictDesc
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:37 上午
     */
    @RequestMapping(value = "/updateById", method = RequestMethod.GET)
    public Result<Object> updateById(@RequestParam Long id,
                                     @RequestParam String dictVal,
                                     @RequestParam String dictDesc) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }

        if (StrUtil.isBlank(dictVal)) {
            return ResultUtil.validateError("字典值不能为空！");
        }
        int res = sysDictService.updateLeafDict(id, dictVal, dictDesc);
        if (res > 0) {
            return ResultUtil.success("修改成功！");
        } else {
            return ResultUtil.busiError("修改失败！");
        }
    }


    /**
     * TODO 删除叶子节点
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:37 上午
     */
    @RequestMapping(value = "/delById/{id}", method = RequestMethod.GET)
    public Result<Object> delById(@PathVariable Long id) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }

        int res = sysDictService.delLeafDictById(id);
        if (res > 0) {
            return ResultUtil.success("删除成功！");
        } else {
            return ResultUtil.busiError("删除失败！");
        }
    }


    /**
     * TODO 恢复叶子节点
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:38 上午
     */
    @RequestMapping(value = "/enableById/{id}", method = RequestMethod.GET)
    public Result<Object> enableById(@PathVariable Long id) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }

        int res = sysDictService.enableLeafDictById(id);
        if (res > 0) {
            return ResultUtil.success("恢复成功！");
        } else {
            return ResultUtil.busiError("恢复失败！");
        }
    }
}