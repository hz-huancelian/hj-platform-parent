package org.hj.chain.platform.report.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.report.service.IReportService;
import org.hj.chain.platform.tdo.report.ReportInfoAuditTdo;
import org.hj.chain.platform.vo.report.ReportCheckVo;
import org.hj.chain.platform.vo.report.ReportSearchVo;
import org.hj.chain.platform.vo.report.ReportSignVo;
import org.hj.chain.platform.vo.report.ReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告交互
 * @Iteration : 1.0
 * @Date : 2021/5/23  7:43 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private IReportService reportService;

    /**
     * TODO 分页查询报告列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:29 下午
     */
    @RequestMapping(value = "/findReportInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<ReportVo>> findReportInfosByCondition(@ModelAttribute PageVo pageVo,
                                                              @ModelAttribute ReportSearchVo sv) {

        sv.setJobId(StrUtil.trimToNull(sv.getJobId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setReportCode(StrUtil.trimToNull(sv.getReportCode()))
                .setReportStatus(StrUtil.trimToNull(sv.getReportStatus()));
        IPage<ReportVo> page = reportService.findReportInfosByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 分页查询历史报告列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:29 下午
     */
    @RequestMapping(value = "/findHisReportInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<ReportVo>> findHisReportInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                 @ModelAttribute ReportSearchVo sv) {

        sv.setJobId(StrUtil.trimToNull(sv.getJobId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setReportCode(StrUtil.trimToNull(sv.getReportCode()))
                .setReportStatus("6");
        IPage<ReportVo> page = reportService.findHisReportInfosByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 分页查询审核列表
     *
     * @param pageVo
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/30 4:19 下午
     */
    @RequestMapping(value = "/findReportCheckByPage", method = RequestMethod.GET)
    public Result<IPage<ReportCheckVo>> findReportCheckByPage(@ModelAttribute PageVo pageVo,
                                                              @RequestParam String jobId,
                                                              @RequestParam String projectName) {

        Page<ReportCheckVo> page = PageUtil.initMpPage(pageVo);
        reportService.findReportCheckByPage(page, jobId, projectName);
        return ResultUtil.data(page);
    }


    /**
     * TODO 分页查询签发列表
     *
     * @param pageVo
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/30 4:21 下午
     */
    @RequestMapping(value = "/findReportSignByPage", method = RequestMethod.GET)
    public Result<IPage<ReportSignVo>> findReportSignByPage(@ModelAttribute PageVo pageVo,
                                                            @RequestParam String jobId,
                                                            @RequestParam String projectName) {

        Page<ReportSignVo> page = PageUtil.initMpPage(pageVo);
        reportService.findReportSignByPage(page, jobId, projectName);
        return ResultUtil.data(page);
    }

    /**
     * TODO 报告提交审批
     *
     * @param id
     * @Author: chh
     * @Iteration : 1.0
     * @Date: 2021/5/24 0:34
     */
    @RequestMapping(value = "/submitReport", method = RequestMethod.POST)
    public Result<Object> submitReport(@RequestParam Long id) {
        return reportService.submitReport(id);
    }

    /**
     * TODO 报告审批
     *
     * @param tdo
     * @Author: chh
     * @Iteration : 1.0
     * @Date: 2021/5/24 0:49
     */
    @RequestMapping(value = "/doAuditReport", method = RequestMethod.POST)
    public Result<Object> doAuditReport(@Validated @RequestBody ReportInfoAuditTdo tdo) {
        return reportService.doAuditReport(tdo);
    }

    /**
     * TODO 部门经理签发报告
     *
     * @param tdo
     * @Author: chh
     * @Iteration : 1.0
     * @Date: 2021/5/24 0:49
     */
    @RequestMapping(value = "/doSignReport", method = RequestMethod.POST)
    public Result<Object> doSignReport(@Validated @RequestBody ReportInfoAuditTdo tdo) {
        return reportService.doSignReport(tdo);
    }

    /**
     * TODO 生成报告
     *
     * @param reportCode
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/25 11:46 下午
     */
    @RequestMapping(value = "/genReport/{reportCode}", method = RequestMethod.GET)
    public Result<Object> genReport(@PathVariable String reportCode) {
        if (StrUtil.isBlank(reportCode)) {
            return ResultUtil.busiError("报告编号不能为空！");
        }

        return reportService.genReport(reportCode);
    }


    /**
     * TODO 报告上传，并更新状态
     *
     * @param file
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/27 10:59 上午
     */
    @RequestMapping(value = "/uploadReportFile", method = RequestMethod.POST)
    public Result<Object> uploadReportFile(@RequestParam("file") MultipartFile file, @RequestParam Long reportId) {

        if (file == null) {
            return ResultUtil.busiError("请上传文件！");
        }

        if (reportId == null) {
            return ResultUtil.validateError("报告ID不能为空");
        }

        Result<Object> result = reportService.uploadReportFileById(file, reportId);
        return result;
    }
}