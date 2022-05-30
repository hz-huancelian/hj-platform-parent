package org.hj.chain.platform.word;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.common.BusiUtils;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO //雨水/地表水/地下水
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Slf4j
public class ReportTablePolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        ReportItemData detailData = (ReportItemData) data;
        //填充头：
        RowRenderData headData = detailData.getHeadData();
        int headStartColumn = detailData.getHeadStartColumn();
        for (int i = 0; i < headStartColumn; i++) {
            List<CellRenderData> cells = headData.getCells();
            cells.set(i, null);
        }
        int headRow = detailData.getHeadRow();
        TableRenderPolicy.Helper.renderRow(table.getRow(headRow), headData);

        List<RowRenderData> rows = detailData.getRows();
        int startRow = detailData.getStartRow();
        int totalRow = detailData.getTotalRow();
        if (rows != null && !rows.isEmpty()) {
            // 循环插入行
            for (int i = 0; i < totalRow; i++) {
//                填充
                if (rows.size() > i) {
                    XWPFTableRow row = table.getRow(startRow + i);
                    RowRenderData data1 = rows.get(i);
                    TableRenderPolicy.Helper.renderRow(row, data1);
                }
            }

        }

        //填充尾巴
        int tailRow = detailData.getTailRow();
        int tailStartColumn = detailData.getTailStartColumn();
        RowRenderData tailData = detailData.getTailData();
        List<CellRenderData> dataCells = tailData.getCells();
        for (int i = 0; i < tailStartColumn; i++) {
            dataCells.set(i, null);
        }
        TableRenderPolicy.Helper.renderRow(table.getRow(tailRow), tailData);


        if (totalRow - rows.size() > 0) {
            log.info(" 图片地址" + detailData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, detailData.getTemplatePath() + "/blank_pic.png", (startRow + rows.size()));
        }
    }
}