package org.hj.chain.platform.word;

import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
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

import java.util.List;

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
public class OuterUnOrganExhPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        OuterUnOrganExhItemData exhData = (OuterUnOrganExhItemData) data;
        List<String> factorPoints = exhData.getFactorPoints();

        List<List<RowRenderData>> rows = exhData.getRows();
        int totalRows = rows.stream().mapToInt(item ->
                item.size()
        ).sum();
        //填充点位
        XWPFTableRow row = table.getRow(2);
        RowRenderData pointData = Rows.of().create();
        int startColumn = 3;
        for (int i = 0; i < startColumn; i++) {
            pointData.addCell(null);
        }

        for (int i = 0; i < factorPoints.size(); i++) {
            pointData.addCell(Cells.create(factorPoints.get(i)));
        }

        for (int i = 0; i < (10 - (factorPoints.size() + startColumn)); i++) {
            pointData.addCell(null);
        }
        TableRenderPolicy.Helper.renderRow(row, pointData);

        int startRow = 3;
        int totalCells = 10;
        //补充行
        if (totalRows <= 15) {
            for (int i = 0; i < 15 - totalRows; i++) {
                XwpsUtils.buildRow(table, startRow, totalCells);
            }
        }
        //渲染数据
        for (int k = rows.size() - 1; k >= 0; k--) {
            List<RowRenderData> list = rows.get(k);
            int groupSize = list.size();
//            for (int i = 0; i < groupSize; i++) {
//            }
            for (int i = groupSize - 1; i >= 0; i--) {
                XwpsUtils.buildRow(table, startRow, totalCells);
                //频次次数
                RowRenderData rowData = list.get(i);
                int size = rowData.getCells().size();
                for (int m = 0; m < totalCells - size; m++) {
                    rowData.addCell(Cells.create(null));
                }
                TableRenderPolicy.Helper.renderRow(table.getRow(startRow), rowData);
            }

            //合并单元格
            if (groupSize > 1) {
                TableTools.mergeCellsVertically(table, 0, startRow, startRow + groupSize - 1);
                TableTools.mergeCellsVertically(table, 1, startRow, startRow + groupSize - 1);
                TableTools.mergeCellsVertically(table, 8, startRow, startRow + groupSize - 1);
                TableTools.mergeCellsVertically(table, 9, startRow, startRow + groupSize - 1);
            }

        }

        if (15 - totalRows > 0) {
            log.info(" 图片地址" + exhData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, exhData.getTemplatePath() + "/blank_pic.png", (startRow + totalRows));
        }
    }
}