package org.hj.chain.platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.annotation.CustomParam;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.service.ISamplerService;
import org.hj.chain.platform.tdo.MobileSampItemDetailTdo;
import org.hj.chain.platform.tdo.MobileSampItemReviewTdo;
import org.hj.chain.platform.tdo.MobileSampleAuditTdo;
import org.hj.chain.platform.tdo.MobileSignTdo;
import org.hj.chain.platform.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 采样管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-05
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-05
 */
@RequestMapping(value = "/sample")
@RestController
public class SampleController {
    @Autowired
    private ISamplerService samplerService;

    /**
      * TODO 采样组长-任务列表
      * @Author chh
      * @param pageVo
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/teamLeader/taskList", method = RequestMethod.GET)
    public Result<IPage<MobileSampleTaskVo>> getSampleTasksForTeamLeader(@ModelAttribute PageVo pageVo) {
        String userId = (String) StpUtil.getLoginId();
        IPage<MobileSampleTaskVo> vos = samplerService.getSampleTasksForTeamLeader(pageVo, userId);
        return ResultUtil.data(vos);
    }
    /**
      * TODO 根据采样任务ID获取样品列表
      * @Author chh
      * @param taskId
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/items/{taskId}", method = RequestMethod.GET)
    public Result<SampleTaskItemVo> getSampleItemsBySampleTaskId(@PathVariable Long taskId) {
        String userId = (String) StpUtil.getLoginId();
        return samplerService.getSampleItemsBySampleTaskId(taskId, userId);
    }

    /**
     * TODO 采样组长待办列表：0-待确认 1-待入库 2-已完成
     * @return
     */
    @GetMapping("/teamLeader/getSampleTasksByCondition")
    public Result<IPage<SampleTaskVo>> getSampleTasksByCondition(@ModelAttribute PageVo pageVo, @RequestParam String sampleStatus) {
        return samplerService.getSampleTasksByCondition(pageVo, sampleStatus);
    }

    /**
     * TODO 采样组长待办列表-样品列表
     * @param taskId 采样任务ID
     * @param sampleStatus 0-待确认 1-待入库 2-已完成
     * @return
     */
    @GetMapping("/teamLeader/getSampleListByCondition")
    public Result<SampleDbTaskItemVo> getSampleListByCondition(@RequestParam Long taskId, @RequestParam String sampleStatus) {
        if(taskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        if(StrUtil.isBlank(sampleStatus)) {
            return ResultUtil.validateError("样品状态不能为空！");
        }
        return samplerService.getSampleListByCondition(taskId, sampleStatus);
    }

    /**
     * TODO 采样组长待办列表 - 已驳回
     * @return
     */
    @GetMapping("/teamLeader/getRejectedSampleList")
    public Result<IPage<SampleListVo>> getRejectedSampleList(@ModelAttribute PageVo pageVo) {
        return samplerService.getRejectedSampleList(pageVo);
    }

    /**
     * 根据样品ID查询审核历史
     * @param sampItemId
     * @Date 2022-03-17
     * @Iteration 3.0
     */
    @GetMapping("/getSampleAuditRecordBySampItemId/{sampItemId}")
    public Result<List<SampleAuditRecordVo>> getSampleAuditRecordBySampItemId(@PathVariable Long sampItemId) {
        if(sampItemId == null) {
            return ResultUtil.validateError("样品ID不能为空！");
        }
        return samplerService.getSampleAuditRecordBySampItemId(sampItemId);
    }

    /**
     * TODO 样品编号校验
     * @param sampleNo
     * @return
     */
    @GetMapping("/checkSampleNo/{sampleNo}")
    public Result<Object> checkSampleNo(@PathVariable String sampleNo) {
        return samplerService.checkSampleNo(sampleNo);
    }

    /**
     * TODO 扫码(输入样品编号)查询样品详情
     * @Author chh
     * @param sampleNo
     * @Date 2022-03-17
     * @Iteration 3.0
     * @return SampleItemVo
     */
    @RequestMapping(value = "/getSampleDetailBySampleNo/{sampleNo}", method = RequestMethod.GET)
    public Result<SampleItemVo> getSampleDetailBySampleNo(@PathVariable String sampleNo) {
        return samplerService.getSampleDetailBySampleNo(sampleNo);
    }

    /**
      * TODO 根据样品ID获取样品详情
      * @Author chh
      * @param sampItemId
      * @Date 2022-03-17
      * @Iteration 3.0
      * @return
      */
    @RequestMapping(value = "/detail/{sampItemId}", method = RequestMethod.GET)
    public Result<SampleItemVo> getSampleDetailBySampItemId(@PathVariable Long sampItemId) {
        return samplerService.getSampleDetailBySampItemId(sampItemId);
    }

    /**
     * TODO 采样员获取样品列表
     * @param pageVo
     * @param sampleStatus 0-待提交 1-已驳回
     * @return
     */
    @GetMapping("/getSampleItemsForCollectUser")
    public Result<IPage<SampleListVo>> getSampleItemsForCollectUser(@ModelAttribute PageVo pageVo, @RequestParam String sampleStatus) {
        if(StrUtil.isBlank(sampleStatus)) {
            return ResultUtil.validateError("样品状态不能为空！");
        }
        return samplerService.getSampleItemsForCollectUser(pageVo, sampleStatus);
    }

    /**
     * TODO 采样员待复核任务列表 (只看有自己待复核样品的任务)
     * @param pageVo
     * @return
     */
    @GetMapping("/getSampleTasksForCollectUser")
    public Result<IPage<SampleTaskVo>> getSampleTasksForCollectUser(@ModelAttribute PageVo pageVo) {
        return samplerService.getSampleTasksForCollectUser(pageVo);
    }

    /**
     * TODO 采样员带复核样品信息
     * @param taskId
     * @return
     */
    @GetMapping("/getSampleItemsForCollectUserByTaskId")
    public Result<SampleDbTaskItemVo> getSampleItemsForCollectUserByTaskId(@RequestParam Long taskId) {
        if(taskId == null) {
            return ResultUtil.validateError("采样任务ID不能为空！");
        }
        return samplerService.getSampleItemsForCollectUserByTaskId(taskId);
    }

    /**
      * TODO 采样组长确认样品（单个/批量）
      * @Author chh
      * @param auditVo
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/auditSampleItem", method = RequestMethod.POST)
    public Result<Object> auditSampleItem(@RequestBody @Valid MobileSampleAuditTdo auditVo) {
        return samplerService.auditSampleItem(auditVo);
    }

    /**
      * TODO 采样组长提交样品入库申请
      * @Author chh
      * @param sampleItemId 样品ID，单个以","结尾；批量以","拼接
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/sampStoreApply", method = RequestMethod.POST)
    public Result<Object> sampStoreApply(@CustomParam String sampleItemId) {
        return samplerService.sampStoreApply(sampleItemId);
    }

    /**
      * TODO 采样员保存样品采集数据
      * @Author chh
      * @param detailTDo
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/saveSampItemDetail", method = RequestMethod.POST)
    public Result<Object> saveSampItemDetail(@Validated @RequestBody MobileSampItemDetailTdo detailTDo) {
        return samplerService.saveSampItemDetail(detailTDo);
    }

    /**
      * TODO 采样员提交样品复核
      * @Author chh
      * @param sampleItemId 样品ID
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/submitSampItemDetail", method = RequestMethod.POST)
    public Result<Object> submitSampItemDetail(@CustomParam Long sampleItemId) {
        return samplerService.submitSampItemDetail(sampleItemId);
    }

    /**
      * TODO 采样组长签名
      * @Author chh
      * @param tdo
      * @Date 2022-03-17
      * @Iteration 3.0
      */
    @RequestMapping(value = "/doSignForSamp", method = RequestMethod.POST)
    public Result<Object> doSignForSamp(@RequestBody MobileSignTdo tdo) {
        return samplerService.doSignForSamp(tdo);
    }


    /**
     * 现场采样设备列表查询
     * @return
     */
    @RequestMapping(value = "/listSampEquipments/{sampItemId}", method = RequestMethod.GET)
    public Result<List<EquipmentInfoVo>> listSampEquipments(@PathVariable String sampItemId) {
        return samplerService.listSampEquipments(sampItemId);
    }

    /**
     * 现场检测设备列表
     * @param sampItemId
     * @return
     */
    @RequestMapping(value = "/listCheckEquipments/{sampItemId}", method = RequestMethod.GET)
    public Result<List<EquipmentInfoVo>> listCheckEquipments(@PathVariable String sampItemId) {
        return samplerService.listCheckEquipments(sampItemId);
    }

    /**
     * 现场检测设备树
     * @return
     */
    @GetMapping("/checkEquimentTree")
    public Result<JSONArray> checkEquimentTree() {
        return samplerService.checkEquimentTree();
    }

    /**
     * 校准设备列表
     * @param sampItemId
     * @return
     */
    @RequestMapping(value = "/listCalibrationEquipments/{sampItemId}", method = RequestMethod.GET)
    public Result<List<EquipmentInfoVo>> listCalibrationEquipments(@PathVariable String sampItemId) {
        return samplerService.listCalibrationEquipments(sampItemId);
    }

    /**
     * 校准设备树
     * @return
     */
    @GetMapping("/calibrationEquipmentTree")
    public Result<JSONArray> calibrationEquipmentTree() {
        return samplerService.calibrationEquipmentTree();
    }

    /**
     * todo 复核人员列表
     * @return
     */
    @RequestMapping(value = "/listReviewUsers", method = RequestMethod.GET)
    public Result<List<UserParamVo>> listReviewUsers() {
        return samplerService.listReviewUsers();
    }


    /**
     * todo 样品复核
     * @return
     */
    @RequestMapping(value = "/reviewSampleItem", method = RequestMethod.POST)
    public Result<Object> reviewSampleItem(@Validated @RequestBody MobileSampItemReviewTdo tdo) {
        return samplerService.reviewSampleItem(tdo);
    }

    /**
     * TODO 一键复核通过
     * @param sampleItemId 样品ID”,”拼接
     * @return
     */
    @PostMapping("/batchReviewSampleItem")
    public Result<Object> batchReviewSampleItem(@CustomParam String sampleItemId) {
        if(StrUtil.isBlank(sampleItemId)) {
            return ResultUtil.validateError("操作失败，至少包含一样样品！");
        }
        return samplerService.batchReviewSampleItem(sampleItemId);
    }
}
