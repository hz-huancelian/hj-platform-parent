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
 * @description TODO 厂界无组织废气
 * @Iteration : 1.0
 * @Date : 2021/6/10  9:54 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/10    create
 */
@Slf4j
@Component
@SecdClassType(value = "002003")
public class OuterUnOrganExhStrategyImpl implements IReportStrategy {
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

        List<String> sampIds = secdClassSampMap.get(sedClassId);
        Set<String> factorIds = new HashSet<>();
        List<ReportSampleVo> reportSampleVos = new ArrayList<>(sampIds.size());
        sampIds.stream().forEach(item -> {
            ReportSampleVo reportSampleVo = sampleVoMap.get(item);
            reportSampleVos.add(reportSampleVo);
            Map<String, String> idValMap = sampIdValMap.get(item);
            factorIds.addAll(idValMap.keySet());
        });

        //日期排序
        //按照日期升序排序
        List<ReportSampleVo> dateAscSamples = reportSampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());
        //有序的因子IDs
        List<String> ascFactorIds = factorIds.stream().sorted().collect(Collectors.toList());
        //因子名称
        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);

        List<String> factorPoints = new ArrayList<>();
        List<String> collectDates = new ArrayList<>();

        //同一天几个样品ID
        Map<String, List<ReportSampleVo>> collectDateGroup = dateAscSamples.stream().collect(Collectors.groupingBy(ReportSampleVo::getCollectDate));
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


        OuterUnOrganExhData exhData = new OuterUnOrganExhData();
        OuterUnOrganExhItemData itemData = new OuterUnOrganExhItemData();
        exhData.setOuterUnOrganExhItemData(itemData);

        itemData.setFactorPoints(factorPoints);
        itemData.setTemplatePath(imagePath);
        List<List<RowRenderData>> rows = new ArrayList<>();
        itemData.setRows(rows);


        for (String collectDate : collectDates) {

            //一个因子同一天，频次为多少乘以点位就有几条记录
            List<ReportSampleVo> sampleVos = collectDateGroup.get(collectDate);
            if (sampleVos != null && !sampleVos.isEmpty()) {
                //同频次下，因子对应的行数据：
                Map<Integer, Map<String, RowRenderData>> factorMap = new HashMap<>();
                //按照同一点位分组(同一日期下)
                Map<String, List<ReportSampleVo>> pointGroupMap = sampleVos.stream().collect(Collectors.groupingBy(ReportSampleVo::getFactorPoint));
                factorPoints.stream().forEach(factorPoint -> {
                    //集合的大小等于频次大小 几次对应几个样品
                    List<ReportSampleVo> reportSampleVosByPoint = pointGroupMap.get(factorPoint);
                    if (reportSampleVosByPoint != null && !reportSampleVosByPoint.isEmpty()) {
                        for (ReportSampleVo samp : reportSampleVosByPoint) {
                            Integer frequency = samp.getFrequency();
                            Map<String, RowRenderData> dataMap = factorMap.get(frequency);
                            if (dataMap == null) {
                                dataMap = new HashMap<>();
                                factorMap.put(frequency, dataMap);
                            }
                            Map<String, String> factorVal = sampIdValMap.get(samp.getSampItemId());
                            for (String item : ascFactorIds) {
                                String val = factorVal.get(item);
                                CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(val);
                                RowRenderData data = dataMap.get(item);
                                if (data != null) {
                                    data.addCell(factorValCell);
                                } else {
                                    data = Rows.of().center().create();
                                    data.addCell(Cells.of(factorParamMap.get(item).getFactorName()).center().create());
                                    data.addCell(Cells.of(collectDate).center().create());
                                    data.addCell(Cells.of("第" + frequency + "次").center().create());
                                    data.addCell(factorValCell);
                                    dataMap.put(item, data);
                                }
                            }
                        }

                    }
                });


                //该采集日期下，
                Map<String, List<RowRenderData>> rowRenderDataMap = new HashMap<>();
                factorMap.keySet().stream().forEach(item -> {
                    //频次下的因子对应的信息
                    Map<String, RowRenderData> renderDataMap = factorMap.get(item);
                    ascFactorIds.stream().forEach(factorId -> {
                        List<RowRenderData> dataList = rowRenderDataMap.get(factorId);
                        if (dataList == null) {
                            dataList = new ArrayList<>();
                            rowRenderDataMap.put(factorId, dataList);
                        }
                        dataList.add(renderDataMap.get(factorId));

                    });
                });

                ascFactorIds.stream().forEach(item -> {
                    rows.add(rowRenderDataMap.get(item));
                });


            }

        }

        List<SubFactorAirData> airDataList = null;
        Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap = dataVo.getSampIdLaborSubFactorValMap();
        if (sampIdSubValMap != null && !sampIdSubValMap.isEmpty()) {
            String secdClassName = dictUtils.getFactorClassMap().get(sedClassId);
            airDataList = XwpsUtils.buildSubFactorAirData(
                    sampIdSubValMap,
                    secdClassName,
                    imagePath,
                    dataVo.getCheckFactorIdHomeFactorNameMap(),
                    sampIds,
                    factorPoints,
                    collectDates,
                    collectDateGroup);

        }

        log.info("数据->" + secdName + "->" + exhData);
        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("exh_table", new OuterUnOrganExhPolicy()).build();
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile(templatePath + "/outerunorganexh_template.docx", config).render(exhData);
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


        if (airDataList != null) {
            config = Configure.builder().bind("rep_table", new SubFactorAirPolicy()).build();
            for (int i = 0; i < airDataList.size(); i++) {
                //合并文件
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
            EquipmentItemData equipItemData = new EquipmentItemData();
            equipItemData.setDataMap(equipmentVoMap);
            equipItemData.setTemplatePath(imagePath);
            tableData.setItemData(equipItemData);
            config = Configure.builder().bind("equip_table", new EquipmentPolicy()).build();
            //合并文件
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