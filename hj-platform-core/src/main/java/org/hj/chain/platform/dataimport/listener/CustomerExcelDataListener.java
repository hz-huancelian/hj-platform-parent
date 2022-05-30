package org.hj.chain.platform.dataimport.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.dataimport.entity.CustomerImportEntity;
import org.hj.chain.platform.dataimport.entity.UserImportEntity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerExcelDataListener extends AnalysisEventListener<CustomerImportEntity> {
    List<CustomerImportEntity> datas = new ArrayList<>();
    @Override
    public void invoke(CustomerImportEntity t, AnalysisContext analysisContext) {
        datas.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("数据解析完成！");
    }
    public List<CustomerImportEntity> getDatas() {
        return datas;
    }

    public List<CustomerImportEntity> parseImportFile(InputStream inputStream) {
        CustomerExcelDataListener listener = new CustomerExcelDataListener();
        ExcelReader excelReader = EasyExcel.read(inputStream, CustomerImportEntity.class, listener).headRowNumber(1).build();
        ReadSheet sheet = EasyExcel.readSheet(0).build();
        excelReader.read(sheet);
        return listener.getDatas();
    }
}
