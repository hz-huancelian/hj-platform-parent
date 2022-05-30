package org.hj.chain.platform.report.biz.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.RowRenderData;
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
 * @description TODO 车间无组织废气
 * @Iteration : 1.0
 * @Date : 2021/6/10  9:54 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/10    create
 */
@Slf4j
@Component
@SecdClassType(value = "002004")
public class InnerUnOrganExhStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //检测日期-检测点位-检测因子{检测次数：检测结果}-》检测日期-检测点位-检测因子-检测次数-检测结果

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
        //先按日期分组、点位分组、因子分组
        LinkedHashMap<String, Map<String, Map<String, List<RowRenderData>>>> rowDataMap = new LinkedHashMap<>();
        sampleVos.stream().forEach(item -> {

            //采样日期
            String collectDate = item.getCollectDate();
            //检测点位
            String factorPoint = item.getFactorPoint();
            //检测频次
            Integer frequency = item.getFrequency();
            //样品ID
            String sampItemId = item.getSampItemId();
            //检测因子：
            Map<String, String> factorValMap = sampIdValMap.get(sampItemId);

            factorValMap.forEach((key, val) -> {
                Map<String, Map<String, List<RowRenderData>>> rowsMap = rowDataMap.get(collectDate);
                if (rowsMap == null) {
                    rowsMap = new HashMap<>();
                    rowDataMap.put(collectDate, rowsMap);
                }

                Map<String, List<RowRenderData>> factorPointMap = rowsMap.get(factorPoint);
                if (factorPointMap == null) {
                    factorPointMap = new HashMap<>();
                    rowsMap.put(factorPoint, factorPointMap);
                }

                List<RowRenderData> list = factorPointMap.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                    factorPointMap.put(key, list);
                }

                RowRenderData renderData = new RowRenderData();
                //日期
                renderData.addCell(Cells.of(collectDate).center().create());
                //检测点位
                renderData.addCell(Cells.of(factorPoint).center().create());
                //检测项目
                renderData.addCell(Cells.of(factorParamMap.get(key).getFactorName()).center().create());
                //检测频次
                renderData.addCell(Cells.of(String.valueOf(frequency)).center().create());
                //检测值
                CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(val);
                renderData.addCell(factorValCell);
                renderData.addCell(Cells.create(""));
                list.add(renderData);

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

        log.info("数据->" + secdName + "->" + rowDataMap);
        InnnerUnOrganExhData exhData = new InnnerUnOrganExhData();
        InnerUnOrganExhItemData itemData = new InnerUnOrganExhItemData();
        itemData.setTemplatePath(imagePath);
        itemData.setRowDataMap(rowDataMap);
        exhData.setUnOrganExhItemData(itemData);
        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("exh_table", new InnerUnOrganExhPolicy()).build();
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile(templatePath + "/innerunorganexh_template.docx", config).render(exhData);
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