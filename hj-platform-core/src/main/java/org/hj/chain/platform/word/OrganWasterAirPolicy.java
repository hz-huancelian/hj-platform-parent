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
import org.hj.chain.platform.common.XwpsUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 有组织废气(通用)
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class OrganWasterAirPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        OrganWaterAirItemData exhData = (OrganWaterAirItemData) data;

        LinkedHashMap<String, List<RowRenderData>> rowDataMap = exhData.getRowDataMap();

        int startRowNum = 13;
        int totalCells = 5;
        rowDataMap.forEach((factorName, rows) -> {
            rows.stream().forEach(item -> {
                XwpsUtils.buildRow(table, startRowNum, totalCells);


                try {
                    TableRenderPolicy.Helper.renderRow(table.getRow(startRowNum), item);
//                    TableTools.mergeCellsHorizonal(table, startRowNum, 2, 3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (rows.size() > 1) {
                TableTools.mergeCellsVertically(table, 0, startRowNum, startRowNum + rows.size() - 1);
            }
        });

        table.removeRow(startRowNum - 1);
    }
}