package org.hj.chain.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.model.DynamicParamConf;
import org.hj.chain.platform.service.IDynamicParamConfService;
import org.hj.chain.platform.tdo.DynamicParamConfTdo;
import org.hj.chain.platform.vo.DynamicParamConfSearchVo;
import org.hj.chain.platform.vo.FactorSecdClassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/8
 */
@RestController
@RequestMapping("/platform/dpc")
public class DynamicParamConfController {
    @Autowired
    private IDynamicParamConfService dynamicParamConfService;

    /**
     * TODO 分页查询因子二级类别采样属性列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findPageByCondition")
    public Result<Page<DynamicParamConf>> findPageByCondition(@ModelAttribute PageVo pageVo,
                                                              @ModelAttribute DynamicParamConfSearchVo sv) {
        sv.setSecdClassId(StrUtil.trimToNull(sv.getSecdClassId()));
        sv.setPKey(StrUtil.trimToNull(sv.getPKey()));
        sv.setPName(StrUtil.trimToNull(sv.getPName()));
        Page<DynamicParamConf> page = PageUtil.initMpPage(pageVo);
        dynamicParamConfService.page(page, Wrappers.<DynamicParamConf>lambdaQuery()
                .eq(sv.getSecdClassId() != null, DynamicParamConf::getSecdClassId, sv.getSecdClassId())
                .eq(sv.getPKey() != null, DynamicParamConf::getPKey, sv.getPKey())
                .like(sv.getPName() != null, DynamicParamConf::getPName, sv.getPName())
                .orderByAsc(DynamicParamConf::getGroupKey, DynamicParamConf::getSort));
        return ResultUtil.data(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }
        dynamicParamConfService.update(Wrappers.<DynamicParamConf>lambdaUpdate()
                .set(DynamicParamConf::getIsDelete, "1")
                .eq(DynamicParamConf::getId, id));
        return ResultUtil.success("删除成功！");
    }

    @PostMapping("/add")
    public Result<Object> add(@Validated @RequestBody DynamicParamConfTdo tdo) {
        return dynamicParamConfService.add(tdo);
    }

    /**
     * TODO 查询所有二级分类
     * @return
     */
    @GetMapping("/getAllFactorSecdClass")
    public Result<List<FactorSecdClassVo>> getAllFactorSecdClass() {
        return dynamicParamConfService.getAllFactorSecdClass();
    }

    /**
     * TODO 查询二级分类下属性
     * @param secdClassId
     * @return
     */
    @GetMapping("/getListBySecdClassId/{secdClassId}")
    public Result<List<DynamicParamConf>> getListBySecdClassId(String secdClassId) {
        List<DynamicParamConf> list = dynamicParamConfService.list(Wrappers.<DynamicParamConf>lambdaQuery()
                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                .eq(DynamicParamConf::getIsDelete, "0")
                .orderByAsc(DynamicParamConf::getGroupKey, DynamicParamConf::getSort));
        return ResultUtil.data(list);
    }

}
