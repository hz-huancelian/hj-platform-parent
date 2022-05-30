package org.hj.chain.platform.report.biz;

import org.hj.chain.platform.vo.report.ReportGenDataVo;

import java.io.IOException;

public interface IReportStrategy {

    /**
     * TODO 报告填充
     *
     * @param dataVo
     * @param sourceFile
     * @param sedClassId
     * @param reportGenPath
     * @param templatePath
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/7 1:15 下午
     */
    void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException;

}
