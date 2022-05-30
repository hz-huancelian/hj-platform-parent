package org.hj.chain.platform.report.biz.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.report.component.SecdClassType;
import org.hj.chain.platform.vo.report.FactorParam;
import org.hj.chain.platform.vo.report.ReportGenDataVo;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;
import org.hj.chain.platform.word.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 环境空气
 * @Iteration : 1.0
 * @Date : 2021/6/20  1:39 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/20    create
 */
@Slf4j
@Component
@SecdClassType(value = "002001")
public class AmbientAirStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //样品ID-样品性状、采集时间
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();
        //二级分类：样品集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();
        //样品：对应的key val
        Map<String, Map<String, String>> sampIdValMap = dataVo.getSampIdLaborFactorValMap();
        //二级分类下的因子信息
        Map<String, LinkedHashMap<String, FactorParam>> secdClassFactorMap = dataVo.getSecdClassLaborFactorMap();

        //因子名称
        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);
        //均值类型
        Map<String, String> meanTypeMap = dataVo.getMeanTypeMap();

        List<AmbientAirData> dataList = new ArrayList<>();

        //均值类型；因子-数据
        Map<String, Map<String, AmbientAirData>> dataMap = new HashMap<>();
        //均值类型-因子-日期-频次-数据
        Map<String, Map<String, Map<String, Map<Integer, RowRenderData>>>> dataItemMap = new HashMap<>();

        List<String> sampIds = secdClassSampMap.get(sedClassId).stream().sorted().collect(Collectors.toList());

        Map<String, List<String>> factorPointsMap = new HashMap<>();
        sampIds.stream().forEach(sampId -> {
            ReportSampleVo sampleVo = sampleVoMap.get(sampId);
            String factorPoint = sampleVo.getFactorPoint();
            factorParamMap.forEach((factorKey, param) -> {
                List<String> pointList = factorPointsMap.get(factorKey);
                if (pointList == null) {
                    pointList = new ArrayList<>();
                    factorPointsMap.put(factorKey, pointList);
                }

                if (!pointList.contains(factorPoint)) {
                    pointList.add(factorPoint);
                }
            });

        });
        //样品ID
        sampIds.stream().forEach(item -> {
            //样品下的因子ID
            ReportSampleVo sampleVo = sampleVoMap.get(item);
            Integer frequency = sampleVo.getFrequency();
            String collectDate = sampleVo.getCollectDate();
            String meanType = meanTypeMap.get(item);

            //检测点位
            String factorPoint = sampleVo.getFactorPoint();
            //该样品下所有的因子信息
            Map<String, String> factorMap = sampIdValMap.get(item);
            factorMap.forEach((factorKey, factorVal) -> {

                Map<String, Map<String, Map<Integer, RowRenderData>>> factorItemMap = dataItemMap.get(meanType);
                if (factorItemMap == null) {
                    factorItemMap = new HashMap<>();
                    dataItemMap.put(meanType, factorItemMap);
                }

                Map<String, Map<Integer, RowRenderData>> renderDataMap = factorItemMap.get(factorKey);

                if (renderDataMap == null) {
                    renderDataMap = new HashMap<>();
                    factorItemMap.put(factorKey, renderDataMap);
                }

                Map<Integer, RowRenderData> freqRowRenderDataMap = renderDataMap.get(collectDate);
                if (freqRowRenderDataMap == null) {
                    freqRowRenderDataMap = new HashMap<>();
                    renderDataMap.put(collectDate, freqRowRenderDataMap);
                }

                FactorParam param = factorParamMap.get(factorKey);
                Map<String, AmbientAirData> airDataMap = dataMap.get(meanType);
                if (airDataMap == null) {
                    airDataMap = new HashMap<>();
                    dataMap.put(meanType, airDataMap);
                }
                List<String> factorPoints = factorPointsMap.get(factorKey);
                CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(factorVal);
                AmbientAirData airData = airDataMap.get(factorKey);
                if (airData == null) {
                    airData = new AmbientAirData();
                    dataList.add(airData);
                    airData.setFactorName(param.getFactorName());
                    airData.setMeanType(meanType);
                    AmbientAirItemData itemData = new AmbientAirItemData();
                    itemData.setTemplatePath(imagePath);
                    itemData.setFactorPoints(factorPoints);
                    airData.setItemData(itemData);
                    LinkedHashMap<String, List<RowRenderData>> rowMap = new LinkedHashMap<>();
                    itemData.setRowMap(rowMap);

                    RowRenderData renderData = Rows.of(collectDate, String.valueOf(frequency)).center().create();
                    for (int i = 0; i < factorPoints.size(); i++) {
                        //表头展示因子点位
                        String disFactorPoint = factorPoints.get(i);
                        if (disFactorPoint.equals(factorPoint)) {
                            renderData.getCells().add(factorValCell);
                        } else {
                            renderData.addCell(Cells.of("").create());
                        }
                    }
                    List<RowRenderData> itemDatas = new ArrayList<>();
                    itemDatas.add(renderData);
                    rowMap.put(collectDate, itemDatas);

                    airDataMap.put(factorKey, airData);

                    freqRowRenderDataMap.put(frequency, renderData);
                } else {
                    RowRenderData data = freqRowRenderDataMap.get(frequency);
                    if (data != null) {
                        for (int i = 0; i < factorPoints.size(); i++) {
                            //表头展示因子点位
                            String disFactorPoint = factorPoints.get(i);
                            if (disFactorPoint.equals(factorPoint)) {
                                List<CellRenderData> cells = data.getCells();
                                cells.set(i + 2, factorValCell);
                            }
                        }
                    } else {
                        AmbientAirItemData itemData = airData.getItemData();
                        LinkedHashMap<String, List<RowRenderData>> rowMap = itemData.getRowMap();
                        List<RowRenderData> list = rowMap.get(collectDate);
                        if (list == null) {
                            list = new ArrayList<>();
                            rowMap.put(collectDate, list);
                        }
                        data = Rows.of(collectDate, String.valueOf(frequency)).center().create();
                        for (int i = 0; i < factorPoints.size(); i++) {
                            //表头展示因子点位
                            String disFactorPoint = factorPoints.get(i);
                            if (disFactorPoint.equals(factorPoint)) {
                                data.getCells().add(factorValCell);
                            } else {
                                data.addCell(Cells.of("").create());
                            }
                        }
                        list.add(data);

                        freqRowRenderDataMap.put(frequency, data);
                    }
                }
                log.info("数据->" + secdName + "->" + airData);
            });
        });


        //日期排序
        //按照日期升序排序
        List<ReportSampleVo> reportSampleVos = new ArrayList<>(sampIds.size());
        sampIds.stream().forEach(item -> {
            ReportSampleVo reportSampleVo = sampleVoMap.get(item);
            reportSampleVos.add(reportSampleVo);
        });

        List<String> factorPoints = new ArrayList<>();
        List<String> collectDates = new ArrayList<>();
        List<ReportSampleVo> dateAscSamples = reportSampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());
        Map<String, List<ReportSampleVo>> collectDateGroup = reportSampleVos.stream().collect(Collectors.groupingBy(ReportSampleVo::getCollectDate));
        dateAscSamples.stream().forEach(item -> {
            String factorPoint = item.getFactorPoint();
            if (!factorPoints.contains(factorPoint)) {
                factorPoints.add(factorPoint);
            }

            String collectDate = item.getCollectDate();
            if (!collectDates.contains(collectDate)) {
                collectDates.add(collectDate);
            }
        });


        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("rep_table", new AmbientAirPolicy()).build();
        for (AmbientAirData data : dataList) {
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/ambient_air_template.docx", config).render(data);
                NiceXWPFDocument xwpfDocument = attachTemplate.getXWPFDocument();
                temDoc = temDoc.merge(xwpfDocument);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (attachTemplate != null) {
                    try {
                        attachTemplate.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        List<SubFactorAirData> airDataList = null;
        Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdLaborSubFactorValMap = dataVo.getSampIdLaborSubFactorValMap();
        if (sampIdLaborSubFactorValMap != null && !sampIdLaborSubFactorValMap.isEmpty()) {
            String secdClassName = dictUtils.getFactorClassMap().get(sedClassId);
            airDataList = XwpsUtils.buildSubFactorAirData(
                    sampIdLaborSubFactorValMap,
                    secdClassName,
                    imagePath,
                    dataVo.getCheckFactorIdHomeFactorNameMap(),
                    sampIds,
                    factorPoints,
                    collectDates,
                    collectDateGroup);
        }


        if (airDataList != null) {
            config = Configure.builder().bind("rep_table", new SubFactorAirPolicy()).build();
            for (int i = 0; i < airDataList.size(); i++) {
                //合并文件
                XWPFTemplate attachTemplate = null;
                try {
                    attachTemplate = XWPFTemplate.compile(templatePath + "/ambient_air_subfactor_template.docx", config).render(airDataList.get(i));
                    NiceXWPFDocument xwpfDocument = attachTemplate.getXWPFDocument();
                    temDoc = temDoc.merge(xwpfDocument);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (attachTemplate != null) {
                        try {
                            attachTemplate.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        //设备附表
        List<ReportEquipVo> sceneEquipVos = dataVo.getSceneEquipVoMap().get(sedClassId);
        //实验室
        List<ReportEquipVo> reportEquipVos = dataVo.getLaborEquipVoMap().get(sedClassId);
        Map<String, List<ReportEquipVo>> equipmentVoMap = BusiUtils.buildEquipMap(secdName, reportEquipVos, sceneEquipVos);
        if (equipmentVoMap != null) {
            EquipmentTableData tableData = new EquipmentTableData();
            EquipmentItemData itemData = new EquipmentItemData();
            itemData.setTemplatePath(imagePath);
            itemData.setDataMap(equipmentVoMap);
            tableData.setItemData(itemData);
            config = Configure.builder().bind("equip_table", new EquipmentPolicy()).build();
            //合并文件
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/equipment_template.docx", config).render(tableData);
                NiceXWPFDocument xwpfDocument = attachTemplate.getXWPFDocument();
                temDoc = temDoc.merge(xwpfDocument);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (attachTemplate != null) {
                    try {
                        attachTemplate.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(sourceFile);
            temDoc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

            if (temDoc != null) {
                temDoc.close();
            }
        }
    }
}