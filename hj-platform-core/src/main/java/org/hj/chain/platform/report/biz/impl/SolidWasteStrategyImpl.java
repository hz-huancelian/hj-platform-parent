package org.hj.chain.platform.report.biz.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.hj.chain.platform.mapper.FactorClassInfoMapper;
import org.hj.chain.platform.model.FactorClassInfo;
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
 * @description TODO 固废 005001 生活垃圾 006001  室内空气002005
 * @Iteration : 1.0
 * @Date : 2021/6/21  6:39 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/21    create
 */
@Slf4j
@Component
@SecdClassType(value = "005001,006001,002005")
public class SolidWasteStrategyImpl implements IReportStrategy {

    @Autowired
    private FactorClassInfoMapper factorClassInfoMapper;
    @Autowired
    private DictUtils dictUtils;

    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //检测点位-检测时间-样品性状-检测项目-检测结果（按照时间分组）

        FactorClassInfo dbClass = factorClassInfoMapper.selectOne(Wrappers.<FactorClassInfo>lambdaQuery()
                .select(FactorClassInfo::getName)
                .eq(FactorClassInfo::getId, sedClassId));
        String name = dbClass.getName();

        //样品ID-样品性状、采集时间
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();
        //二级分类：样品集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();
        //样品：对应的key val
        Map<String, Map<String, String>> sampIdValMap = dataVo.getSampIdLaborFactorValMap();
        //二级分类下的因子信息
        Map<String, LinkedHashMap<String, FactorParam>> secdClassFactorMap = dataVo.getSecdClassLaborFactorMap();

        List<String> sampIds = secdClassSampMap.get(sedClassId);
        List<ReportSampleVo> reportSampleVos = new ArrayList<>(sampIds.size());
        sampIds.stream().forEach(item -> {
            ReportSampleVo reportSampleVo = sampleVoMap.get(item);
            reportSampleVos.add(reportSampleVo);
        });

        //日期排序
        //按照日期升序排序
        List<ReportSampleVo> dateAscSamples = reportSampleVos.stream().sorted(Comparator.comparing(ReportSampleVo::getCollectDate)).collect(Collectors.toList());
        //因子名称
        LinkedHashMap<String, FactorParam> factorParamMap = secdClassFactorMap.get(sedClassId);

        //先按日期分组
        LinkedHashMap<String, List<RowRenderData>> rowDataMap = new LinkedHashMap<>();

        dateAscSamples.stream().forEach(item -> {
            //样品ID
            String sampItemId = item.getSampItemId();
            //点位
            String factorPoint = item.getFactorPoint();
            //采集日期
            String collectDate = item.getCollectDate();
            //性状
            String sampleProperties = item.getSampleProperties();

            List<RowRenderData> dataList = rowDataMap.get(collectDate);
            if (dataList == null) {
                dataList = new ArrayList<>();
                rowDataMap.put(collectDate, dataList);
            }
            Map<String, String> factorMap = sampIdValMap.get(sampItemId);
            Map<String, String> finalFactorMap = factorMap != null ? factorMap : new HashMap<>();
            for (Map.Entry<String, String> entry : finalFactorMap.entrySet()) {
                String factorKey = entry.getKey();
                String factorVal = entry.getValue();
                //检测值
                CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(factorVal);
                FactorParam factorParam = factorParamMap.get(factorKey);
                RowRenderData data = Rows.of().center().create();
                data.addCell(Cells.of(factorPoint).center().create());
                data.addCell(Cells.of(collectDate).center().create());
                data.addCell(Cells.of(sampleProperties).center().create());
                data.addCell(Cells.of(factorParam.getFactorName()).center().create());
                data.addCell(factorValCell);
                data.addCell(Cells.of("").center().create());
                data.addCell(Cells.of(factorParam.getUnitVal()).center().create());
//                RowRenderData data = Rows.of(factorPoint, collectDate, sampleProperties, factorParam.getFactorName(), "factorVal", "", factorParam.getUnitVal()).center().create();
                dataList.add(data);
            }

        });

        SolidWasteData wasteData = new SolidWasteData();
        wasteData.setSecdClass(name);
        SolidWasteItemData itemData = new SolidWasteItemData();
        wasteData.setItemData(itemData);
        itemData.setTemplatePath(imagePath);
        itemData.setRowMap(rowDataMap);

        log.info("数据->" + secdName + "->" + wasteData);

        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("rep_table", new SolidWastePolicy()).build();
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile(templatePath + "/solid_waste_template.docx", config).render(wasteData);
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

        if (sedClassId.equals("002005")) {
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
            if (subFactorDatas != null) {
                config = Configure.builder().bind("rep_table", new SubFactorPolicy()).build();
                for (int i = 0; i < subFactorDatas.size(); i++) {
                    //合并文件
                    XWPFTemplate subAttachTemplate = null;
                    try {
                        subAttachTemplate = XWPFTemplate.compile(templatePath + "/water_subfactor_template.docx", config).render(subFactorDatas.get(i));
                        NiceXWPFDocument xwpfDocument = subAttachTemplate.getXWPFDocument();
                        temDoc = temDoc.merge(xwpfDocument);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (subAttachTemplate != null) {
                            try {
                                subAttachTemplate.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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