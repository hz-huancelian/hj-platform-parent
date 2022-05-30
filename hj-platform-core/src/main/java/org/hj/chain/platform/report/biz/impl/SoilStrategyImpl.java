package org.hj.chain.platform.report.biz.impl;

import cn.hutool.core.util.StrUtil;
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
 * @description TODO 土壤、底泥
 * @Iteration : 1.0
 * @Date : 2021/6/21  6:36 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/21    create
 */
@Slf4j
@Component
@SecdClassType(value = "003001,003002")
public class SoilStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //检测点位-采样日期-样品性状-检测因子{子因子：检测结果}-》检测点位-采样日期-样品性状-检测因子-子因子-检测结果-标准限值-单位

        //样品ID-样品性状、采集时间
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();
        //二级分类：样品集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();
        //样品：对应的key val
        Map<String, Map<String, String>> sampIdValMap = dataVo.getSampIdLaborFactorValMap();
        //子因子值
        Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap = dataVo.getSampIdLaborSubFactorValMap();
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
            //样品ID
            String sampItemId = item.getSampItemId();
            //检测因子：
            Map<String, String> factorValMap = sampIdValMap.get(sampItemId);
            String sampleData = item.getSampleData();
            Map<String, Object> sampleRecordDataMap = BusiUtils.parseSampleDataJson(sampleData);
            //性状
            StringBuilder sampleProperties = new StringBuilder();
            //颜色
            RecordValItem color = (RecordValItem) sampleRecordDataMap.get("color");
            String colorVal = BusiUtils.getSingleVal(color);
            colorVal = (color != null ? colorVal : "");
            sampleProperties.append(colorVal);
            //质地
            RecordValItem texture = (RecordValItem) sampleRecordDataMap.get("texture");
            String textureVal = BusiUtils.getSingleVal(texture);
            textureVal = (textureVal != null ? textureVal : "");
            if (StrUtil.isNotBlank(colorVal) || StrUtil.isNotBlank(textureVal)) {
                sampleProperties.append("、");
            }
            sampleProperties.append(textureVal);


            log.info("样品ID[" + sampItemId + "]没有子因子信息");
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
                //点位
                renderData.addCell(Cells.of(factorPoint).center().create());
                //日期
                renderData.addCell(Cells.of(collectDate).center().create());
                //性状
                String str = sampleProperties.toString();
                renderData.addCell(Cells.of((str != null ? str : "")).center().create());
                //检测项目
                renderData.addCell(Cells.of(factorParamMap.get(key).getFactorName()).center().create());
//                //子项目
//                renderData.addCell(Cells.create(""));
                //检测值
                CellRenderData factorValCell = BusiUtils.convertCellRenderDataByFactorVal(val);
                //检测值
                renderData.addCell(factorValCell);
                //标准限值
                renderData.addCell(Cells.create(""));
                //单位
                renderData.addCell(Cells.create(""));
                list.add(renderData);


            });

        });

        SoilData soilData = new SoilData();
        SoilItemData soilItemData = new SoilItemData();
        soilItemData.setRowDataMap(rowDataMap);
        soilItemData.setTemplatePath(imagePath);
        soilData.setItemData(soilItemData);

        log.info("数据->" + secdName + "->" + soilData);
        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().bind("rep_table", new SoilPolicy()).build();
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile(templatePath + "/soil_template.docx", config).render(soilData);
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