package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 道路噪声
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
public class NoiseType2TemplatePolicy extends DynamicTableRenderPolicy {
    //起始行
    private int startRow;
    //列数
    private int columCount;

    public NoiseType2TemplatePolicy(int startRow, int columCount) {
        this.startRow = startRow;
        this.columCount = columCount;
    }

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        NoiseType2ItemTemplateData detailData = (NoiseType2ItemTemplateData) data;
        List<RowRenderData> rrds = detailData.getRrdList();
        if (rrds != null && !rrds.isEmpty()) {
            for (RowRenderData item : rrds) {
                //构建表格
                XwpsUtils.buildRow(table, startRow, columCount);
                //填充
                TableRenderPolicy.Helper.renderRow(table.getRow(startRow), item);
            }
        }

        table.removeRow(startRow - 1);
    }


}