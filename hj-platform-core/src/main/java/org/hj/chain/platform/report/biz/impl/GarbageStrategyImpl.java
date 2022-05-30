package org.hj.chain.platform.report.biz.impl;

import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.vo.report.ReportGenDataVo;

import java.io.IOException;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO  污泥和生活垃圾
 * @Iteration : 1.0
 * @Date : 2021/6/21  6:41 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/21    create
 */
//@Slf4j
//@Component
//@SecdClassType(value = "006001")
@Deprecated
public class GarbageStrategyImpl implements IReportStrategy {
    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {

    }
}