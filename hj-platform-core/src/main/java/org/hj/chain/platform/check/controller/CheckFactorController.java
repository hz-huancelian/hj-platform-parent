package org.hj.chain.platform.check.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.check.service.ICheckFactorInfoService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.check.BatchCheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorInfoTdo;
import org.hj.chain.platform.vo.check.CheckFactorInfoVo;
import org.hj.chain.platform.vo.check.CheckFactorSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 检测列表管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@RestController
@RequestMapping(value = "/checkFactor")
public class CheckFactorController {
    @Autowired
    private ICheckFactorInfoService checkFactorInfoService;

    /**
     * TODO 待审批的检测列表
     * @Author chh
     * @param pageVo
     * @param sv
     * @Date 2021-05-15 17:41
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findAuditCheckFactorByCondition", method = RequestMethod.GET)
    public Result<IPage<CheckFactorInfoVo>> findAuditCheckFactorByCondition(@ModelAttribute PageVo pageVo,
                                                                            @ModelAttribute CheckFactorSearchVo sv) {
        sv.setCheckStatus("4");
        String reqPath = "check:laboratory:approve";
        return checkFactorInfoService.findAuditCheckFactorByCondition(pageVo, sv, reqPath);
    }

    /**
     * TODO 录入因子检测数据
     * @Author chh
     * @param tdo
     * @Date 2021-05-10 0:40
     * @Iteration 1.0
     */
    @RequestMapping(value = "/saveCheckFactorData", method = RequestMethod.POST)
    public Result<Object> saveCheckFactorData(@Validated @RequestBody CheckFactorInfoTdo tdo) {
        return checkFactorInfoService.saveCheckFactorData(tdo);
    }

    /**
      * TODO 检测列表提交审核
      * @Author chh
      * @param checkFactorId
      * @Date 2021-05-15 22:24 检测列表ID
      * @Iteration 1.0
      */
    @RequestMapping(value = "/submitCheckFactor", method = RequestMethod.GET)
    public Result<Object> submitCheckFactor(@RequestParam Long checkFactorId) {
        return checkFactorInfoService.submitCheckFactor(checkFactorId);
    }

    /**
      * TODO 检测负责人审核检测列表（单个）
      * @Author chh
      * @param tdo
      * @Date 2021-05-15 23:48
      * @Iteration 1.0
      */
    @RequestMapping(value = "/auditCheckFactor", method = RequestMethod.POST)
    public Result<Object> auditCheckFactor(@Validated @RequestBody CheckFactorAuditTdo tdo) {
        return checkFactorInfoService.auditCheckFactor(tdo);
    }

    /**
     * TODO 检测员单个申请领样
     * @Author chh
     * @param checkFactorId 检测列表ID
     * @Date 2021-05-09 23:23
     * @Iteration 1.0
     */
    @RequestMapping(value = "/sampDrawApply", method = RequestMethod.POST)
    public Result<Object> sampDrawApply(@RequestParam Long checkFactorId) {
        return checkFactorInfoService.sampDrawApply(checkFactorId);
    }

    /**
     * todo 检测负责人审核检测列表（批量）
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/batchAuditCheckFactor", method = RequestMethod.POST)
    public Result<Object> batchAuditCheckFactor(@Validated @RequestBody BatchCheckFactorAuditTdo tdo) {
        return checkFactorInfoService.batchAuditCheckFactor(tdo);
    }

    /**
     * TODO 检测员批量申请领样
     * @param checkFactorId 检测列表ID ","拼接
     * @return
     */
    @PostMapping("/batchSampDrawApply")
    public Result<Object> batchSampDrawApply(@RequestParam String checkFactorId) {
        return checkFactorInfoService.batchSampDrawApply(checkFactorId);
    }
}
