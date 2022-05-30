package org.hj.chain.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.annotation.CustomParam;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.service.ISampleManagerService;
import org.hj.chain.platform.service.ISamplerService;
import org.hj.chain.platform.tdo.MobileSampleStoreTdo;
import org.hj.chain.platform.vo.MobileSampleItemDetailVo;
import org.hj.chain.platform.vo.MobileSampleItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 样品管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
@RestController
@RequestMapping("/sampManager")
public class SampleManagerController {
    @Autowired
    private ISampleManagerService sampleManagerService;
    @Autowired
    private ISamplerService samplerService;

    /**
      * TODO 样品管理员获取样品列表
      * @Author chh
      * @param pageVo
      * @param sampStatus 0-待入库；1-待出库
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/findSampleItemByCondition", method = RequestMethod.GET)
    public Result<IPage<MobileSampleItemVo>> findSampleItemByCondition(@ModelAttribute PageVo pageVo,
                                                                       @RequestParam String sampStatus) {
        IPage<MobileSampleItemVo> vos = sampleManagerService.findSampleItemByCondition(pageVo, sampStatus);
        return ResultUtil.data(vos);
    }

    /**
      * TODO 获取样品详情
      * @Author chh
      * @param sampItemId
      * @Date 2022-03-17
      * @Iteration 3.0
      */
//    @RequestMapping(value = "/getSampItemDetailBySampItemId/{sampItemId}", method = RequestMethod.GET)
//    public Result<MobileSampleItemDetailVo> getSampItemDetailBySampItemId(@PathVariable Long sampItemId) {
//        return samplerService.getSampleDetailBySampItemId(sampItemId);
//    }

    /**
      * TODO 样品入库
      * @Author chh
      * @param tdo
      * @Date 2021-05-12 10:26
      * @Iteration 1.0
      */
    @RequestMapping(value = "/storeSample", method = RequestMethod.POST)
    public Result<Object> storeSample(@Validated  @RequestBody MobileSampleStoreTdo tdo) {
        return sampleManagerService.storeSample(tdo);
    }

    /**
      * TODO 样品出库（单个/批量）
      * @Author chh
      * @param drawApplyId 出库申请ID， 以","拼接
      * @Date 2021-05-12 10:45
      * @Iteration 1.0
      */
    @RequestMapping(value = "/drawSample", method = RequestMethod.POST)
    public Result<Object> drawSample(@CustomParam String drawApplyId) {
        return sampleManagerService.drawSample(drawApplyId);
    }
}
