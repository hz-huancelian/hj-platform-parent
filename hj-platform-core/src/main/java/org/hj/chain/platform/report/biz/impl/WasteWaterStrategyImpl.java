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
import org.hj.chain.platform.vo.record.RecordValItem;
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
 * @description TODO 废水
 * @Iteration : 1.0
 * @Date : 2021/6/20  1:37 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/20    create
 */
@Slf4j
@Component
@SecdClassType(value = "001004")
public class WasteWaterStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //采样点-采样日期-样品性状-检测项目-检测结果（多频次）-标准限值
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
            if (idValMap != null && !idValMap.isEmpty()) {
                factorIds.addAll(idValMap.keySet());
            }
        });

        //日期排序
        //按照日期升序排序
        List<ReportSampleVo> dateAscSamples = reportSampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());
        //因子名称
        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);

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


        WasteWaterData wwData = new WasteWaterData();
        WasteWaterItemData itemData = new WasteWaterItemData();
        wwData.setItemData(itemData);
        itemData.setTemplatePath(imagePath);
        itemData.setFreqs(freqs);
        List<List<RowRenderData>> rows = new ArrayList<>();
        itemData.setRows(rows);
        log.info("数据->" + secdName + "->" + wwData);

        for (String collectDate : collectDates) {
            //一个因子同一天，频次为多少乘以点位就有几条记录
            List<ReportSampleVo> sampleVos = collectDateGroup.get(collectDate);
            if (sampleVos != null && !sampleVos.isEmpty()) {
                log.info("日期下的样品不为空！");
                //同日期，同采样点下，因子对应的行数据：
                Map<String, Map<String, RowRenderData>> factorMap = new HashMap<>();
                //按照同一频次分组(同一日期下)
                Map<Integer, List<ReportSampleVo>> freqGroupMap = sampleVos.stream().collect(Collectors.groupingBy(ReportSampleVo::getFrequency));
                freqs.stream().forEach(freq -> {
                    //集合的大小等于频次大小 几次对应几个样品
                    List<ReportSampleVo> reportSampleVosByFreq = freqGroupMap.get(Integer.parseInt(freq));
                    if (reportSampleVosByFreq != null && !reportSampleVosByFreq.isEmpty()) {
                        for (ReportSampleVo samp : reportSampleVosByFreq) {
                            log.info("进入数据列表！");
                            String factorPoint = samp.getFactorPoint();
                            String sampItemId = samp.getSampItemId();
//                            String sampleProperties = samp.getSampleProperties();
                            Map<String, String> factorVal = sampIdValMap.get(sampItemId);
                            factorVal = factorVal != null ? factorVal : new HashMap<>();
                            factorVal.forEach((factorKey, value) -> {
                                Map<String, RowRenderData> dataMap = factorMap.get(factorPoint);
                                if (dataMap == null) {
                                    dataMap = new HashMap<>();
                                    factorMap.put(factorPoint, dataMap);
                                }
                                CellRenderData cell = BusiUtils.convertCellRenderDataByFactorVal(value);
                                RowRenderData data = dataMap.get(factorKey);
                                if (data != null) {
                                    data.addCell(cell);
                                    //追加性状
//                                    CellRenderData cellRenderData = data.getCells().get(2);
//                                    ParagraphRenderData pdata = cellRenderData.getParagraphs().get(0);
//                                    String pdataVal = pdata.toString();
//                                    ParagraphRenderData newData = Paragraphs.of(pdataVal + sampleProperties).create();
//                                    cellRenderData.getParagraphs().set(0, newData);
                                } else {
                                    String sampleData = samp.getSampleData();
                                    Map<String, Object> sampleRecordDataMap = BusiUtils.parseSampleDataJson(sampleData);
                                    //感官描述
                                    RecordValItem organoleptic = (RecordValItem) sampleRecordDataMap.get("organoleptic");
                                    String organolepticVal = BusiUtils.getSingleVal(organoleptic);
                                    data = Rows.of().center().create();
                                    data.addCell(Cells.of(factorPoint).center().create());
                                    data.addCell(Cells.of(collectDate).center().create());
                                    data.addCell(Cells.of(organolepticVal).center().create());
                                    data.addCell(Cells.of(factorParamMap.get(factorKey).getFactorName()).center().create());
                                    data.addCell(cell);
                                    dataMap.put(factorKey, data);
                                }
                            });


                        }

                    }
                });


                //该采集日期下，
                factorMap.keySet().stream().forEach(item -> {
                    //同点位下的因子对应的信息
                    Map<String, RowRenderData> renderDataMap = factorMap.get(item);
                    List<RowRenderData> datas = renderDataMap.values().stream().collect(Collectors.toList());
                    rows.add(datas);
                });

            }

        }


        List<SubFactorData> subFactorDatas = null;
        Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdLaborSubFactorValMap = dataVo.getSampIdLaborSubFactorValMap();
        if (sampIdLaborSubFactorValMap != null) {
            String secdClassName = dictUtils.getFactorClassMap().get(sedClassId);
            subFactorDatas = XwpsUtils.buildSubFactorDatas(
                    secdClassName,
                    imagePath,
                    sampleVoMap,
                    sampIdLaborSubFactorValMap,
                    dataVo.getCheckFactorIdHomeFactorNameMap(),
                    sampIds,
                    6,
                    4
            );
        }


        log.info("废水：" + wwData);
        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("exh_table", new WasteWaterPolicy(freqs.size())).build();
        XWPFTemplate attachTemplate = null;
        try {
            if (freqs.size() > 1) {
                attachTemplate = XWPFTemplate.compile(templatePath + "/waste_water_template.docx", config).render(wwData);
            } else {
                attachTemplate = XWPFTemplate.compile(templatePath + "/waste_water_template_other.docx", config).render(wwData);
            }
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


        if (subFactorDatas != null) {
            config = Configure.builder().bind("rep_table", new SubFactorPolicy()).build();
            for (int i = 0; i < subFactorDatas.size(); i++) {
                //合并文件
                try {
                    attachTemplate = XWPFTemplate.compile(templatePath + "/water_subfactor_template.docx", config).render(subFactorDatas.get(i));
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
            equipItemData.setTemplatePath(imagePath);
            equipItemData.setDataMap(equipmentVoMap);
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
            log.info("合并成功...................");
            temDoc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (temDoc != null) {
                temDoc.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}