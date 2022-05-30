package org.hj.chain.platform.schedule.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.entity.ScheduleTask;
import org.hj.chain.platform.schedule.service.IScheduleTaskService;
import org.hj.chain.platform.tdo.schedule.ScheduleJobTdo;
import org.hj.chain.platform.vo.offer.OfferPlanFactorVo;
import org.hj.chain.platform.vo.schedule.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */

@RestController
@RequestMapping("/scheduleTask")
public class ScheduleTaskController {
    @Autowired
    private IScheduleTaskService scheduleTaskService;

    /**
     * TODO 分页查询任务调度列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findByCondition")
    public Result<IPage<ScheduleTaskVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                         @ModelAttribute ScheduleTaskSearchVo sv) {
        sv.setContCode(StrUtil.trimToNull(sv.getContCode()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        IPage<ScheduleTaskVo> page = scheduleTaskService.findByCondition(pageVo,sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 分页查询任务调度列表（历史）
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findHistoryByCondition")
    public Result<IPage<ScheduleTaskVo>> findHistoryByCondition(@ModelAttribute PageVo pageVo,
                                                         @ModelAttribute ScheduleTaskSearchVo sv) {
        sv.setContCode(StrUtil.trimToNull(sv.getContCode()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        sv.setScheduleStatus("2");
        IPage<ScheduleTaskVo> page = scheduleTaskService.findByCondition(pageVo,sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 根据taskId查询调度任务job
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findJobForTask")
    public Result<IPage<ScheduleJobVo>> findJobForTask(@ModelAttribute PageVo pageVo,
                                                       @ModelAttribute ScheduleJobSearchVo sv) {
        if(sv.getTaskId() == null) {
            return ResultUtil.validateError("任务调度ID不能为空！");
        }
        IPage<ScheduleJobVo> page = scheduleTaskService.findJobForTask(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * 根据taskId查询暂存的任务
     * @param taskId
     * @return
     */
    @GetMapping("/findJobByTaskId/{taskId}")
    public Result<ScheduleJobVo> findJobByTaskId(@PathVariable Long taskId) {
        if(taskId == null) {
            return ResultUtil.validateError("任务调度ID不能为空！");
        }
        return scheduleTaskService.findJobByTaskId(taskId);
    }

    /**
     * TODO 查询任务下检测因子信息
     * @param jobId
     * @return
     */
    @GetMapping("/{jobId}/jobFactors")
    public Result<List<ScheduleJobFactorVo>> getJobFactorsByJobId(@PathVariable String jobId) {
        if(StrUtil.isBlank(jobId)) {
            return ResultUtil.validateError("任务号不能为空！");
        }
        List<ScheduleJobFactorVo> jobFactorVos = scheduleTaskService.getJobFactorsByJobId(jobId);
        return ResultUtil.data(jobFactorVos);
    }

    /**
     * TODO 根据任务调度ID查询待调度计划
     * @param taskId
     * @return
     */
    @GetMapping("/{taskId}/findOfferPlan")
    public Result<List<ScheduleOfferPlanVo>> findOfferPlanByTaskId(@PathVariable Long taskId) {
        if(taskId == null) {
            return ResultUtil.validateError("任务调度ID不能为空！");
        }
        ScheduleTask task = scheduleTaskService.getById(taskId);
        if(task == null) {
            return ResultUtil.validateError("任务调度信息不存在！");
        }
        List<ScheduleOfferPlanVo> offerPlanVos = scheduleTaskService.findOfferPlanByOfferId(task.getOfferId());
        return ResultUtil.data(offerPlanVos);
    }

    /**
     * 查询监测计划下因子信息
     * @param offerPlanId
     * @return
     */
    @GetMapping("/{offerPlanId}/findFactors")
    public Result<List<OfferPlanFactorVo>> findFactorsByOfferPlanId(@PathVariable Long offerPlanId) {
        if(offerPlanId == null) {
            return ResultUtil.validateError("监测计划ID不能为空！");
        }
        List<OfferPlanFactorVo> factorVos = scheduleTaskService.findFactorsByOfferPlanId(offerPlanId);
        return ResultUtil.data(factorVos);
    }

    /**
     * TODO 查询多个监测计划下因子信息
     * @param offerPlanId
     * @return
     */
    @GetMapping("/findFactorsByOfferPlanIds")
    public Result<List<OfferPlanFactorVo>> findFactorsByOfferPlanIds(@RequestParam String offerPlanId) {
        if(StrUtil.isBlank(offerPlanId)) {
            return ResultUtil.validateError("检测计划ID不能为空！");
        }
        List<Long> offerPlanIds = Arrays.stream(offerPlanId.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
        List<OfferPlanFactorVo> factorVos = scheduleTaskService.findFactorsByOfferPlanIds(offerPlanIds);
        return ResultUtil.data(factorVos);
    }

    /**
     * TODO 保存调度任务(暂存)
     * @param tdo
     * @return
     */
    @PostMapping("/saveScheduleJob")
    public Result<Object> saveScheduleJob(@Validated @RequestBody ScheduleJobTdo tdo) {
        return scheduleTaskService.doScheduleJob(tdo, "0");
    }

    /**
     * TODO 确认调度任务
     * @param tdo
     * @return
     */
    @PostMapping("/confirmScheduleJob")
    public Result<Object> confirmScheduleJob(@Validated @RequestBody ScheduleJobTdo tdo) {
        return scheduleTaskService.doScheduleJob(tdo, "1");
    }

    /**
     * 任务列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findJobsByCondition")
    public Result<IPage<ScheduleJobVo>> findJobsByCondition(@ModelAttribute PageVo pageVo,
                                                            @ModelAttribute ScheduleJobSearchVo sv) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        IPage<ScheduleJobVo> page = scheduleTaskService.findJobsByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }
}
