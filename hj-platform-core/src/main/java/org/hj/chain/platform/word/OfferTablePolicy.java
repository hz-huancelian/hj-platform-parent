package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/17  5:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/17    create
 */
public class OfferTablePolicy extends DynamicTableRenderPolicy {
    // 因子填充数据所在行数
    int factorsStartRow = 8;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        FactorData detailData = (FactorData) data;
        LinkedHashMap<String, List<RowRenderData>> laborsMap = detailData.getFactorsMap();
        int lastStartRow = factorsStartRow;
        if (laborsMap != null && !laborsMap.isEmpty()) {
            table.removeRow(factorsStartRow);
            for (Map.Entry<String, List<RowRenderData>> entry : laborsMap.entrySet()) {
                List<RowRenderData> factors = entry.getValue();
                // 循环插入行
                for (int i = factors.size() - 1; i >= 0; i--) {
                    XWPFTableRow insertNewTableRow = table.insertNewTableRow(lastStartRow);
                    //插入单元格
                    for (int j = 0; j < 13; j++) insertNewTableRow.createCell();

                    // 合并单元格
                    if (i == factors.size() - 1) {
                        TableTools.mergeCellsHorizonal(table, lastStartRow, 2, 11);
                    }
                    TableRenderPolicy.Helper.renderRow(table.getRow(lastStartRow), factors.get(i));
                }
                TableTools.mergeCellsVertically(table, 0, lastStartRow, lastStartRow + factors.size() - 1);
                TableTools.mergeCellsVertically(table, 1, lastStartRow, lastStartRow + factors.size() - 1);
                lastStartRow += factors.size();

            }

        }

        LinkedHashMap<String, String> selfCosts = detailData.getSelfCosts();
        if (selfCosts != null && !selfCosts.isEmpty()) {
            lastStartRow = lastStartRow + 6;
            AtomicInteger count = new AtomicInteger(0);
            laborsMap.forEach((item, list) -> {
                count.getAndAdd(list.size());
            });
            for (Map.Entry<String, String> entry : selfCosts.entrySet()) {
                String selfName = entry.getKey();
                String cost = entry.getValue();
                XWPFTableRow insertNewTableRow2 = table.insertNewTableRow(lastStartRow);
                //插入单元格
                for (int j = 0; j < 13; j++) insertNewTableRow2.createCell();
                TableTools.mergeCellsHorizonal(table, lastStartRow, 0, 11);

                RowRenderData selfFeet = Rows.of(selfName, cost)
                        .rowHeight(11)
                        .textFontFamily("宋体")
                        .center().create();

                TableRenderPolicy.Helper.renderRow(table.getRow(lastStartRow), selfFeet);

            }
        }
    }
}