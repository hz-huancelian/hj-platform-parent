package org.hj.chain.platform.word;

import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import com.github.javaparser.utils.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.Arrays;
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
@Slf4j
public class NoiseType0TablePolicy extends DynamicTableRenderPolicy {
    //起始行
    private int startRow = 5;
    //列数
    private int columCount = 7;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        SampleData detailData = (SampleData) data;
        String fieldTestingEquipment = detailData.getFieldTestingEquipment();
        String flowEquipment = detailData.getFlowEquipment();
        String remark = detailData.getRemark();
        List<RowRenderData> sampleTable = detailData.getFactors();
        if (sampleTable != null && !sampleTable.isEmpty()) {
            int size = sampleTable.size();
            RowRenderData rrd = sampleTable.get(size - 1);
            List<CellRenderData> cells = rrd.getCells();
            CellRenderData crd1 = Cells.of("仪器型号及编号:" + (StrUtil.isBlank(fieldTestingEquipment) ? "" : fieldTestingEquipment)).center().create();
            cells.set(4, crd1);
            CellRenderData crd2 = Cells.of("声校准器型号及编号:" + (StrUtil.isBlank(flowEquipment) ? "" : flowEquipment)).center().create();
            cells.set(5, crd2);
            CellRenderData crd3 = Cells.of("备注:" + (StrUtil.isBlank(remark) ? "" : remark)).center().create();
            cells.set(6, crd3);
            log.info("cells.size" + cells.size());
            // 循环插入行
            for (int i = 0; i < size; i++) {
                XwpsUtils.buildRow(table, startRow, columCount);
//                填充
                TableRenderPolicy.Helper.renderRow(table.getRow(startRow), sampleTable.get(i));

            }

            if (size > 1) {
                TableTools.mergeCellsVertically(table, 4, startRow, startRow + size - 1);
                TableTools.mergeCellsVertically(table, 5, startRow, startRow + size - 1);
                TableTools.mergeCellsVertically(table, 6, startRow, startRow + size - 1);
            }

            if (size < 6) {
                for (int i = 0; i < 6 - (size + 1); i++) {
                    XwpsUtils.buildRow(table, startRow + size, columCount);
                }
            }
            table.removeRow(startRow - 1);
        }
    }


}