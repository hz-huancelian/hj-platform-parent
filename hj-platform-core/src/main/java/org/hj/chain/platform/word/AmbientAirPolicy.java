package org.hj.chain.platform.word;

import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.style.RowStyle;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 环境空气
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class AmbientAirPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        AmbientAirItemData itemData = (AmbientAirItemData) data;
        List<String> factorPoints = itemData.getFactorPoints();

        log.info("factorPoints->" + factorPoints);

        LinkedHashMap<String, List<RowRenderData>> rowMap = itemData.getRowMap();

        //填充点位
        XWPFTableRow row = table.getRow(1);
        RowRenderData pointData = Rows.of().create();
        int startColumn = 2;
        for (int i = 0; i < startColumn; i++) {
            pointData.addCell(null);
        }

        int fpSize = factorPoints.size();
        if (fpSize > 4) {
            factorPoints = factorPoints.subList(0, 3);
        }
        for (int i = 0; i < fpSize; i++) {
            pointData.addCell(Cells.of(factorPoints.get(i)).center().create());
        }

        for (int i = 0; i < 4 - fpSize; i++) {
            pointData.addCell(Cells.of("").center().create());
        }

        TableRenderPolicy.Helper.renderRow(row, pointData);

        int maxRowNum = 13;

        int renderRows = rowMap.values().stream().mapToInt(item -> item.size()).sum();

        //起始行
        int startRow = 2;
        //总列
        int totalCells = 6;
        //补充行
        if (renderRows <= maxRowNum) {
            for (int i = 0; i < maxRowNum - renderRows; i++) {
                XwpsUtils.buildRow(table, startRow, totalCells);
            }
        }

        rowMap.forEach((collectDate, rdList) -> {
            int rdSize = rdList.size();
            if (rdSize > 4) {
                rdList = rdList.subList(0, 3);
            }
            for (int i = rdSize - 1; i >= 0; i--) {
                XwpsUtils.buildRow(table, startRow, totalCells);
                try {
                    RowRenderData renderData = rdList.get(i);
                    int size = renderData.getCells().size();
                    for (int j = 0; j < totalCells - size; j++) {
                        renderData.addCell(Cells.of("").create());
                    }
                    TableRenderPolicy.Helper.renderRow(table.getRow(startRow), renderData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (rdSize > 1) {
                TableTools.mergeCellsVertically(table, 0, startRow, startRow + rdSize - 1);
            }
        });

        if (maxRowNum - renderRows > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (startRow + renderRows));
        }
    }
}