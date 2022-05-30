package org.hj.chain.platform.report.biz.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
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
 * @description TODO 有组织废气(通用)
 * @Iteration : 1.0
 * @Date : 2021/6/20  1:41 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/20    create
 */
@Slf4j
@Component
@SecdClassType(value = "002002")
public class OrgWasterAirStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private IFactorService factorService;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //检测项目-检测次数-检测结果-null   同一日期同一点位，下因子的检测信息

        //样品ID-样品性状、采集时间
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();
        //二级分类：样品集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();
        //样品：对应的key val
        Map<String, Map<String, String>> sampIdValMap = dataVo.getSampIdLaborFactorValMap();
        //二级分类下的因子信息
        Map<String, LinkedHashMap<String, FactorParam>> secdClassFactorMap = dataVo.getSecdClassLaborFactorMap();

        //获取二级分类下的样品集合
        List<String> sampIdList = secdClassSampMap.get(sedClassId);
        //获取该二级分类下关联的样品采集信息,并按照日期降序排序
        List<ReportSampleVo> sampleVos = sampIdList.stream().map(item -> sampleVoMap.get(item)).sorted(Comparator.comparing(ReportSampleVo::getCollectDate).reversed()).collect(Collectors.toList());
        //因子参数map
        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);
        Map<String, FactorParam> finalFctorParamMap = (factorParamMap != null ? factorParamMap : new LinkedHashMap<String, FactorParam>());
        //先按日期分组、点位分组、因子分组
//        LinkedHashMap<String, LinkedHashMap<String, List<RowRenderData>>> rowDataMap = new LinkedHashMap<>();
        List<SampleFactorDataParam> sampleFactors = dataVo.getSceneSampleFactorDataMap().get(sedClassId);
        Map<String, Map<String, SampleFactorDataParam>> factorDataMap = new HashMap<>();
        if (sampleFactors != null && !sampleFactors.isEmpty()) {
            factorDataMap = sampleFactors.stream().collect(Collectors.groupingBy(SampleFactorDataParam::getSampleNo, Collectors.toMap(SampleFactorDataParam::getCheckStandardId, Function.identity())));
        }

        Map<String, Map<String, SampleFactorDataParam>> finalFactorDataMap = factorDataMap;

        List<OrganWaterAirData> dataList = new ArrayList<>();
        //按照日期和点位分组
        Map<String, Map<String, List<ReportSampleVo>>> groupMap = sampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getSampItemId))
                .collect(Collectors.groupingBy(ReportSampleVo::getCollectDate, Collectors.groupingBy(ReportSampleVo::getFactorPoint)));
        groupMap.keySet().stream().sorted().forEach(collectDate -> {
            Map<String, List<ReportSampleVo>> factorGroupMap = groupMap.get(collectDate);
            factorGroupMap.forEach((factorPoint, sampList) -> {
                List<List<ReportSampleVo>> lists = StringUtils.splistList(sampList, 3);
                lists.stream().forEach(sampleVoList -> {
                    OrganWaterAirData organData = new OrganWaterAirData();
                    organData.setCollectDate(collectDate);
                    organData.setFactorPoint(factorPoint);
                    OrganWaterAirItemData itemData = new OrganWaterAirItemData();
                    organData.setItemData(itemData);
                    LinkedHashMap<String, List<RowRenderData>> rowsMap = new LinkedHashMap<>();
                    for (int i = 0; i < sampleVoList.size(); i++) {
                        boolean isExist = false;
                        ReportSampleVo item = sampleVoList.get(i);
                        //检测频次
                        Integer frequency = item.getFrequency();
                        //样品ID
                        String sampItemId = item.getSampItemId();

                        //采样记录因子记录信息
                        //采样记录的因子信息
                        Map<String, SampleFactorDataParam> sampleFactorDataParamMap = finalFactorDataMap.get(sampItemId);
                        sampleFactorDataParamMap = sampleFactorDataParamMap != null ? sampleFactorDataParamMap : new HashMap<>();

                        //采样记录的二级分类信息
                        String sampleData = item.getSampleData();
                        Map<String, Object> sampleRecordSecdClassDataMap = BusiUtils.parseSampleDataJson(sampleData);

                        //烟气温度
                        RecordValItem gasTemperature = (RecordValItem) sampleRecordSecdClassDataMap.get("gasTemperature");
                        //烟气流速
                        RecordValItem gasVelocity = (RecordValItem) sampleRecordSecdClassDataMap.get("gasVelocity");
                        //标态烟气流量
                        RecordValItem smokeFlowRate = (RecordValItem) sampleRecordSecdClassDataMap.get("smokeFlowRate");
                        if (i == 0) {
                            log.info("start...");
                            RecordValItem sectionalArea = (RecordValItem) sampleRecordSecdClassDataMap.get("sectionalArea");
                            if (sectionalArea != null) {
                                organData.setSectionalArea(BusiUtils.getSingleVal(sectionalArea));
                            }
                            RecordValItem exhaustHeight = (RecordValItem) sampleRecordSecdClassDataMap.get("exhaustHeight");
                            if (exhaustHeight != null) {
                                organData.setExhaustHeight(BusiUtils.getSingleVal(exhaustHeight));
                            }

                            if (gasTemperature != null) {
                                log.info("start...");
                                isExist = true;
                                organData.setGasTemperature(BusiUtils.getSingleVal(gasTemperature));
                            }
                            if (gasVelocity != null) {
                                log.info("start...");
                                isExist = true;
                                organData.setGasVelocity(BusiUtils.getSingleVal(gasVelocity));
                            }

                            if (smokeFlowRate != null) {
                                log.info("start...");
                                isExist = true;
                                organData.setSmokeFlowRate(BusiUtils.getSingleVal(smokeFlowRate));
                            }

                        } else if (i == 1) {
                            if (gasTemperature != null) {
                                isExist = true;
                                organData.setGasTemperature2(BusiUtils.getSingleVal(gasTemperature));
                            }

                            if (gasVelocity != null) {
                                isExist = true;
                                organData.setGasVelocity2(BusiUtils.getSingleVal(gasVelocity));
                            }

                            if (smokeFlowRate != null) {
                                isExist = true;
                                organData.setSmokeFlowRate2(BusiUtils.getSingleVal(smokeFlowRate));
                            }
                        } else {
                            if (gasTemperature != null) {
                                isExist = true;
                                organData.setGasTemperature3(BusiUtils.getSingleVal(gasTemperature));
                            }
                            if (gasVelocity != null) {
                                isExist = true;
                                organData.setGasVelocity3(BusiUtils.getSingleVal(gasVelocity));
                            }

                            if (smokeFlowRate != null) {
                                isExist = true;
                                organData.setSmokeFlowRate3(BusiUtils.getSingleVal(smokeFlowRate));
                            }
                        }

                        //检测因子：
                        Map<String, String> factorValMap = sampIdValMap.get(sampItemId);
                        Map<String, String> finalFactorValMap = factorValMap != null ? factorValMap : new HashMap<>();
                        if (isExist) {
                            sampleFactorDataParamMap.forEach((factorKey, factorDataParam) -> {
                                finalFactorValMap.put(factorKey, BusiUtils.getSingleValByJson(factorDataParam.getFactorData()));
                                FactorMethodInfoVo factorMethod = factorService.findFactorMethodById(factorDataParam.getCheckStandardId());
                                FactorParam factorParam = new FactorParam();
                                factorParam.setFactorName(factorDataParam.getFactorName())
                                        .setCheckStandardId(factorKey);
                                if (factorMethod != null) {
                                    factorParam.setUnitVal(factorMethod.getDefaultUnitName())
                                            .setDataEntryStep(factorMethod.getDataEntryStep());
                                }
                                finalFctorParamMap.put(factorKey, factorParam);
                            });
                        }

                        finalFactorValMap.forEach((key, val) -> {

                            List<RowRenderData> list = rowsMap.get(key);
                            if (list == null) {
                                list = new ArrayList<>();
                                RowRenderData renderData = new RowRenderData();
                                //检测项目
                                renderData.addCell(Cells.create(""));
                                //检测频次
                                renderData.addCell(Cells.of("均值").center().create());
                                //检测值（实测排放浓度（mg/m3）/折算排放浓度（mg/m3）/排放速率（kg/h））
                                renderData.addCell(Cells.create(""));
                                renderData.addCell(Cells.create(""));
                                renderData.addCell(Cells.create(""));
                                list.add(renderData);
                                RowRenderData renderData2 = new RowRenderData();
                                //检测项目
                                renderData2.addCell(Cells.create(""));
                                //检测频次
                                renderData2.addCell(Cells.of("标准限值").center().create());
                                //检测值（实测排放浓度（mg/m3）/折算排放浓度（mg/m3）/排放速率（kg/h））
                                renderData2.addCell(Cells.create(""));
                                renderData2.addCell(Cells.create(""));
                                renderData2.addCell(Cells.create(""));
                                list.add(renderData2);
                                rowsMap.put(key, list);
                            }

                            RowRenderData renderData = new RowRenderData();
                            //检测项目
                            String factorName = finalFctorParamMap.get(key).getFactorName();

                            renderData.addCell(Cells.of(factorName).center().create());
                            //检测频次
                            renderData.addCell(Cells.of(String.valueOf(frequency)).center().create());
                            //检测值
                            CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(val);
                            renderData.addCell(factorValCell);
                            renderData.addCell(Cells.create(""));
                            renderData.addCell(Cells.create(""));
                            list.add(renderData);

                        });
                    }

                    itemData.setRowDataMap(rowsMap);
                    dataList.add(organData);
                    log.info("数据->" + secdName + "->" + organData);
                });
            });

        });


        List<SubFactorData> subFactorDatas = null;
        Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdLaborSubFactorValMap = dataVo.getSampIdLaborSubFactorValMap();
        if (sampIdLaborSubFactorValMap != null && !sampIdLaborSubFactorValMap.isEmpty()) {
            String secdClassName = dictUtils.getFactorClassMap().get(sedClassId);
            subFactorDatas = XwpsUtils.buildSubFactorDatas(
                    secdClassName,
                    imagePath,
                    sampleVoMap,
                    sampIdLaborSubFactorValMap,
                    dataVo.getCheckFactorIdHomeFactorNameMap(),
                    sampIdList,
                    5,
                    3
            );
        }

        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("rep_table", new OrganWasterAirPolicy()).build();
        for (OrganWaterAirData data : dataList) {
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/oran_exh_common_template.docx", config).render(data);
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
                SubFactorData data = subFactorDatas.get(i);
                //合并文件
                XWPFTemplate attachTemplate = null;
                try {
                    attachTemplate = XWPFTemplate.compile(templatePath + "/org_unorg_subfactor_template.docx", config).render(data);
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
            itemData.setDataMap(equipmentVoMap);
            itemData.setTemplatePath(imagePath);
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