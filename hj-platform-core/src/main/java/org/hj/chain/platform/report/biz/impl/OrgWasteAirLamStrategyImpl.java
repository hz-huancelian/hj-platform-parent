package org.hj.chain.platform.report.biz.impl;

import cn.hutool.core.util.StrUtil;
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
 * @description TODO 有组织废气（油烟）
 * @Iteration : 1.0
 * @Date : 2021/6/21  6:30 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/21    create
 */
@Slf4j
@Component
@SecdClassType(value = "0020021")
public class OrgWasteAirLamStrategyImpl implements IReportStrategy {
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
//        //二级分类下的因子信息
        Map<String, LinkedHashMap<String, FactorParam>> secdClassFactorMap = dataVo.getSecdClassLaborFactorMap();

        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);

        //获取二级分类下的样品集合
        List<String> sampIdList = secdClassSampMap.get(sedClassId);
        //获取该二级分类下关联的样品采集信息,并按照日期降序排序
        List<ReportSampleVo> sampleVos = sampIdList.stream().map(item -> sampleVoMap.get(item)).sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());


        int startCellCount = 3;
        int totalCellCount = 6;


        List<OrgWasteAirLamData> dataList = new ArrayList<>();
        Map<String, Map<String, List<ReportSampleVo>>> factorPointCollectDateMap = sampleVos.stream().collect(Collectors.groupingBy(ReportSampleVo::getFactorPoint, Collectors.groupingBy(ReportSampleVo::getCollectDate)));
        factorPointCollectDateMap.forEach((factorPoint, collectDateMap) -> {

            collectDateMap.forEach((collectDate, sampleVoList) -> {
                List<List<ReportSampleVo>> splistList = StringUtils.splistList(sampleVoList, 5);
                splistList.stream().forEach(list -> {
                    OrgWasteAirLamData lamData = new OrgWasteAirLamData();
                    lamData.setFactorPoint(factorPoint)
                            .setCollectDate(collectDate);
                    OrgWasteAirLamItemData airItemData = new OrgWasteAirLamItemData();
                    RowRenderData renderData = Rows.of().center().create();
                    airItemData.setData(renderData);
                    lamData.setItemData(airItemData);
                    for (int i = 0; i < startCellCount; i++) {
                        renderData.addCell(null);
                    }

                    ReportSampleVo lastData = list.get(list.size() - 1);
                    String sampleData = lastData.getSampleData();
                    Map<String, Object> sampleDataMap = BusiUtils.parseSampleDataJson(sampleData);
                    RecordValItem exhaustHeight = (RecordValItem) sampleDataMap.get("exhaustHeight");
                    lamData.setExhaustHeight(BusiUtils.getSingleVal(exhaustHeight));
                    RecordValItem area = (RecordValItem) sampleDataMap.get("area");
                    lamData.setSectionalArea(BusiUtils.getSingleVal(area));
                    StringBuilder factorValBuilder = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        ReportSampleVo item = list.get(i);
                        String sampItemId = item.getSampItemId();
                        Map<String, String> factorValMap = sampIdValMap.get(sampItemId);
                        List<String> vals = factorValMap.values().stream().collect(Collectors.toList());
                        String val = vals.get(0);
                        if (StrUtil.isNotBlank(val) && val.contains("×")) {
                            String[] valArray = val.split("×");
                            factorValBuilder.append(valArray[0]).append(" × 10").append("^").append(valArray[1]).append("、");
                        } else {
                            factorValBuilder.append(val).append("、");
                        }


                        String sampleDataItem = item.getSampleData();
                        Map<String, Object> sampleDataItemMap = BusiUtils.parseSampleDataJson(sampleDataItem);
                        RecordValItem airVolume = (RecordValItem) sampleDataItemMap.get("airVolume");
                        String ariVolumeVal = BusiUtils.getSingleVal(airVolume);
                        if (i == 0) {
                            lamData.setAirVolume0(ariVolumeVal);
                        } else if (i == 1) {
                            lamData.setAirVolume1(ariVolumeVal);
                        } else if (i == 2) {
                            lamData.setAirVolume2(ariVolumeVal);
                        } else if (i == 3) {
                            lamData.setAirVolume3(ariVolumeVal);
                        } else if (i == 4) {
                            lamData.setAirVolume4(ariVolumeVal);
                        }
                    }

                    String factorVal = factorValBuilder.substring(0, factorValBuilder.lastIndexOf("、"));
                    factorVal = factorVal + "的平均值";
                    CellRenderData factorValCell = Cells.of(factorVal).bgColor("FF0000").center().create();

                    renderData.addCell(factorValCell);

                    for (int i = 0; i < totalCellCount - startCellCount - 1; i++) {
                        renderData.addCell(null);
                    }

                    dataList.add(lamData);
                    log.info("数据->" + secdName + "->" + lamData);
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
        Configure config = Configure.builder().bind("rep_table", new OrgWasteAirLamPolicy()).build();
        for (int k = 0; k < dataList.size(); k++) {
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/oran_exh_template.docx", config).render(dataList.get(k));
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
                    attachTemplate = XWPFTemplate.compile(templatePath + "/org_unorg_subfactor_template.docx", config).render(subFactorDatas.get(i));
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