package org.hj.chain.platform.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.report.ReportInfoAuditTdo;
import org.hj.chain.platform.vo.report.*;
import org.hj.chain.platform.word.ReportTableData;
import org.springframework.web.multipart.MultipartFile;

public interface IReportService {


    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/23 7:37 下午
     */
    IPage<ReportVo> findReportInfosByCondition(PageVo pageVo, ReportSearchVo sv);


    /**
     * TODO 分页查询历史
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/5 10:26 下午
     */
    IPage<ReportVo> findHisReportInfosByCondition(PageVo pageVo, ReportSearchVo sv);

    Result<Object> submitReport(Long id);

    Result<Object> doAuditReport(ReportInfoAuditTdo tdo);

    Result<Object> doSignReport(ReportInfoAuditTdo tdo);


    /**
     * TODO 根据 报告编号查看报告信息
     *
     * @param reportCode
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/25 4:46 下午
     */
    ReportGenDataVo findReportDataByReportCode(String reportCode);

    /**
     * TODO 生成报告
     *
     * @param reportCode
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/26 4:16 下午
     */
    Result<Object> genReport(String reportCode);

    /**
     * TODO 上传文件
     *
     * @param file
     * @param reportId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/27 11:15 上午
     */
    Result<Object> uploadReportFileById(MultipartFile file, Long reportId);


    /**
     * TODO 报告审核列表
     *
     * @param page
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/12 11:39 上午
     */
    IPage<ReportCheckVo> findReportCheckByPage(IPage<ReportCheckVo> page, String jobId, String projectName);


    /**
     * TODO 报告签发列表
     *
     * @param page
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/12 11:39 上午
     */
    IPage<ReportSignVo> findReportSignByPage(IPage<ReportSignVo> page, String jobId, String projectName);
}
