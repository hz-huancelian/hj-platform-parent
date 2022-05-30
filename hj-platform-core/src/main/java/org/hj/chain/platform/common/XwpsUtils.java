package org.hj.chain.platform.common;

import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.RowStyle;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hj.chain.platform.vo.report.FactorParam;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;
import org.hj.chain.platform.word.SubFactorAirData;
import org.hj.chain.platform.word.SubFactorAirItemData;
import org.hj.chain.platform.word.SubFactorData;
import org.hj.chain.platform.word.SubFactorItemData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO word-poi 工具
 * @Iteration : 1.0
 * @Date : 2021/7/1  11:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/01    create
 */
@Slf4j
public class XwpsUtils {

    /**
     * TODO 构建值
     *
     * @param factorVal
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/2 12:01 上午
     */
    public static CellRenderData buildCellRendData(String factorVal) {
        String[] valArray = factorVal.split("×");
        CellRenderData cellRenderData = Cells.of(valArray[0] + " × 10").center().create();
        ParagraphRenderData renderData = cellRenderData.getParagraphs().get(0);
        TextRenderData data = new TextRenderData();
        data.setText(valArray[1]);
        Style style = new Style();
        style.setVertAlign("superscript");
        data.setStyle(style);
        renderData.addText(data);
        return cellRenderData;
    }


    /**
     * TODO 构建行数据
     *
     * @param table
     * @param startRow
     * @param columCount
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/3 3:11 下午
     */
    public static void buildRow(XWPFTable table, int startRow, int columCount) {
        XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow);
        RowStyle rowStyle = new RowStyle();
        rowStyle.setHeight(567);
        rowStyle.setHeightRule("atleast");
        TableTools.styleTableRow(insertNewTableRow, rowStyle);
        //插入单元格
        for (int j = 0; j < columCount; j++) insertNewTableRow.createCell();
    }


    /**
     * TODO  构建子因子
     *
     * @param sedClassName                   二级类别名称
     * @param sampleVoMap                    样品ID 对应的采样数据
     * @param sampIdSubValMap                样品ID对应的子因子集合
     * @param checkFactorIdHomeFactorNameMap 同系因子主键key 对应的同系套餐名称
     * @param sampIdList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/3 5:02 下午
     */
    public static List<SubFactorData> buildSubFactorDatas(String sedClassName,
                                                          String imagePath,
                                                          Map<String, ReportSampleVo> sampleVoMap,
                                                          Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap,
                                                          Map<Long, String> checkFactorIdHomeFactorNameMap,
                                                          List<String> sampIdList,
                                                          int totalCellCount,
                                                          int maxFreqCount) {

        List<ReportSampleVo> reportSampleVos = new ArrayList<>(sampIdList.size());
        sampIdList.stream().forEach(item -> {
            ReportSampleVo reportSampleVo = sampleVoMap.get(item);
            reportSampleVos.add(reportSampleVo);
        });
        //日期排序
        //按照日期升序排序
        List<ReportSampleVo> dateAscSamples = reportSampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());
        List<String> freqs = new ArrayList<>();
        List<String> collectDates = new ArrayList<>();

        //同一天几个样品ID
        Map<String, List<ReportSampleVo>> collectDateGroup = dateAscSamples.stream().collect(Collectors.groupingBy(ReportSampleVo::getCollectDate));
        dateAscSamples.stream().forEach(item -> {
            String freq = String.valueOf(item.getFrequency());
            if (!freqs.contains(freq)) {
                freqs.add(freq);
            }

            String collectDate = item.getCollectDate();
            if (!collectDates.contains(collectDate)) {
                collectDates.add(collectDate);
            }
        });

        //子因子是同日期-同点位下 不同频次的记录信息
        Map<String, Map<Long, LinkedHashMap<String, String>>> existSampIdSubValMap = new HashMap<>();
        sampIdList.stream().forEach(item -> {
            Map<Long, LinkedHashMap<String, String>> subFactorMap = sampIdSubValMap.get(item);
            if (subFactorMap != null && !subFactorMap.isEmpty()) {
                existSampIdSubValMap.put(item, subFactorMap);
            }

        });

        List<SubFactorData> subFactorDatas = new ArrayList<>();
        if (!existSampIdSubValMap.isEmpty()) {
            for (String collectDate : collectDates) {
                //一个因子同一天，频次为多少乘以点位就有几条记录
                List<ReportSampleVo> sampleVos2 = collectDateGroup.get(collectDate);
                if (sampleVos2 != null && !sampleVos2.isEmpty()) {
                    Map<String, List<ReportSampleVo>> factorGroupByPoint = sampleVos2.stream().collect(Collectors.groupingBy(ReportSampleVo::getFactorPoint));
                    factorGroupByPoint.forEach((factorPoint, samples) -> {
                        SubFactorItemData subFactorItemData = new SubFactorItemData();
                        SubFactorData subFactorData = new SubFactorData();
                        subFactorData.setCollectDate(collectDate)
                                .setFactorPoint(factorPoint)
                                .setSecdClassName(sedClassName)
                                .setItemData(subFactorItemData);
                        subFactorDatas.add(subFactorData);
                        List<String> ascFreqs = freqs.stream().sorted().collect(Collectors.toList());
                        subFactorItemData.setFreqs(ascFreqs)
                                .setHeadRowPos(2)
                                .setDataRowStartPos(4)
                                .setMaxFreqCount(maxFreqCount)
                                .setTotalCellCount(totalCellCount);
                        //检测因子主键key->子因子->val
                        Map<Long, Map<String, RowRenderData>> rowDatasMap = new HashMap<>();
                        samples.stream().sorted(Comparator.comparing(ReportSampleVo::getFrequency)).forEach(item -> {
                            String sampItemId = item.getSampItemId();
                            Map<Long, LinkedHashMap<String, String>> subFactorMap = sampIdSubValMap.get(sampItemId);
                            if (subFactorMap != null && !subFactorMap.isEmpty()) {
                                subFactorMap.forEach((checkFactorId, subFactorLinkedMap) -> {
                                    Map<String, RowRenderData> subRowDataMap = rowDatasMap.get(checkFactorId);
                                    if (subRowDataMap == null) {
                                        subRowDataMap = new HashMap<>();
                                        rowDatasMap.put(checkFactorId, subRowDataMap);
                                    }

                                    if (subFactorLinkedMap != null && !subFactorLinkedMap.isEmpty()) {
                                        for (Map.Entry<String, String> entry : subFactorLinkedMap.entrySet()) {
                                            String subFactorKey = entry.getKey();
                                            String subFactorVal = entry.getValue();
                                            CellRenderData cell;
                                            if (StrUtil.isNotBlank(subFactorVal) && subFactorVal.contains("×")) {
                                                cell = XwpsUtils.buildCellRendData(subFactorVal);
                                            } else {
                                                cell = Cells.of(subFactorVal).center().create();
                                            }

                                            RowRenderData data = subRowDataMap.get(subFactorKey);
                                            if (data == null) {
                                                String homeFactorName = checkFactorIdHomeFactorNameMap.get(checkFactorId);
                                                data = Rows.of(homeFactorName).center().create();
                                                data.addCell(Cells.of(subFactorKey).center().create());
                                                subRowDataMap.put(subFactorKey, data);
                                            }
                                            data.addCell(cell);

                                        }

                                    }

                                });

                            }

                        });

                        Map<Long, List<RowRenderData>> dealRowDataMap = new HashMap<>();
                        subFactorItemData.setRowDataMap(dealRowDataMap);
                        subFactorItemData.setTemplatePath(imagePath);

                        rowDatasMap.forEach((checkFactorId, rowsMap) -> {
                            List<RowRenderData> datasList = dealRowDataMap.get(checkFactorId);
                            if (datasList == null) {
                                datasList = rowsMap.values().stream().collect(Collectors.toList());
                                dealRowDataMap.put(checkFactorId, datasList);
                            }
                        });

                    });
                }
            }
        }
        return subFactorDatas;
    }


    /**
     * TODO 环境空气，厂界无组织废气
     *
     * @param sampIdSubValMap                子因子集合
     * @param sedClassName                   二级类别名称
     * @param checkFactorIdHomeFactorNameMap 同系因子主键key 对应的同系套餐名称
     * @param sampIds                        样品ID集合
     * @param factorPoints                   点位集合
     * @param collectDates                   采样日期集合
     * @param collectDateGroup               采样数据按照日期分组
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/3 11:06 下午
     */
    public static List<SubFactorAirData> buildSubFactorAirData(Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap,
                                                               String sedClassName,
                                                               String imagePath,
                                                               Map<Long, String> checkFactorIdHomeFactorNameMap,
                                                               List<String> sampIds,
                                                               List<String> factorPoints,
                                                               List<String> collectDates,
                                                               Map<String, List<ReportSampleVo>> collectDateGroup) {

        Map<String, Map<Long, LinkedHashMap<String, String>>> existSampIdSubValMap = new HashMap<>();
        sampIds.stream().forEach(item -> {
            Map<Long, LinkedHashMap<String, String>> subFactorMap = sampIdSubValMap.get(item);
            if (subFactorMap != null && !subFactorMap.isEmpty()) {
                existSampIdSubValMap.put(item, subFactorMap);
            }

        });

        List<SubFactorAirData> airDataList = new ArrayList<>();
        if (!existSampIdSubValMap.isEmpty()) {
            for (String collectDate : collectDates) {
                //检测因子主键key-频次-子因子
                Map<Long, Map<Integer, Map<String, RowRenderData>>> subFactorMap = new HashMap<>();
                //一个因子同一天，
                List<ReportSampleVo> sampleVos = collectDateGroup.get(collectDate);
                if (sampleVos != null && !sampleVos.isEmpty()) {
                    sampleVos.stream().forEach(item -> {
                        String factorPoint = item.getFactorPoint();
                        Integer frequency = item.getFrequency();
                        String sampItemId = item.getSampItemId();
                        //检测因子主键：子因子
                        Map<Long, LinkedHashMap<String, String>> factorSubValMap = existSampIdSubValMap.get(sampItemId);

                        if (factorSubValMap != null && !factorSubValMap.isEmpty()) {
                            factorSubValMap.forEach((checkFactorId, subFactorLikedMap) -> {
                                Map<Integer, Map<String, RowRenderData>> renderDataMap = subFactorMap.get(checkFactorId);
                                if (renderDataMap == null) {
                                    renderDataMap = new HashMap<>();
                                    subFactorMap.put(checkFactorId, renderDataMap);
                                }
                                Map<String, RowRenderData> rowRenderDataMap = renderDataMap.get(frequency);
                                if (rowRenderDataMap == null) {
                                    rowRenderDataMap = new HashMap<>();
                                    renderDataMap.put(frequency, rowRenderDataMap);
                                }

                                Map<String, String> subFactorValMap = factorSubValMap.get(checkFactorId);

                                if (subFactorValMap != null && !subFactorValMap.isEmpty()) {
                                    for (Map.Entry<String, String> entry : subFactorValMap.entrySet()) {
                                        String subfactorKey = entry.getKey();
                                        String subfactorVal = entry.getValue();
                                        RowRenderData data = rowRenderDataMap.get(subfactorKey);
                                        if (data == null) {
                                            data = Rows.of(subfactorKey).center().create();
                                            for (int i = 0; i < factorPoints.size(); i++) {
                                                data.addCell(null);
                                            }

                                            rowRenderDataMap.put(subfactorKey, data);
                                        }

                                        CellRenderData cell;
                                        if (StrUtil.isNotBlank(subfactorVal) && subfactorVal.contains("×")) {
                                            cell = XwpsUtils.buildCellRendData(subfactorVal);
                                        } else {
                                            cell = Cells.of(subfactorVal).center().create();
                                        }
                                        int index = factorPoints.indexOf(factorPoint);
                                        data.getCells().set(index + 1, cell);
                                    }
                                }
                            });
                        }

                    });

                    subFactorMap.forEach((factorKey, freqSubFactorMap) -> {

                        freqSubFactorMap.forEach((freq, subfactorMap) -> {
                            SubFactorAirData airData = new SubFactorAirData();
                            airData.setCollectDate(collectDate)
                                    .setFactorName(checkFactorIdHomeFactorNameMap.get(factorKey))
                                    .setSecdClassName(sedClassName);

                            List<RowRenderData> renderDataList = subfactorMap.values().stream().collect(Collectors.toList());
                            if (renderDataList.size() > 4) {
                                renderDataList = renderDataList.subList(0, 3);
                            }

                            List<String> resFactorPoints = factorPoints;
                            if (factorPoints.size() > 4) {
                                resFactorPoints = factorPoints.subList(0, 3);
                            }
                            SubFactorAirItemData airItemData = new SubFactorAirItemData();
                            airItemData.setPoints(resFactorPoints)
                                    .setDataRowStartPos(4)
                                    .setHeadRowPos(3)
                                    .setMaxPointCount(4)
                                    .setTotalCellCount(5)
                                    .setTemplatePath(imagePath)
                                    .setDataList(renderDataList);

                            airData.setItemData(airItemData);
                            airDataList.add(airData);

                        });
                    });
                }
            }

        }
        return airDataList;
    }

}