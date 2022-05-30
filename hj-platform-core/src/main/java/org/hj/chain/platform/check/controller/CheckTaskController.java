package org.hj.chain.platform.check.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.service.ICheckTaskService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.vo.check.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 检测任务管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-14
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-14
 */
@RestController
@RequestMapping("/checkTask")
public class CheckTaskController {
    @Autowired
    private ICheckTaskService checkTaskInfoService;

    /*                      检测管理-任务分配                          */
    /**
      * TODO 分页查询检测任务列表(待分配)
      * @Author chh
      * @param pageVo
      * @param sv
      * @Date 2021-05-14 16:46
      * @Iteration 1.0
      */
    @RequestMapping(value = "/findByCondition", method = RequestMethod.GET)
    public Result<IPage<CheckTaskInfoVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                          @ModelAttribute CheckTaskInfoSearchVo sv) {
        String reqPath = "check:laboratory:task";
        sv.setCheckTaskStatus("0");
        return checkTaskInfoService.findByCondition(pageVo, sv, reqPath);
    }

    /**
     * TODO 分页查询检测任务列表(已分配)
     * @Author chh
     * @param pageVo
     * @param sv
     * @Date 2021-05-14 16:46
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findHistoryByCondition", method = RequestMethod.GET)
    public Result<IPage<CheckTaskInfoVo>> findHistoryByCondition(@ModelAttribute PageVo pageVo,
                                                          @ModelAttribute CheckTaskInfoSearchVo sv) {
        String reqPath = "check:laboratory:task";
        return checkTaskInfoService.findByCondition(pageVo, sv, reqPath);
    }

    /**
      * TODO 根据检测任务号查询检测列表
      * @Author chh
      * @param pageVo 分页
      * @param sv 检索条件
      * @param checkTaskId 检测任务号
      * @Date 2021-05-15 19:12
      * @Iteration 1.0
      */
    @RequestMapping(value = "/getCheckFactorInfos", method = RequestMethod.GET)
    public Result<IPage<CheckFactorInfoVo>> getCheckFactorByCondition(@ModelAttribute PageVo pageVo,
                                                                   @ModelAttribute CheckFactorSearchVo sv,
                                                                   @RequestParam Long checkTaskId) {
        return checkTaskInfoService.getCheckFactorByCondition(pageVo, sv, checkTaskId);
    }

    /**
     * TODO 根据检测任务查询检测因子
     * @Author chh
     * @param checkTaskId 检测任务ID
     * @Date 2021-05-11 1:22
     * @Iteration 1.0
     */
    @RequestMapping(value = "/getOfferFactorsByTaskId/{checkTaskId}", method = RequestMethod.GET)
    public Result<List<CheckTaskFactorVo>> getOfferFactorsByTaskId(@PathVariable Long checkTaskId) {
        return checkTaskInfoService.getOfferFactorsByTaskId(checkTaskId);
    }

    /**
      * TODO 因子分配
      * @Author chh
      * @param param {"checkTaskId":"", "params":[{"checkUser":"检测员", "jobPlanFactorId":"报价单因子ID"}]}
      * @Date 2021-05-14 16:56
      * @Iteration 1.0
      */
    @RequestMapping(value = "/assignmentsFactor", method = RequestMethod.POST)
    public Result<Object> assignmentsFactor(@RequestParam String param) {
        return checkTaskInfoService.assignmentsFactor(param);
    }

    /**
     * TODO 检测员列表
     * @Author chh
     * @Date 2021-05-14 16:56
     * @Iteration 1.0
     */
    @RequestMapping(value = "/getCheckUsers", method = RequestMethod.GET)
    public Result<List<CheckUserVo>> getCheckUsers() {
        return checkTaskInfoService.getCheckUsers();
    }

    /**
     * 检测列表
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findCheckTaskByCondition", method = RequestMethod.GET)
    public Result<IPage<CheckTaskVo>> findCheckTaskByCondition(@ModelAttribute PageVo pageVo,
                                                               @ModelAttribute CheckTaskSearchVo sv) {
        String reqPath = "check:laboratory:list";
        return checkTaskInfoService.findCheckTaskByCondition(pageVo, sv, reqPath);
    }

    /**
     * 检测列表明细 - 待录入因子列表
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findCheckTaskDetailByCondition0", method = RequestMethod.GET)
    public Result<IPage<CheckFactorInfoVo>> findCheckTaskDetailByCondition0(@ModelAttribute PageVo pageVo,
                                                            @Validated @ModelAttribute CheckFactorSearchVo sv) {
        if(sv.getCheckTaskId() == null) {
            return ResultUtil.validateError("检测任务ID不能为空！");
        }
        String reqPath = "check:laboratory:list";
        return checkTaskInfoService.findCheckTaskDetailByCondition(pageVo, sv, reqPath, "0");
    }

    /**
     * 检测列表明细 - 已录入因子列表
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findCheckTaskDetailByCondition1", method = RequestMethod.GET)
    public Result<IPage<CheckFactorInfoVo>> findCheckTaskDetailByCondition1(@ModelAttribute PageVo pageVo,
                                                                           @Validated @ModelAttribute CheckFactorSearchVo sv) {
        if(sv.getCheckTaskId() == null) {
            return ResultUtil.validateError("检测任务ID不能为空！");
        }
        String reqPath = "check:laboratory:list";
        return checkTaskInfoService.findCheckTaskDetailByCondition(pageVo, sv, reqPath, "1");
    }
}
