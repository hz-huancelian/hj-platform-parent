package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.style.RowStyle;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.TableWidthType;
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
 * @description TODO 土壤、底泥 策略
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class SoilPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        SoilItemData itemData = (SoilItemData) data;

        LinkedHashMap<String, Map<String, Map<String, List<RowRenderData>>>> rowDataMap = itemData.getRowDataMap();

        int totalRows = 13;
        int startRowNum = 2;
        int totalCells = 7;

        //一共多少行
        AtomicInteger renderRows = new AtomicInteger(0);
        rowDataMap.forEach((collectDate, factorPointMap) -> {
            factorPointMap.forEach((factorPoint, factorMap) -> {
                factorMap.forEach((factorName, rows) -> {
                    renderRows.getAndAdd(rows.size());
                });
            });
        });

        //补充行
        if (renderRows.get() <= totalRows) {
            for (int i = 0; i < totalRows - renderRows.get(); i++) {
                XwpsUtils.buildRow(table, startRowNum, totalCells);
            }
        }
        rowDataMap.forEach((collectDate, factorPointMap) -> {
            AtomicInteger outerMergeRows = new AtomicInteger(0);

            factorPointMap.forEach((factorPoint, factorMap) -> {
                //合并行
                AtomicInteger mergeRows = new AtomicInteger(0);
                factorMap.forEach((factorName, rows) -> {
                    rows.stream().forEach(item -> {
                        XwpsUtils.buildRow(table, startRowNum, totalCells);

                        try {
                            TableRenderPolicy.Helper.renderRow(table.getRow(startRowNum), item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
//                    if (rows.size() > 1) {
//                        TableTools.mergeCellsVertically(table, 3, startRowNum, startRowNum + rows.size() - 1);
//                    }

                    mergeRows.getAndAdd(rows.size());
                    outerMergeRows.getAndAdd(rows.size());
                });

                int size = mergeRows.get();
                if (size > 1) {
                    TableTools.mergeCellsVertically(table, 0, startRowNum, startRowNum + size - 1);
                    TableTools.mergeCellsVertically(table, 2, startRowNum, startRowNum + size - 1);
                }
            });

            int size = outerMergeRows.get();
            if (size > 1) {
                TableTools.mergeCellsVertically(table, 1, startRowNum, startRowNum + size - 1);

            }
        });
        table.removeRow(startRowNum - 1);

        if (totalRows - renderRows.get() > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (startRowNum + renderRows.get()));
        }

    }
}