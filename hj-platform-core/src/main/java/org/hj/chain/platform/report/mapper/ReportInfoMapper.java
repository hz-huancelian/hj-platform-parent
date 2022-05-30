package org.hj.chain.platform.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.vo.report.ReportBaseParam;
import org.hj.chain.platform.vo.report.ReportSearchVo;
import org.hj.chain.platform.vo.report.ReportVo;
import org.hj.chain.platform.vo.statistics.CompleteTaskVo;
import org.hj.chain.platform.vo.statistics.ReportIssueVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReportInfoMapper extends BaseMapper<ReportInfo> {


    /**
     * TODO 分页查询
     *
     * @param page
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/23 7:37 下午
     */
    IPage<ReportVo> findReportInfosByCondition(IPage<ReportVo> page, @Param("organId") String organId, @Param("sv") ReportSearchVo sv);

    /**
     * TODO 分页历史查询
     *
     * @param page
     * @param organId
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/5 10:31 下午
     */
    IPage<ReportVo> findHisReportInfosByCondition(IPage<ReportVo> page, @Param("organId") String organId, @Param("sv") ReportSearchVo sv);

    Integer getCount(@Param("organId") String organId, @Param("date") String date);

    CompleteTaskVo completeTaskForCurrMonth(String organId);

    CompleteTaskVo completeTaskForCurrYear(String organId);

    ReportIssueVo issueReportForCurrMonth(String organId);

    ReportIssueVo issueReportForCurrYear(String organId);

    int selectCountFinishedForCurrMonth(String organId);

    int selectCountFinishedForCurrYear(String organId);

    List<Map<String, Object>> issueReportForPass11Month(String organId);


    /**
     * TODO 根据报告编号获取报告基础信息
     *
     * @param reportCode
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/14 9:19 上午
     */
    ReportBaseParam findReportBaseByCondition(@Param("reportCode") String reportCode, @Param("organId") String organId);
}
