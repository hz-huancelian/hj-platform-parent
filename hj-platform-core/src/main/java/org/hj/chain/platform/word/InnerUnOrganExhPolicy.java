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
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class InnerUnOrganExhPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        InnerUnOrganExhItemData exhData = (InnerUnOrganExhItemData) data;
        log.info("车间无组织废气开始渲染");

        LinkedHashMap<String, Map<String, Map<String, List<RowRenderData>>>> rowDataMap = exhData.getRowDataMap();

        int startRowNum = 1;
        int totalCells = 6;
        AtomicInteger totalRows = new AtomicInteger(0);

        rowDataMap.forEach((collectDate, factorPointMap) -> {
            factorPointMap.forEach((factorPoint, factorMap) -> {
                factorMap.forEach((factorName, rows) -> {
                    totalRows.addAndGet(rows.size());
                });
            });
        });
        //补充行
        if (totalRows.get() <= 15) {
            for (int i = 0; i < 15 - totalRows.get(); i++) {
                XwpsUtils.buildRow(table, startRowNum, totalCells);
            }
        }
        rowDataMap.forEach((collectDate, factorPointMap) -> {
            factorPointMap.forEach((factorPoint, factorMap) -> {
                factorMap.forEach((factorName, rows) -> {
                    log.info("车间无组织废气开始数据渲染");
                    rows.stream().forEach(item -> {
                        log.info("数据->" + item);
                        XwpsUtils.buildRow(table, startRowNum, totalCells);

                        try {
                            TableRenderPolicy.Helper.renderRow(table.getRow(startRowNum), item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    if (rows.size() > 1) {
                        TableTools.mergeCellsVertically(table, 2, startRowNum, startRowNum + rows.size() - 1);
                    }
                });

                int size = factorMap.values().size();
                if (size > 1) {
                    TableTools.mergeCellsVertically(table, 1, startRowNum, startRowNum + size - 1);
                }
            });

            int size = factorPointMap.values().size();
            if (size > 1) {
                TableTools.mergeCellsVertically(table, 0, startRowNum, startRowNum + size - 1);

            }
        });
        if (15 - totalRows.get() > 0) {
            log.info(" 图片地址" + exhData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, exhData.getTemplatePath() + "/blank_pic.png", (startRowNum + totalRows.get()));
        }

    }
}