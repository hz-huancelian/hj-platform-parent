package org.hj.chain.platform.controller;

import cn.hutool.core.util.StrUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.vo.FactorClassInfoVo;
import org.hj.chain.platform.vo.FactorClassTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子相关控制
 * @Iteration : 1.0
 * @Date : 2021/5/6  3:40 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@RestController
@RequestMapping("/platform/factor")
public class FactorController {

    @Autowired
    private IFactorService factorService;


    /**
     * TODO 查询所有的一级分类
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 3:43 下午
     */
    @RequestMapping(value = "/findFstClasses", method = RequestMethod.GET)
    public Result<List<FactorClassInfoVo>> findFstClasses() {
        List<FactorClassInfoVo> vos = factorService.findFstClass();

        return ResultUtil.data(vos);
    }

    /**
     * TODO 根据一级分类查看下面所有的二级分类
     *
     * @param classId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 3:44 下午
     */
    @RequestMapping(value = "/findSecdClassByClassId/{classId}", method = RequestMethod.GET)
    public Result<List<FactorClassInfoVo>> findSecdClassByClassId(@PathVariable String classId) {
        if (StrUtil.isBlank(classId)) {
            return ResultUtil.validateError("一级分类ID不能为空！");
        }
        List<FactorClassInfoVo> vos = factorService.findSecdClassByClassId(classId);
        return ResultUtil.data(vos);
    }

    /**
     * 查询所有因子二级分类
     * @return
     */
    @GetMapping("/getAllFactorSecClass")
    public Result<List<FactorClassInfoVo>> getAllFactorSecClass() {
        List<FactorClassInfoVo> vos = factorService.getAllFactorSecClass();
        return ResultUtil.data(vos);
    }

    /**
     * TODO 查询字典树
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 1:47 下午
     */
    @RequestMapping(value = "/findClassTrees", method = RequestMethod.GET)
    public Result<List<FactorClassTreeVo>> findClassTree() {
        List<FactorClassInfoVo> vos = factorService.findFstClass();
        if (vos != null && !vos.isEmpty()) {
            List<FactorClassTreeVo> classTreeVos = vos.stream().map(item -> {
                FactorClassTreeVo treeVo = new FactorClassTreeVo();
                treeVo.setId(item.getId())
                        .setName(item.getName());
                List<FactorClassInfoVo> childrens = factorService.findSecdClassByClassId(item.getId());
                if (childrens != null && !childrens.isEmpty()) {
                    List<FactorClassTreeVo> list = childrens.stream().map(subItem -> {
                        FactorClassTreeVo treeVo1 = new FactorClassTreeVo();
                        treeVo1.setId(subItem.getId())
                                .setName(subItem.getName());
                        return treeVo1;
                    }).collect(Collectors.toList());
                    treeVo.setChildren(list);
                }
                return treeVo;
            }).collect(Collectors.toList());
            return ResultUtil.data(classTreeVos);
        }

        return ResultUtil.data(null);
    }


}