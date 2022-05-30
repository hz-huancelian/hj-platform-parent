package org.hj.chain.platform.sample.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.entity.SampleList;
import org.hj.chain.platform.sample.service.ISampleTaskService;
import org.hj.chain.platform.vo.sample.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@RestController
@RequestMapping("/sampTask")
public class SampleTaskController {

    @Autowired
    private ISampleTaskService sampleTaskService;

    /**
     * TODO 分页查询采样任务列表 (待分配任务)
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findByCondition")
    public Result<IPage<SampleTaskVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                       @ModelAttribute SampleTaskSearchVo sv) {
        String reqPath = "sample:task:list";
        return sampleTaskService.findByCondition(pageVo, sv, reqPath);
    }

    /**
     * TODO 分页查询采样任务列表 (历史任务)
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findHistoryByCondition")
    public Result<IPage<SampleTaskVo>> findHistoryByCondition(@ModelAttribute PageVo pageVo,
                                                              @ModelAttribute SampleTaskSearchVo sv) {
        sv.setSampTaskStatus("3");
        String reqPath = "sample:task:list";
        return sampleTaskService.findByCondition(pageVo, sv, reqPath);
    }

    /**
     * TODO 根据任务单号查询检测因子(自检)
     * @Author chh
     * @param sv 任务单号、因子名称、检测位置
     * @Date 2022-03-09 1:22
     * @Iteration 3.0
     */
    @GetMapping("/getZjJobFactorsByJobId")
    public Result<List<JobFactorVo>> getZjJobFactorsByTaskId(@Validated @ModelAttribute JobFactorSearchVo sv) {
        if(StrUtil.isBlank(sv.getJobId())) {
            return ResultUtil.validateError("任务单号不能为空！");
        }
        sv.setFbFlag("0").setHyFlag("0");
        List<JobFactorVo> factorVos = sampleTaskService.getJobFactorsByCondition(sv);
        return ResultUtil.data(factorVos);
    }

    /**
     * TODO 根据任务单号查询检测因子（外包）
     * @Author chh
     * @param sv 任务单号、因子名称、检测位置
     * @Date 2022-03-09 1:22
     * @Iteration 3.0
     */
    @GetMapping("/getWbJobFactorsByJobId")
    public Result<List<JobFactorVo>> getWbJobFactorsByTaskId(@Validated @ModelAttribute JobFactorSearchVo sv) {
        if(StrUtil.isBlank(sv.getJobId())) {
            return ResultUtil.validateError("任务单号不能为空！");
        }
        sv.setFbFlag("1").setHyFlag("0");
        List<JobFactorVo> factorVos = sampleTaskService.getJobFactorsByCondition(sv);
        return ResultUtil.data(factorVos);
    }

    /**
     * TODO 查询任务因子点位列表
     * @param jobId
     * @return
     */
    @GetMapping("/getFactorPointsBySampTaskId/{jobId}")
    public Result<List<String>> getFactorPointsByJobId(@PathVariable String jobId) {
        if(StrUtil.isBlank(jobId)) {
            return ResultUtil.validateError("任务单号不能为空！");
        }
        List<String> factorPoints = sampleTaskService.getFactorPointsByJobId(jobId);
        return ResultUtil.data(factorPoints);
    }



    /**
     * TODO 根据任务单号获取因子信息(任务详情因子信息)
     * @Author chh
     * @param jobId
     * @Date 2022-03-09 1:22
     * @Iteration 3.0
     */
    @GetMapping("/getJobFactorsByJobId/{jobId}")
    public Result<List<JobFactorVo>> getJobFactorsById(@PathVariable String jobId) {
        if(StrUtil.isBlank(jobId)) {
            return ResultUtil.validateError("任务单号不能为空！");
        }
        List<JobFactorVo> factorVos = sampleTaskService.getJobFactorsByJobId(jobId);
        return ResultUtil.data(factorVos);
    }

    /**
     * TODO 查询采样任务下保存的合样列表
     * @param sampTaskId
     * @param fbFlag
     * @return
     */
    @GetMapping("/getSampleListByCondition")
    public Result<List<SampleList>> getSampleListByCondition(@RequestParam Long sampTaskId, @RequestParam String fbFlag){
        if(sampTaskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        if(StrUtil.isBlank(fbFlag)) {
            return ResultUtil.validateError("分包标志不能为空！");
        }
        List<SampleList> sampleLists = sampleTaskService.getSampleListByCondition(sampTaskId, fbFlag);
        return ResultUtil.data(sampleLists);
    }

    /**
     * TODO 保存合样列表
     * @param list
     * @return
     */
    @PostMapping("/saveSampleList")
    public Result<Object> saveSampleList(@RequestBody List<SampleList> list) {
        return sampleTaskService.saveSampleList(list);
    }

    /**
     * TODO 删除合样列表（单个），负责人可以任意删除，组长只能删除自己
     * @param sampleListId
     * @return
     */
    @DeleteMapping("/deleteSampleListById/{sampleListId}")
    public Result<Object> deleteSampleListById(@PathVariable Long sampleListId) {
        if(sampleListId == null) {
            return ResultUtil.validateError("合样列表ID不能为空！");
        }
        return sampleTaskService.deleteSampleListById(sampleListId);
    }

    /**
     * TODO 确认合样列表（1、采样任务所有因子全部完成合样；2、只允许采样负责人操作）
     * @param sampTaskId
     * @return
     */
    @PostMapping("/confirmSampleList")
    public Result<Object> confirmSampleList(@RequestParam Long sampTaskId) {
        if(sampTaskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        return sampleTaskService.confirmSampleList(sampTaskId);
    }

    /**
     * TODO 采样位置分配
     * @param param {"sampTaskId": ,"params":[{"factorPoint":"", "sampleUserId":""},{}]} sampleUserId多个用“，”拼接
     * @return
     */
    @PostMapping("/assignFactorPoint")
    public Result<Object> assignFactorPoint(@RequestParam String param) {
        if(StrUtil.isBlank(param)) {
            return ResultUtil.validateError("采样位置分配参数不能为空！");
        }
        return sampleTaskService.assignFactorPoint(param);
    }

    /**
     * TODO 采样位置分配信息查询
     * @param sampTaskId
     * @return
     */
    @GetMapping("/getSampleTaskPointBySampTaskId/{sampTaskId}")
    public Result<List<SampleTaskPointVo>> getSampleTaskPointBySampTaskId(@PathVariable Long sampTaskId) {
        if(sampTaskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        List<SampleTaskPointVo> vos = sampleTaskService.getSampleTaskPointBySampTaskId(sampTaskId);
        return ResultUtil.data(vos);
    }

    /**
     * TODO 保存合样备注
     * @param param {"sampTaskId" : ,"combinedRemark" : ""}
     * @return
     */
    @PostMapping("/saveCombinedRemark")
    public Result<Object> saveCombinedRemark(@RequestParam String param) {
        if(StrUtil.isBlank(param)) {
            return ResultUtil.validateError("合样备注参数不能为空！");
        }
        return sampleTaskService.saveCombinedRemark(param);
    }

    /**
     * 根据采样任务ID获取样品标签信息（确认合样后）
     * @param sampTaskId
     * @return
     */
    @GetMapping("/getSampleItemBySampTaskId")
    public Result<IPage<SampleTaskItemVo>> getSampleItemBySampTaskId(@ModelAttribute PageVo pageVo, @RequestParam Long sampTaskId) {
        if(sampTaskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        String reqPath = "sample:sample:list";
        IPage<SampleTaskItemVo> page = sampleTaskService.getSampleItemBySampTaskId(pageVo, sampTaskId, reqPath);
        return ResultUtil.data(page);
    }


    /**
     * TODO 采样列表
     * @param pageVo
     * @param sv
     * @return
     */
    @GetMapping("/findSampTaskByCondition")
    public Result<IPage<SampleTaskListVo>> findSampTaskByCondition(@ModelAttribute PageVo pageVo,
                                                             @ModelAttribute SampleTaskSearchVo sv) {
        String reqPath = "sample:sample:list";
        IPage<SampleTaskListVo> page = sampleTaskService.findSampTaskByCondition(pageVo, sv, reqPath);
        return ResultUtil.data(page);
    }

    /**
     * 采样列表-查看样品
     * @param pageVo
     * @param sv sv.sampTaskId不能为空
     * @return
     */
    @GetMapping("/findSampTaskDetailByCondition")
    public Result<IPage<SampleTaskDetailVo>> findSampTaskDetailByCondition(@ModelAttribute PageVo pageVo,
                                                                           @Validated @ModelAttribute SampleTaskDetailSearchVo sv) {
        IPage<SampleTaskDetailVo> page =  sampleTaskService.findSampTaskDetailByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


}
