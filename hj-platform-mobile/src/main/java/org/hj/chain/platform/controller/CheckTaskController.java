package org.hj.chain.platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.service.ICheckTaskService;
import org.hj.chain.platform.tdo.MobileCheckFactorTdo;
import org.hj.chain.platform.vo.MobileCheckFactorInfoVo;
import org.hj.chain.platform.vo.MobileSampleItemDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 检测管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@RestController
@RequestMapping("/check")
public class CheckTaskController {
    @Autowired
    private ICheckTaskService checkTaskService;

    /**
      * TODO 检测员获取样品列表
      * @Author chh
      * @param pageVo
      * @param taskStatus 1-待领样；2-待检测；3-已驳回
      * @Date 2021-05-09 1:41
      * @Iteration 1.0
      */
    @RequestMapping(value = "/findCheckTaskByCondition", method = RequestMethod.GET)
    public Result<IPage<MobileCheckFactorInfoVo>> findCheckTaskByCondition(@ModelAttribute PageVo pageVo
            , @RequestParam String taskStatus) {
        String userId = (String) StpUtil.getLoginId();
        IPage<MobileCheckFactorInfoVo> vos = checkTaskService.findCheckTaskByCondition(pageVo, userId, taskStatus);
        return ResultUtil.data(vos);
    }

    /**
      * TODO 检测员批量申请领样
      * @Author chh
      * @param ids 检测列表ID"，"拼接
      * @Date 2021-05-09 23:21
      * @Iteration 1.0
      */
    @RequestMapping(value = "/batchSampDrawApply", method = RequestMethod.POST)
    public Result<Object> batchSampDrawApply(@RequestBody String ids) {
        return checkTaskService.batchSampDrawApply(ids);
    }

    /**
      * TODO 检测员单个申请领样
      * @Author chh
      * @param id 检测列表ID
      * @Date 2021-05-09 23:23
      * @Iteration 1.0
      */
    @RequestMapping(value = "/sampDrawApply", method = RequestMethod.POST)
    public Result<Object> sampDrawApply(@RequestBody String id) {
        return checkTaskService.sampDrawApply(id);
    }

    /**
      * TODO 录入因子检测数据
      * @Author chh
      * @param tdo
      * @Date 2021-05-10 0:40
      * @Iteration 1.0
      */
    @RequestMapping(value = "/saveCheckFactorData", method = RequestMethod.POST)
    public Result<Object> saveCheckFactorData(@Validated @RequestBody MobileCheckFactorTdo tdo) {
        return checkTaskService.saveCheckFactorData(tdo);
    }

    /**
      * TODO 根据检测列表ID获取样品详情数据
      * @Author chh
      * @param id 检测列表ID
      * @Date 2021-05-10 1:20
      * @Iteration 1.0
      */
    @RequestMapping(value = "/getCheckSampItemDetail/{id}", method = RequestMethod.GET)
    public Result<MobileSampleItemDetailVo> getCheckSampItemDetail(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.busiError("检测列表ID不能为空！");
        }
        return checkTaskService.getCheckSampItemDetail(id);
    }
}
