package org.hj.chain.platform.factor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.service.FactorRainService;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.vo.factor.FactorRainSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : zzl
 * @description 因子服务
 * @Date : 2022.6.1
 */
@RestController
@RequestMapping("/factor/category")
public class FactorsController {

    @Autowired
    private FactorRainService factorRainService;

    /**
     * TODO 分页查询雨水因子表单信息
     * @param pageVo
     * @param fr
     * @Author: zzl
     * @Date: 2022.6.1
     */
    @GetMapping("/rainFindPageByCondition")
    public Result<IPage<FactorRainVo>> rainsFindPageByCondition(@ModelAttribute PageVo pageVo,
                                                            @ModelAttribute FactorRainSearchVo fr) {
        IPage<FactorRainVo> page = factorRainService.findFactorRainByCondition(pageVo, fr);
        return ResultUtil.data(page);
    }

    /**
     * TODO 分页查询采样类别
     * @param pageVo
     * @Author: zzl
     * @Date: 2022.6.2
     */
    @GetMapping("/factorClassFindPageByCondition")
    public Result<IPage<FactorClassInfo>> factorsClassFindPageByCondition(@ModelAttribute PageVo pageVo) {
        IPage<FactorClassInfo> page = factorRainService.findFactorClassInfoCondition(pageVo);
        return ResultUtil.data(page);
    }

}
