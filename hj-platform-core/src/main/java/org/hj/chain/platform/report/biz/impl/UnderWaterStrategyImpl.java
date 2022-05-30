package org.hj.chain.platform.report.biz.impl;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.report.component.SecdClassType;
import org.hj.chain.platform.vo.report.ReportGenDataVo;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 地下水
 * @Iteration : 1.0
 * @Date : 2021/6/20  1:34 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/20    create
 */
//@Slf4j
//@Component
//@SecdClassType(value = "001003")
@Deprecated
public class UnderWaterStrategyImpl implements IReportStrategy {
    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {

    }
}