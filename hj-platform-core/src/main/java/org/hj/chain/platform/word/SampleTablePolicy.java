package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.style.RowStyle;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
public class SampleTablePolicy extends DynamicTableRenderPolicy {
    //起始行
    private int startRow = 3;
    //列数
    private int columCount = 22;

    private int[] mergeCells;

    public SampleTablePolicy() {

    }

    public SampleTablePolicy(int startRow, int columCount, int[] mergeCells) {
        this.startRow = startRow;
        this.columCount = columCount;
        this.mergeCells = mergeCells;
    }

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        SampleData detailData = (SampleData) data;
        List<RowRenderData> sampleTable = detailData.getFactors();
        if (sampleTable != null && !sampleTable.isEmpty()) {
            // 循环插入行
            for (int i = 0; i < sampleTable.size(); i++) {
                XwpsUtils.buildRow(table, startRow, columCount);

//                填充
                TableRenderPolicy.Helper.renderRow(table.getRow(startRow), sampleTable.get(i));

                if ((i + 1) % 2 == 0) {
                    // 合并单元格
                    Arrays.stream(mergeCells).forEach(item -> {
                        TableTools.mergeCellsVertically(table, item, startRow, startRow + 1);
                    });

                }

            }

            table.removeRow(startRow - 1);
        }
    }


}