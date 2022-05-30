package com.hj.chin.platform.sys;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款通知书 明细表格的自定义渲染策略<br/>
 * 1. 填充货品数据 <br/>
 * 2. 填充人工费数据 <br/>
 *
 * @author Sayi
 */
public class DetailTablePolicy extends DynamicTableRenderPolicy {

    // 货品填充数据所在行数
    int goodsStartRow = 2;
    // 人工费填充数据所在行数
    int laborsStartRow = 5;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        DetailData detailData = (DetailData) data;

//        List<RowRenderData> labors = detailData.getLabors();
//        if (null != labors) {
//            table.removeRow(laborsStartRow);
//            // 循环插入行
//            for (int i = 0; i < labors.size(); i++) {
//                XWPFTableRow insertNewTableRow = table.insertNewTableRow(laborsStartRow);
//                for (int j = 0; j < 7; j++) insertNewTableRow.createCell();
//
//                // 合并单元格
//                TableTools.mergeCellsHorizonal(table, laborsStartRow, 0, 3);
//                TableRenderPolicy.Helper.renderRow(table.getRow(laborsStartRow), labors.get(i));
//            }
//            TableTools.mergeCellsVertically(table, 0, laborsStartRow, laborsStartRow + labors.size());
//        }

        LinkedHashMap<String, List<RowRenderData>> laborsMap = detailData.getLaborsMap();
        int lastStartRow = laborsStartRow;
        if (laborsMap != null && !laborsMap.isEmpty()) {
            table.removeRow(laborsStartRow);
            int k = 0;
            for (Map.Entry<String, List<RowRenderData>> entry : laborsMap.entrySet()) {
                List<RowRenderData> labors = entry.getValue();
                // 循环插入行
                for (int i = labors.size() - 1; i >= 0; i--) {
                    XWPFTableRow insertNewTableRow = table.insertNewTableRow(lastStartRow);
                    //插入单元格
                    for (int j = 0; j < 7; j++) insertNewTableRow.createCell();

                    // 合并单元格
                    TableTools.mergeCellsHorizonal(table, lastStartRow, 1, 3);
                    if (i == labors.size() - 1) {
                        TableTools.mergeCellsHorizonal(table, lastStartRow, 2, 3);
                    }
                    TableRenderPolicy.Helper.renderRow(table.getRow(lastStartRow), labors.get(i));
                }
                TableTools.mergeCellsVertically(table, 0, lastStartRow, lastStartRow + labors.size() - 1);
                TableTools.mergeCellsVertically(table, 1, lastStartRow, lastStartRow + labors.size() - 1);
                lastStartRow += labors.size();

            }
        }

        List<RowRenderData> goods = detailData.getGoods();
        if (null != goods) {
            table.removeRow(goodsStartRow);
            for (int i = 0; i < goods.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(goodsStartRow);
                for (int j = 0; j < 7; j++) insertNewTableRow.createCell();
                TableRenderPolicy.Helper.renderRow(table.getRow(goodsStartRow), goods.get(i));
            }
        }
        XWPFTableRow rowTest = table.getRow(0);


        XWPFTableCell imageCell = rowTest.getCell(0);

        List<XWPFParagraph> paragraphs = imageCell.getParagraphs();

        XWPFParagraph newPara = paragraphs.get(0);

        XWPFRun imageCellRunn = newPara.createRun();

        imageCellRunn.addPicture(new FileInputStream("d:/test/1.png"), XWPFDocument.PICTURE_TYPE_PNG, "1.png", Units.toEMU(600), Units.toEMU(300));

    }

}
