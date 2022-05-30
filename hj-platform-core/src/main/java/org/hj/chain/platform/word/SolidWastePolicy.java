package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.style.RowStyle;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 固废 策略
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class SolidWastePolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        SolidWasteItemData itemData = (SolidWasteItemData) data;

        LinkedHashMap<String, List<RowRenderData>> rowMap = itemData.getRowMap();
        int startRowNum = 1;
        int totalCells = 7;

        AtomicInteger totalRows = new AtomicInteger(0);
        rowMap.forEach((collectDate, dataList) -> {
            totalRows.addAndGet(dataList.size());
        });

        //补充行
        if (totalRows.get() <= 15) {
            for (int i = 0; i < 15 - totalRows.get(); i++) {
                XwpsUtils.buildRow(table, startRowNum, totalCells);
            }
        }

        rowMap.forEach((collectDate, dataList) -> {
            dataList.stream().forEach(item -> {
                XwpsUtils.buildRow(table, startRowNum, totalCells);

                try {
                    TableRenderPolicy.Helper.renderRow(table.getRow(startRowNum), item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (dataList.size() > 1) {
                TableTools.mergeCellsVertically(table, 1, startRowNum, startRowNum + dataList.size() - 1);
            }
        });

        if (15 - totalRows.get() > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (startRowNum + totalRows.get()));
        }

    }
}