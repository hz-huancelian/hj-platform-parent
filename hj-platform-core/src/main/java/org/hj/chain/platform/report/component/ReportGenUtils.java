package org.hj.chain.platform.report.component;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.vo.report.ReportGenDataVo;

import java.io.IOException;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/6  10:35 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/06    create
 */
@Slf4j
public class ReportGenUtils {

    /**
     * TODO 组装报告
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/6 10:40 下午
     */
    public static void buildReport(ReportGenDataVo dataVo,
                                   List<IReportStrategy> reportStrategyList,
                                   List<String> secdClassIds,
                                   String sourceFile,
                                   String reportGenPath,
                                   String templatePath) throws IOException {
        log.info("二级类别集合");
        secdClassIds.stream().forEach(System.out::println);
        for (int i = 0; i < reportStrategyList.size(); i++) {
            IReportStrategy strategy = reportStrategyList.get(i);
            String secdClassId = secdClassIds.get(i);
            //文件地址
            strategy.fillTemplate(dataVo, sourceFile, secdClassId, reportGenPath, templatePath);
        }
    }


}