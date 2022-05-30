package org.hj.chain.platform.word;

import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 噪声
 * @Iteration : 1.0
 * @Date : 2021/6/14  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Slf4j
public class NoiseType01TemplatePolicy extends DynamicTableRenderPolicy {

    //原始日期
    private static final String ORIGIN_DATE = "1970-01-01";

    private boolean isMerge;

    public NoiseType01TemplatePolicy(boolean isMerge) {
        this.isMerge = isMerge;
    }

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        NoiseType01ItemTemplateData itemData = (NoiseType01ItemTemplateData) data;
        log.info("噪声：itemData->" + itemData);

        LinkedHashMap<String, List<RowRenderData>> rowMap = itemData.getRowMap();
        //总列数

        int startRow = 5;
        //总列
        int totalCells = 8;
        AtomicInteger count = new AtomicInteger(0);
        rowMap.values().forEach(item -> {
            count.addAndGet(item.size());
        });

        //补充行
        int totalRows = count.get();
        if (totalRows < 8) {
            List<RowRenderData> filledRrds = new ArrayList<>();
            for (int i = 0; i < 8 - totalRows; i++) {
                RowRenderData rrd = Rows.of("", "", "", "", "", "", "", "").center().create();
                filledRrds.add(rrd);
            }
            rowMap.put(ORIGIN_DATE, filledRrds);
        }


        ListIterator<Map.Entry<String, List<RowRenderData>>> iter = new ArrayList<>(rowMap.entrySet()).listIterator(rowMap.size());
        while (iter.hasPrevious()) {
            Map.Entry<String, List<RowRenderData>> listEntry = iter.previous();
            List<RowRenderData> rdList = listEntry.getValue();
            String collectDate = listEntry.getKey();
            Collections.reverse(rdList);
            for (int i = 0; i < rdList.size(); i++) {
                XwpsUtils.buildRow(table, startRow, totalCells);
                try {
                    RowRenderData renderData = rdList.get(i);
                    int size = renderData.getCells().size();
                    for (int j = 0; j < totalCells - size; j++) {
                        renderData.addCell(Cells.of("").create());
                    }
                    TableRenderPolicy.Helper.renderRow(table.getRow(startRow), renderData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!collectDate.equals(ORIGIN_DATE)) {
                if (rdList.size() > 1) {
                    TableTools.mergeCellsVertically(table, 0, startRow, startRow + rdList.size() - 1);
                    if (isMerge) {
                        TableTools.mergeCellsVertically(table, 3, startRow, startRow + rdList.size() - 1);
                    }
                }
            }
        }
        table.removeRow(startRow - 1);

        if (8 - totalRows > 0) {
            log.info(" 图片地址" + itemData.getTemplatePath());
            BusiUtils.insertBlankMarkDefaultConfig(table, itemData.getTemplatePath() + "/blank_pic.png", (startRow + totalRows));
        }

    }
}