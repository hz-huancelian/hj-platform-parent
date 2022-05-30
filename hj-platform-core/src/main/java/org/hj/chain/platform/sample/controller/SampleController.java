package org.hj.chain.platform.sample.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.service.ISampleDrawApplyService;
import org.hj.chain.platform.sample.service.ISampleItemService;
import org.hj.chain.platform.sample.service.ISampleStoreApplyService;
import org.hj.chain.platform.sample.service.ISampleTaskService;
import org.hj.chain.platform.tdo.sample.SampleStoreTdo;
import org.hj.chain.platform.vo.sample.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private ISampleTaskService sampleTaskService;
    @Autowired
    private ISampleItemService sampleItemService;
    @Autowired
    private ISampleStoreApplyService sampleStoreApplyService;
    @Autowired
    private ISampleDrawApplyService sampleDrawApplyService;

    /**
     * TODO 样品管理列表（采样任务状态：采样中、已完成）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findByCondition")
    public Result<IPage<SampleManageVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                         @ModelAttribute SampCommSearchVo sv) {

        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        IPage<SampleManageVo> page = sampleTaskService.findSamplesForJobByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 样品管理-样品列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/getSamplesBySampTaskId")
    public Result<IPage<SampleVo>> getSamplesBySampTaskId(@ModelAttribute PageVo pageVo,
                                                          @ModelAttribute SampleSearchVo sv){
        if(sv.getSampTaskId() == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setDate(StrUtil.trimToNull(sv.getDate()));
        sv.setSampStatus(StrUtil.trimToNull(sv.getSampStatus()));
        IPage<SampleVo> page = sampleItemService.getSamplesBySampTaskId(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 样品管理-样品列表-详情查看
     * @param sampItemId
     * @return
     */
    @RequestMapping(value = "/getSampleDataBySampItemId/{sampItemId}", method = RequestMethod.GET)
    public Result<SampleItemVo> getSampleDataBySampItemId(@PathVariable Long sampItemId) {
        return sampleItemService.getSampleDataBySampItemId(sampItemId);
    }


    /**
     * TODO 入库申请列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/getSampStoreApplyList")
    public Result<IPage<SampleVo>> getSampStoreApplyList(@ModelAttribute PageVo pageVo,
                                                         @ModelAttribute SampleSearchVo sv) {
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        IPage<SampleVo> page = sampleStoreApplyService.getSampStoreApplyList(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 出库申请列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/getSampDrawApplyList")
    public Result<IPage<SampleVo>> getSampDrawApplyList(@ModelAttribute PageVo pageVo,
                                                        @ModelAttribute SampleSearchVo sv) {
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        IPage<SampleVo> page = sampleDrawApplyService.getSampDrawApplyList(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 申请列表-查看样品详情
     * @param sampItemId
     * @return
     */
    @RequestMapping(value = "/getSampleDetailBySampItemId/{sampItemId}", method = RequestMethod.GET)
    public Result<SampleDetailVo> getSampleDetailBySampItemId(@PathVariable Long sampItemId) {
        return sampleItemService.getSampleDetailBySampItemId(sampItemId);
    }

    /**
     * TODO 样品入库（单个）
     * @Author chh
     * @param tdo
     * @Date 2021-05-11 3:45
     * @Iteration 1.0
     */
    @RequestMapping(value = "/doStoreSample", method = RequestMethod.POST)
    public Result<Object> doStoreSample(@Validated @RequestBody SampleStoreTdo tdo) {
        return sampleStoreApplyService.doStoreSample(tdo);
    }

    /**
     * todo 样品入库（批量）
     * @param tdo 批量入库时，入库申请ID“,”拼接
     * @return
     */
    @RequestMapping(value = "/batchDoStoreSample", method = RequestMethod.POST)
    public Result<Object> batchDoStoreSample(@Validated @RequestBody SampleStoreTdo tdo) {
        return sampleStoreApplyService.batchDoStoreSample(tdo);
    }

    /**
     * TODO 样品出库（单个/批量）
     * @Author chh
     * @param drawApplyId 出库申请ID， 以","拼接
     * @Date 2021-05-14 12:06
     * @Iteration 1.0
     */
    @RequestMapping(value = "/doDrawSample", method = RequestMethod.POST)
    public Result<Object> doDrawSample(@RequestParam String drawApplyId) {
        if(StrUtil.isBlank(drawApplyId)) {
            return ResultUtil.validateError("样品ID不能为空！");
        }
        return sampleDrawApplyService.doDrawSample(drawApplyId);
    }
}
