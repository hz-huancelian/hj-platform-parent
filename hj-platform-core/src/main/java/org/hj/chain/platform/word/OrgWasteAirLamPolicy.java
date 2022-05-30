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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 有组织废气（油烟）
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class OrgWasteAirLamPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        OrgWasteAirLamItemData itemData = (OrgWasteAirLamItemData) data;

        RowRenderData renderData = itemData.getData();

        int startRowNum = 9;
        TableRenderPolicy.Helper.renderRow(table.getRow(startRowNum), renderData);
    }
}