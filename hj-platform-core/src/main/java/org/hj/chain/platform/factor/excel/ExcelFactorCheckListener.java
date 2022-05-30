package org.hj.chain.platform.factor.excel;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.service.IFactorCheckStandardService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project : chem-erp
 * @Description : EXCEL读监听器
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2019/12/20  9:18 AM
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2019/12/20    create
 */
@Slf4j
public class ExcelFactorCheckListener extends AnalysisEventListener<FactorCheckImportVo> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    private final List<FactorCheckStandard> list = new ArrayList<>();
    private final List<ExcelErrorVo> errorList;
    //机构ID
    private final String organId;

    private final IFactorCheckStandardService service;

    public ExcelFactorCheckListener(IFactorCheckStandardService standardService,
                                    String organId,
                                    List<ExcelErrorVo> errorList) {
        service = standardService;
        this.organId = organId;
        this.errorList = errorList;
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());

            String errLine = "第" + excelDataConvertException.getRowIndex() + "行，第" + excelDataConvertException.getColumnIndex() + "列";
            String errMsg = "解析异常，数据为:" + excelDataConvertException.getCellData();
            ExcelErrorVo errorVo = new ExcelErrorVo();
            errorVo.setErrLine(errLine);
            errorVo.setErrDesc(errMsg);
            errorList.add(errorVo);

        }
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JSONUtil.parseFromMap(headMap));
    }

    @Override
    public void invoke(FactorCheckImportVo data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.parse(data));
        FactorCheckStandard dbPo = service.getOne(Wrappers.<FactorCheckStandard>lambdaQuery()
                .select(FactorCheckStandard::getId)
                .eq(FactorCheckStandard::getStandardCode, data.getStandardCode())
                .eq(FactorCheckStandard::getOrganId, organId));
        if (dbPo != null) {
            FactorCheckStandard po = new FactorCheckStandard();
            po.setId(dbPo.getId())
                    .setPrice(data.getPrice())
                    .setCnasFlg((data.getCnasFlg().equals("Y") ? "1" : "0"))
                    .setCmaFlg((data.getCmaFlg().equals("Y") ? "1" : "0"));
            list.add(po);
        } else {
            ExcelErrorVo errorVo = new ExcelErrorVo();
            errorVo.setErrLine(data.getStandardCode());
            errorVo.setErrDesc("当前code在平台库里不存在！");
            errorList.add(errorVo);
        }
        if (list.size() >= BATCH_COUNT) {
            service.updateBatchById(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        service.updateBatchById(list);
        log.info("所有数据解析完成！");
    }
}