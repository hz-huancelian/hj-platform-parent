package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO  设备渲染策略
 *
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2022/5/17 7:23 下午
 */
@Slf4j
public class EquipmentPolicy extends DynamicTableRenderPolicy {

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        EquipmentItemData itemData = (EquipmentItemData) data;
        log.info("设备编号数据开始渲染");
        Map<String, List<ReportEquipVo>> dataMap = itemData.getDataMap();
        int startRow = 1;
        int totalCells = 5;
        int totalRows = 13;

        List<List<ReportEquipVo>> listList = dataMap.values().stream().collect(Collectors.toList());
        int renderCounts = listList.get(0).size();
        //补充行
        if (renderCounts <= totalRows) {
            for (int i = 0; i < (totalRows - renderCounts); i++) {
                XwpsUtils.buildRow(table, startRow, totalCells);
            }
        }
        dataMap.forEach((secdClassName, dataList) -> {
            for (ReportEquipVo equipVo : dataList) {
                RowRenderData rrd = Rows.of(secdClassName, equipVo.getFactorName(), equipVo.getAnalysisMethod(), equipVo.getEquipModeAndCode(), equipVo.getDetectionLimit()).center().create();
                XwpsUtils.buildRow(table, startRow, totalCells);
                try {
                    TableRenderPolicy.Helper.renderRow(table.getRow(startRow), rrd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (renderCounts > 1) {
                TableTools.mergeCellsVertically(table, 0, startRow, startRow + renderCounts - 1);
            }
        });

        if (totalRows - renderCounts > 2) {
            log.info("空白" + dataMap.keySet().iterator().next() + " 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (renderCounts + 2));
        }

    }
}