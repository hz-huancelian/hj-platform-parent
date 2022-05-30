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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 废水
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class WasteWaterPolicy extends DynamicTableRenderPolicy {


    private int freqSize;

    public WasteWaterPolicy(int freqSize) {
        this.freqSize = freqSize;
    }

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        WasteWaterItemData dataItem = (WasteWaterItemData) data;
        List<String> freqs = dataItem.getFreqs();

        List<List<RowRenderData>> rows = dataItem.getRows();
        int totalRows = rows.stream().mapToInt(item ->
                item.size()
        ).sum();

        int fstartNum;
        if (freqSize > 1) {

            //填充点位
            XWPFTableRow row = table.getRow(1);
            RowRenderData freqData = Rows.of().create();

            int startColumn = 4;
            for (int i = 0; i < startColumn; i++) {
                freqData.addCell(null);
            }

            for (int i = 0; i < freqs.size(); i++) {
                freqData.addCell(Cells.of(freqs.get(i)).center().create());
            }

            for (int i = 0; i < (10 - (freqs.size() + startColumn)); i++) {
                freqData.addCell(null);
            }
            TableRenderPolicy.Helper.renderRow(row, freqData);

            int startRow = 2;
            fstartNum = startRow;
            int totalCell = 10;
            if (freqs.size() == 1) {
                totalCell = 6;
            }

            //补充行
            if (totalRows <= 15) {
                for (int i = 0; i < 15 - totalRows; i++) {
                    XwpsUtils.buildRow(table, startRow, totalCell);
                }
            }
            //渲染数据
            for (int k = rows.size() - 1; k >= 0; k--) {
                List<RowRenderData> list = rows.get(k);
                int groupSize = list.size();
                for (int i = groupSize - 1; i >= 0; i--) {
                    XwpsUtils.buildRow(table, startRow, totalCell);
                    //频次次数
                    RowRenderData rowData = list.get(i);
                    int size = rowData.getCells().size();
                    for (int m = 0; m < totalCell - size; m++) {
                        rowData.addCell(Cells.create(null));
                    }

                    if (size > totalCell) {
                        log.info("超过值域的记录：" + rowData);
                    }
                    try {
                        TableRenderPolicy.Helper.renderRow(table.getRow(startRow), rowData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //合并单元格
                if (groupSize > 1) {
                    TableTools.mergeCellsVertically(table, 0, startRow, startRow + groupSize - 1);
                    TableTools.mergeCellsVertically(table, 1, startRow, startRow + groupSize - 1);
                    TableTools.mergeCellsVertically(table, 2, startRow, startRow + groupSize - 1);
                }

            }


        } else {
            int startRow = 1;
            fstartNum = startRow;
            int totalCell = 6;
            if (totalRows <= 15) {
                for (int i = 0; i < 15 - totalRows; i++) {
                    XwpsUtils.buildRow(table, startRow, totalCell);
                }
            }
            for (int k = rows.size() - 1; k >= 0; k--) {
                List<RowRenderData> list = rows.get(k);
                int groupSize = list.size();
                for (int i = groupSize - 1; i >= 0; i--) {
                    XwpsUtils.buildRow(table, startRow, totalCell);
                    //频次次数
                    RowRenderData rowData = list.get(i);
                    int size = rowData.getCells().size();
                    for (int m = 0; m < totalCell - size; m++) {
                        rowData.addCell(Cells.create(null));
                    }

                    if (size > totalCell) {
                        log.info("超过值域的记录：" + rowData);
                    }
                    log.info("data->" + rowData);
                    try {
                        TableRenderPolicy.Helper.renderRow(table.getRow(startRow), rowData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //合并单元格
                if (groupSize > 1) {
                    TableTools.mergeCellsVertically(table, 0, startRow, startRow + groupSize - 1);
                    TableTools.mergeCellsVertically(table, 1, startRow, startRow + groupSize - 1);
                    TableTools.mergeCellsVertically(table, 2, startRow, startRow + groupSize - 1);
                }

            }
        }

        if (15 - totalRows > 0) {
            log.info(" 图片地址" + dataItem.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, dataItem.getTemplatePath() + "/blank_pic.png", (fstartNum + totalRows));
        }
    }
}