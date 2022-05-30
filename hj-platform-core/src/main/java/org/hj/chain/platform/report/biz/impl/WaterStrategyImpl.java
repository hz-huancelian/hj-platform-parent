package org.hj.chain.platform.report.biz.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.XwpsUtils;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.report.component.SecdClassType;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.record.RecordValItem;
import org.hj.chain.platform.vo.report.FactorParam;
import org.hj.chain.platform.vo.report.ReportGenDataVo;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 确定边界：参数的边界，返回值的边界，该类仅是策略的算法体，不参与任何其他的业务逻辑
 * @Iteration : 1.0
 * @Date : 2021/6/7  8:31 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/07    create
 */
@Slf4j
@Component
@SecdClassType(value = "001001,001002,001003")//雨水/地表水/地下水
public class WaterStrategyImpl implements IReportStrategy {

    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private IFactorService factorService;
    @Value("${file.template.path}")
    private String imagePath;

    //头
    private int headSize = 13;
    //总列
    private int totalCells = 16;

    //尾巴
    private int tailSize = 14;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //二级分类：样品集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();
        //样品：对应的key val
        Map<String, Map<String, String>> sampIdLaborFactorValMap = dataVo.getSampIdLaborFactorValMap();
        //二级分类下的因子信息
        Map<String, LinkedHashMap<String, FactorParam>> secdClassLaborFactorMap = dataVo.getSecdClassLaborFactorMap();
        //样品ID-报告信息
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();
        //样品列表
        List<String> sampIds = secdClassSampMap.get(sedClassId);

        //主因子数据
        if (sampIds != null && !sampIds.isEmpty()) {
            //现场采样记录单因子
            List<SampleFactorDataParam> sceneSampleFactors = dataVo.getSceneSampleFactorDataMap().get(sedClassId);
            Map<String, Map<String, SampleFactorDataParam>> factorDataMap = new HashMap<>();
            if (sceneSampleFactors != null && !sceneSampleFactors.isEmpty()) {
                factorDataMap = sceneSampleFactors.stream().collect(Collectors.groupingBy(SampleFactorDataParam::getSampleNo, Collectors.toMap(SampleFactorDataParam::getCheckStandardId, Function.identity())));
            }
            Map<String, Map<String, SampleFactorDataParam>> finalFactorDataMap = factorDataMap;

            Set<String> factorIds = new HashSet<>();
            //加工样品下因子对应的值（把现场采样记录单的数据采集进去）
            sampIds.stream().forEach(item -> {
                //采样记录的因子信息
                Map<String, SampleFactorDataParam> sampleFactorDataParamMap = finalFactorDataMap.get(item);
                sampleFactorDataParamMap = (sampleFactorDataParamMap != null ? sampleFactorDataParamMap : new HashMap<>());

                //实验室因子
                Map<String, String> factorMap = sampIdLaborFactorValMap.get(item);
                if (factorMap == null) {
                    factorMap = new HashMap<>();
                    sampIdLaborFactorValMap.put(item, factorMap);
                } else {
                    factorIds.addAll(factorMap.keySet());
                }

                //采样因子数据
                for (Map.Entry<String, SampleFactorDataParam> entry : sampleFactorDataParamMap.entrySet()) {
                    String factorKey = entry.getKey();
                    //以实验室检测因子为主；若实验室没有，则以现场采样为主
                    if (!factorIds.contains(factorKey)) {
                        factorIds.add(factorKey);
                        factorMap.put(factorKey, BusiUtils.getSingleValByJson(entry.getValue().getFactorData()));
                    }
                }

            });

            LinkedHashMap<String, FactorParam> laborFactorMap = secdClassLaborFactorMap.get(sedClassId);
            if (laborFactorMap == null) {
                laborFactorMap = new LinkedHashMap<>();
            }
            LinkedHashMap<String, FactorParam> headMap = laborFactorMap;
            factorIds.stream().forEach(item -> {
                FactorParam factorParam = headMap.get(item);
                if (factorParam == null) {
                    FactorParam param = new FactorParam();
                    param.setCheckStandardId(item);
                    FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item);
                    if (factorMethodId != null) {
                        param.setUnitVal(factorMethodId.getDefaultUnitName())
                                .setFactorName(factorMethodId.getFactorName());
                    }
                    headMap.put(item, param);
                }

            });

            List<FactorParam> headParams = new ArrayList<>();
            headMap.forEach((key, val) -> {
                headParams.add(val);
            });


            List<ReportAttachData> fileDatas = new ArrayList<>();
            //因子集合
            List<List<FactorParam>> headList = StringUtils.splistList(headParams, headSize);
            headList.stream().forEach(item -> {
                List<String> checkStandardIds = item.stream().map(subItem -> subItem.getCheckStandardId()).collect(Collectors.toList());
                List<String> headNams = item.stream().map(subItem -> subItem.getFactorName()).collect(Collectors.toList());
                RowRenderData headData = Rows.of("", "", "").center().create();
                List<CellRenderData> cells = headData.getCells();
                headNams.stream().forEach(subItem -> {
                    cells.add(Cells.of(subItem).create());
                });
                int size = cells.size();
                if (size < totalCells) {
                    for (int i = 0; i < totalCells - size; i++) {
                        cells.add(Cells.of("").create());
                    }
                }


                List<String> tailNames = item.stream().map(subItem -> subItem.getUnitVal()).collect(Collectors.toList());

                RowRenderData tailData = Rows.of("").center().create();
                List<CellRenderData> cells2 = tailData.getCells();
                tailNames.stream().forEach(subItem -> {
                    cells2.add(Cells.of(subItem).create());
                });
                int size2 = cells2.size();
                if (size2 < tailSize) {
                    for (int i = 0; i < tailSize - size2; i++) {
                        cells2.add(Cells.of("").create());
                    }
                }


                //一次表头，至少lists的size个
                List<List<String>> lists = StringUtils.splistList(sampIds, 6);
                lists.stream().forEach(sampItems -> {
                    ReportAttachData attachData = new ReportAttachData();
                    fileDatas.add(attachData);
                    attachData.setSecdClass(secdName);
                    ReportItemData itemData = new ReportItemData();
                    attachData.setItemData(itemData);
                    itemData.setHeadRow(0);
                    itemData.setHeadStartColumn(3);
                    itemData.setTailRow(8);
                    itemData.setTotalRow(6);
                    itemData.setStartRow(1);
                    itemData.setTailStartColumn(1)
                            .setHeadData(headData)
                            .setTailData(tailData);

                    List<RowRenderData> rows = new ArrayList<>();
                    itemData.setTemplatePath(imagePath);
                    itemData.setRows(rows);

                    for (int i = 0; i < sampItems.size(); i++) {
                        String sampleId = sampItems.get(i);
                        ReportSampleVo sampleVo = sampleVoMap.get(sampleId);
                        String sampleData = sampleVo.getSampleData();
                        Map<String, Object> sampleRecordDataMap = BusiUtils.parseSampleDataJson(sampleData);
                        //地表水和雨水
                        //感官描述
                        RecordValItem organoleptic = (RecordValItem) sampleRecordDataMap.get("organoleptic");
                        String organolepticVal = BusiUtils.getSingleVal(organoleptic);
                        Rows.RowBuilder inst = Rows.of();
                        CellRenderData checkPointCell = Cells.of(sampleVo.getFactorPoint()).create();
                        inst.addCell(checkPointCell);
                        CellRenderData properties = Cells.of(organolepticVal).create();
                        inst.addCell(properties);
//                        CellRenderData collectTime = Cells.of(DateUtils.getDefaultTimeDateYYYYmmddHHmm(sampleVo.getCollectTime())).create();
                        CellRenderData collectTime = Cells.of(sampleVo.getCollectDate()).create();
                        inst.addCell(collectTime);


                        Map<String, String> rowFactor = sampIdLaborFactorValMap.get(sampleId);

                        //判断所有的key是否在表头里
                        for (int k = 0; k < checkStandardIds.size(); k++) {
                            String val = rowFactor.get(checkStandardIds.get(k));
                            CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(val);
                            inst.addCell(factorValCell);

                        }


                        RowRenderData data = inst.center().create();
                        List<CellRenderData> renderCellData = data.getCells();
                        int cellSize = renderCellData.size();
                        if (cellSize < totalCells) {
                            for (int j = 0; j < totalCells - cellSize; j++) {
                                renderCellData.add(Cells.of("").create());
                            }
                        }

                        rows.add(data);
                    }

                    log.info("数据->" + secdName + "->" + attachData);
                });

            });


            List<SubFactorData> subFactorDatas = null;
            //实验室子因子
            Map<String, Map<Long, LinkedHashMap<String, String>>> subFactorValMap = dataVo.getSampIdLaborSubFactorValMap();
            if (subFactorValMap != null && !subFactorValMap.isEmpty()) {
                String secdClassName = dictUtils.getFactorClassMap().get(sedClassId);
                Map<Long, String> checkFactorIdHomeFactorNameMap = dataVo.getCheckFactorIdHomeFactorNameMap();
                subFactorDatas = XwpsUtils.buildSubFactorDatas(
                        secdClassName,
                        imagePath,
                        sampleVoMap,
                        subFactorValMap,
                        checkFactorIdHomeFactorNameMap,
                        sampIds,
                        6,
                        4
                );
            }


            //合并文件
            NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
            Configure config = Configure.builder().bind("report_table", new ReportTablePolicy()).build();
            for (int k = 0; k < fileDatas.size(); k++) {
                XWPFTemplate attachTemplate = null;
                try {
                    attachTemplate = XWPFTemplate.compile(templatePath + "/surfacewater_template.docx", config).render(fileDatas.get(k));
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


            if (subFactorDatas != null) {
                config = Configure.builder().bind("rep_table", new SubFactorPolicy()).build();
                for (int i = 0; i < subFactorDatas.size(); i++) {
                    //合并文件
                    XWPFTemplate attachTemplate = null;
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
}