package org.hj.chain.platform.word;

import com.deepoove.poi.data.CellRenderData;
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
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 子因子模板
 * @Iteration : 1.0
 * @Date : 2021/7/3  11:14 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/03    create
 */
@Slf4j
public class SubFactorPolicy extends DynamicTableRenderPolicy {
    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (data == null) return;
        SubFactorItemData itemData = (SubFactorItemData) data;

        List<String> freqs = itemData.getFreqs();
        Map<Long, List<RowRenderData>> rowDataMap = itemData.getRowDataMap();

        int headRowPos = itemData.getHeadRowPos();
        //最大频次数
        int maxFreqCount = itemData.getMaxFreqCount();

        //数据开始位置
        int dataRowStartPos = itemData.getDataRowStartPos();
        //总单元格数
        int totalCellCount = itemData.getTotalCellCount();

        XWPFTableRow row = table.getRow(headRowPos);
        RowRenderData freqData = Rows.of().center().create();
        freqData.addCell(null);
        freqs.stream().forEach(freq -> {
            CellRenderData cellRenderData = Cells.of("第" + freq + "次").center().create();
            freqData.addCell(cellRenderData);
        });

        int freqSize = freqs.size();
        if (maxFreqCount > freqSize) {
            for (int i = 0; i < maxFreqCount - freqSize; i++) {
                freqData.addCell(null);
            }
        }

        TableRenderPolicy.Helper.renderRow(row, freqData);


        int maxRowNum = 13;
        int renderRows = rowDataMap.values().stream().mapToInt(item -> item.size()).sum();

        //补充行
        if (renderRows <= maxRowNum) {
            for (int i = 0; i < maxRowNum - renderRows; i++) {
                XwpsUtils.buildRow(table, dataRowStartPos, totalCellCount);
            }
        }
        rowDataMap.forEach((factorName, subFactorList) -> {
//            log.info("subFactorList->" + subFactorList);

            subFactorList.stream().forEach(item -> {
                XwpsUtils.buildRow(table, dataRowStartPos, totalCellCount);
                int size = item.getCells().size();
                for (int i = 0; i < totalCellCount - size; i++) {
                    item.addCell(null);
                }
                try {
                    TableRenderPolicy.Helper.renderRow(table.getRow(dataRowStartPos), item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            int size = subFactorList.size();
            if (size > 1) {
                TableTools.mergeCellsVertically(table, 0, dataRowStartPos, dataRowStartPos + size - 1);
            }
        });

        table.removeRow(dataRowStartPos - 1);

        if (maxRowNum - renderRows > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (dataRowStartPos + renderRows));
        }

    }
}