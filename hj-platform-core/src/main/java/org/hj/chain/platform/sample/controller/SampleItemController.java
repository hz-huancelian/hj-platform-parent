package org.hj.chain.platform.sample.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.service.ISampleItemService;
import org.hj.chain.platform.tdo.sample.SampleItemAuditTdo;
import org.hj.chain.platform.vo.sample.SampleItemInfoSearchVo;
import org.hj.chain.platform.vo.sample.SampleItemInfoVo;
import org.hj.chain.platform.vo.sample.SampleTaskListVo;
import org.hj.chain.platform.vo.sample.SampleTaskSearchVo;
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
@RequestMapping("/sampItem")
public class SampleItemController {

    @Autowired
    private ISampleItemService sampleItemService;

    /**
     * TODO 采样审核任务列表查询(状态处于“采样中”的)
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findSampTaskForManagerByCondition")
    public Result<IPage<SampleTaskListVo>> findSampTaskForManagerByCondition(@ModelAttribute PageVo pageVo,
                                                           @ModelAttribute SampleTaskSearchVo sv) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        sv.setSampTaskStatus("2");
        IPage<SampleTaskListVo> page = sampleItemService.findSampTaskForManagerByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 采样样品审核列表（待审核）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findAuditSampItemForManageByCondition")
    public Result<IPage<SampleItemInfoVo>> findAuditSampItemForManageByCondition(@ModelAttribute PageVo pageVo,
                                                                                 @ModelAttribute SampleItemInfoSearchVo sv) {
        if(sv.getSampTaskId() == null) {
            return ResultUtil.validateError("采样任务ID不能为空!");
        }
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setSampStatus("3");
        IPage<SampleItemInfoVo> page = sampleItemService.findAuditSampItemForManageByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 采样样品审核列表（已审核）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findHisAuditSampItemForManageByCondition")
    public Result<IPage<SampleItemInfoVo>> findHisAuditSampItemForManageByCondition(@ModelAttribute PageVo pageVo,
                                                                                 @ModelAttribute SampleItemInfoSearchVo sv) {
        if(sv.getSampTaskId() == null) {
            return ResultUtil.validateError("采样任务ID不能为空!");
        }
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setSampStatus(null);
        IPage<SampleItemInfoVo> page = sampleItemService.findAuditSampItemForManageByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 采样确认任务列表查询(状态处于“采样中”的)
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findSampleTaskForLeaderByCondition")
    public Result<IPage<SampleTaskListVo>> findSampleTaskForLeaderByCondition(@ModelAttribute PageVo pageVo,
                                                                             @ModelAttribute SampleTaskSearchVo sv) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        sv.setSampTaskStatus("2");
        IPage<SampleTaskListVo> page = sampleItemService.findSampleTaskForLeaderByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 采样样品确认列表查询（“待确认”）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findAuditSampItemForLeaderByCondition")
    public Result<IPage<SampleItemInfoVo>> findAuditSampItemForLeaderByCondition(@ModelAttribute PageVo pageVo,
                                                                                    @ModelAttribute SampleItemInfoSearchVo sv) {
        if(sv.getSampTaskId() == null) {
            return ResultUtil.validateError("采样任务ID不能为空!");
        }
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setSampStatus("2");
        IPage<SampleItemInfoVo> page = sampleItemService.findAuditSampItemForLeaderByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 采样样品确认列表查询（“已确认”）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findHisAuditSampItemForLeaderByCondition")
    public Result<IPage<SampleItemInfoVo>> findHisAuditSampItemForLeaderByCondition(@ModelAttribute PageVo pageVo,
                                                                                 @ModelAttribute SampleItemInfoSearchVo sv) {
        if(sv.getSampTaskId() == null) {
            return ResultUtil.validateError("采样任务ID不能为空!");
        }
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setSampStatus(null);
        IPage<SampleItemInfoVo> page = sampleItemService.findAuditSampItemForLeaderByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * 采样组长确认（单个）
     * @param tdo
     * @return
     */
    @PostMapping("/doAuditSampItemForLeader")
    public Result<Object> doAuditSampItemForLeader(@Validated @RequestBody SampleItemAuditTdo tdo) {
        return sampleItemService.doAuditSampItemForLeader(tdo);
    }
    /**
     * 采样组长确认（批量）
     * @param tdo
     * @return
     */
    @PostMapping("/batchDoAuditSampItemForLeader")
    public Result<Object> batchDoAuditSampItemForLeader(@Validated  @RequestBody SampleItemAuditTdo tdo) {
        return sampleItemService.batchDoAuditSampItemForLeader(tdo);
    }
    /**
     * 采样负责人审批（单个）
     * @param tdo
     * @return
     */
    @PostMapping("/doAuditSampItemForManager")
    public Result<Object> doAuditSampItemForManager(@Validated  @RequestBody SampleItemAuditTdo tdo) {
        return sampleItemService.doAuditSampItemForManager(tdo);
    }
    /**
     * 采样负责人审批（批量）
     * @param tdo
     * @return
     */
    @PostMapping("/batchDoAuditSampItemForManager")
    public Result<Object> batchDoAuditSampItemForManager(@Validated  @RequestBody SampleItemAuditTdo tdo) {
        return sampleItemService.batchDoAuditSampItemForManager(tdo);
    }

}
