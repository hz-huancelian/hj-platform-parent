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
 * @description TODO 噪声类型0
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
public class NoiseType1TablePolicy extends DynamicTableRenderPolicy {
    //起始行
    private int startRow = 3;
    //列数
    private int columCount = 6;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        SampleData detailData = (SampleData) data;
        List<RowRenderData> sampleTable = detailData.getFactors();
        if (sampleTable != null && !sampleTable.isEmpty()) {
            int size = sampleTable.size();
            // 循环插入行
            for (int i = 0; i < size; i++) {
                XwpsUtils.buildRow(table, startRow, columCount);

//                填充
                TableRenderPolicy.Helper.renderRow(table.getRow(startRow), sampleTable.get(i));

            }

            if (size < 6) {
                for (int i = 0; i < 6 - (size + 1); i++) {
                    XwpsUtils.buildRow(table, startRow + size, columCount);
                }
            }
//            table.removeRow(startRow - 1);
        }
    }


}