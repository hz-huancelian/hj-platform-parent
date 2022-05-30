package org.hj.chain.platform.dataimport.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.hj.chain.platform.dataimport.entity.EquipmentImportEntity;
import org.hj.chain.platform.dataimport.entity.UserImportEntity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserExcelDataListener extends AnalysisEventListener<UserImportEntity> {
    List<UserImportEntity> datas = new ArrayList<>();
    @Override
    public void invoke(UserImportEntity t, AnalysisContext analysisContext) {
        datas.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("数据解析完成！");
    }
    public List<UserImportEntity> getDatas() {
        return datas;
    }

    public List<UserImportEntity> parseImportFile(InputStream inputStream) {
        UserExcelDataListener listener = new UserExcelDataListener();
        ExcelReader excelReader = EasyExcel.read(inputStream, UserImportEntity.class, listener).headRowNumber(1).build();
        ReadSheet sheet = EasyExcel.readSheet(0).build();
        excelReader.read(sheet);
        return listener.getDatas();
    }
}
