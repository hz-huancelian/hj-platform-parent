package org.hj.chain.platform.word;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
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
public class SubFactorAirPolicy extends DynamicTableRenderPolicy {
    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (data == null) return;
        SubFactorAirItemData itemData = (SubFactorAirItemData) data;

        List<String> factorPoints = itemData.getPoints();
        List<RowRenderData> dataList = itemData.getDataList();

        int headRowPos = itemData.getHeadRowPos();
        //最大点位数
        int maxPointCount = itemData.getMaxPointCount();

        //数据开始位置
        int dataRowStartPos = itemData.getDataRowStartPos();
        //总单元格数
        int totalCellCount = itemData.getTotalCellCount();

        XWPFTableRow row = table.getRow(headRowPos);
        RowRenderData factorPointData = Rows.of().center().create();
        factorPointData.addCell(null);
        factorPoints.stream().forEach(factorPoint -> {
            CellRenderData cellRenderData = Cells.of(factorPoint).center().create();
            factorPointData.addCell(cellRenderData);
        });

        int pointSize = factorPoints.size();
        if (maxPointCount > pointSize) {
            for (int i = 0; i < maxPointCount - pointSize; i++) {
                factorPointData.addCell(null);
            }
        }

        TableRenderPolicy.Helper.renderRow(row, factorPointData);

        int maxtRowNum = 13;
        int renderRows = dataList.size();
        //补充行
        if (renderRows <= maxtRowNum) {
            for (int i = 0; i < maxtRowNum - renderRows; i++) {
                XwpsUtils.buildRow(table, dataRowStartPos, totalCellCount);
            }
        }


        dataList.forEach(rowRenderData -> {
            XwpsUtils.buildRow(table, dataRowStartPos, totalCellCount);

            int size = rowRenderData.getCells().size();
            for (int i = 0; i < totalCellCount - size; i++) {
                rowRenderData.addCell(null);
            }

            log.info("rowRenderData->" + rowRenderData);
            try {
                TableRenderPolicy.Helper.renderRow(table.getRow(dataRowStartPos), rowRenderData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        if (maxtRowNum - renderRows > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (dataRowStartPos + renderRows));
        }
    }
}