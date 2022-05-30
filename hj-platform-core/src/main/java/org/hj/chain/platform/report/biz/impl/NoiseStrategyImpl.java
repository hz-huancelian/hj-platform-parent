package org.hj.chain.platform.report.biz.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.report.component.SecdClassType;
import org.hj.chain.platform.vo.record.RecordValItem;
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
 * @description TODO 噪声 （一个因子一模板页）
 * @Iteration : 1.0
 * @Date : 2021/6/21  6:37 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/21    create
 */
@Slf4j
@Component
@SecdClassType(value = "004001,004002,004003,004004,004005")
public class NoiseStrategyImpl implements IReportStrategy {
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String imagePath;

    @Override
    public void fillTemplate(ReportGenDataVo dataVo, String sourceFile, String sedClassId, String reportGenPath, String templatePath) throws IOException {

        log.info("噪声模板解析");
        String secdName = dictUtils.getFactorClassMap().get(sedClassId);
        log.info("数据开始记载：" + secdName);
        //二级分类下的样品ID集合
        Map<String, List<String>> secdClassSampMap = dataVo.getSecdClassSampMap();

        //样品ID集合
        List<String> sampleIds = secdClassSampMap.get(sedClassId);

        //采样数据：key样品ID，
        Map<String, ReportSampleVo> sampleVoMap = dataVo.getSampleVoMap();

        //噪声相关的样品信息
        List<ReportSampleVo> sampleVos = sampleIds.stream().map(item -> sampleVoMap.get(item)).collect(Collectors.toList());
        //日期-点位记录
        //区域环境噪声
        List<LinkedHashMap<String, List<RowRenderData>>> modelType0List = new ArrayList<>();
        List<Map<String, StringBuilder>> modelType0WeatherList = new ArrayList<>();
        //环境振动
        List<LinkedHashMap<String, List<RowRenderData>>> modelType1List = new ArrayList<>();
        List<Map<String, StringBuilder>> modelType1WeatherList = new ArrayList<>();
        //道路交通
        List<LinkedHashMap<String, List<NoiseType2TemplateData>>> modelType2List = new ArrayList<>();
        //工业企业厂界噪声
        List<LinkedHashMap<String, List<RowRenderData>>> modelType3List = new ArrayList<>();
        List<Map<String, StringBuilder>> modelType3WeatherList = new ArrayList<>();
        //社会环境噪声
        List<LinkedHashMap<String, List<RowRenderData>>> modelType4List = new ArrayList<>();
        List<Map<String, StringBuilder>> modelType4WeatherList = new ArrayList<>();
        Map<String, List<ReportSampleVo>> factorPointGroupMap = sampleVos.stream().collect(Collectors.groupingBy(ReportSampleVo::getFactorGroupKey));
        factorPointGroupMap.forEach((factorGroupKey, rsvs1) -> {
            Map<String, List<ReportSampleVo>> groupKeyMap = rsvs1.stream().collect(Collectors.groupingBy(ReportSampleVo::getGroupKey));
            //分组key是一个因子一个点位多个频次
            LinkedHashMap<String, List<RowRenderData>> dateRrdMap = new LinkedHashMap<>();
            Map<String, StringBuilder> weatherMap = new HashMap<>();
            LinkedHashMap<String, List<NoiseType2TemplateData>> modelTypeDateRrdMap = new LinkedHashMap<>();
            groupKeyMap.forEach((groupKey, rsvs2) -> {
                //每天的频次
                Map<String, List<ReportSampleVo>> originGroupKeyMap = rsvs2.stream().collect(Collectors.groupingBy(ReportSampleVo::getOriginGroupKey));
                originGroupKeyMap.forEach((originGroupKey, rsvs) -> {
                    //同一样品，多个频次数据
                    List<ReportSampleVo> sortedSampleVos = rsvs.stream().sorted(Comparator.comparing(ReportSampleVo::getSampItemId)).collect(Collectors.toList());
                    for (int i = 0; i < sortedSampleVos.size(); i++) {
                        ReportSampleVo item = sortedSampleVos.get(i);
                        //采集日期
                        String collectDate = item.getCollectDate();
                        //检测点位
                        String factorPoint = item.getFactorPoint();
                        //采样数据
                        String sampleData = item.getSampleData();
                        Map<String, Object> dataMap = BusiUtils.parseSampleDataJson(sampleData);
                        //风速
                        RecordValItem windSpeed = (RecordValItem) dataMap.get("windSpeed");
                        //测点编号
                        RecordValItem testNum = (RecordValItem) dataMap.get("testNum");
                        //噪声类型
                        if (!sedClassId.equals("004005")) {//道路交通
                            //两天一张表；一条数据对应两个样品，对应一个测点的
                            //天气情况
                            String sampleProperties = item.getSampleProperties();
                            sampleProperties = sampleProperties != null ? sampleProperties : "";
                            RecordValItem leqdb = (RecordValItem) dataMap.get("leqdb");
                            //采集开始时间
//                            String sampleStartDate = item.getSampleStartDate();
//                            sampleStartDate = sampleStartDate != null ? sampleStartDate : "";
                            //采集结束时间
//                            String sampleEndDate = item.getSampleEndDate();
//                            sampleEndDate = sampleEndDate != null ? sampleEndDate : "";
                            List<RowRenderData> rrdList = dateRrdMap.get(collectDate);
                            if (rrdList == null) {
                                rrdList = new ArrayList<>();
                                dateRrdMap.put(collectDate, rrdList);
                            }
                            if (sedClassId.equals("004001") || sedClassId.equals("004002") || sedClassId.equals("004003")) {//区域环境噪声(004001)//工业企业厂界噪声(004002)//社会环境噪声(004003)
                                log.info("groupKey->" + factorGroupKey);
                                if ((i + 1) % 2 == 0) {
                                    //夜间
                                    int size = rrdList.size();
                                    RowRenderData rdata = rrdList.get(size - 1);
                                    List<CellRenderData> cells = rdata.getCells();
                                    CellRenderData vlNumCell = Cells.of(BusiUtils.getSingleVal(leqdb)).center().create();
                                    cells.add(vlNumCell);//检测结果
                                    cells.add(Cells.of("").center().create());//标准限值
                                    CellRenderData checkTimeData = cells.get(3);
                                    String text = "";
                                    TextRenderData textRenderData = (TextRenderData) (checkTimeData.getParagraphs().get(0).getContents().get(0));
                                    if (textRenderData != null) {
                                        text = textRenderData.getText();
                                    }
                                    text = text + "\r夜间:" + BusiUtils.getSingleVal((RecordValItem) dataMap.get("tiemInterval"));
                                    cells.set(3, Cells.of(text).center().create());
                                    StringBuilder weatherConditon = weatherMap.get(collectDate);
                                    if (weatherConditon != null && weatherConditon.indexOf("夜间") < 0) {
                                        weatherConditon.append("夜间:")
                                                .append(sampleProperties).append(",")
                                                .append("风速").append(BusiUtils.getSingleVal(windSpeed)).append("m/s");
                                    }
                                } else {
                                    //昼间
                                    RowRenderData rdata = Rows.of(collectDate,
                                            BusiUtils.getSingleVal(testNum),//测点编号
                                            factorPoint,//采样点位
                                            "昼间:" + BusiUtils.getSingleVal((RecordValItem) dataMap.get("tiemInterval")),//检测时间
                                            BusiUtils.getSingleVal(leqdb),//检测结果
                                            ""//标准限值
                                    ).center().create();
                                    rrdList.add(rdata);
                                    StringBuilder weatherConditon = weatherMap.get(collectDate);
                                    if (weatherConditon == null) {
                                        weatherConditon = new StringBuilder();
                                        weatherConditon.append(collectDate).append(",").append("昼间:")
                                                .append(sampleProperties).append(",")
                                                .append("风速").append(BusiUtils.getSingleVal(windSpeed)).append("m/s");
                                        weatherMap.put(collectDate, weatherConditon);
                                    }
                                }

                            } else if (sedClassId.equals("004004")) {//环境振动
                                //两天一张表；一条数据对应两个样品，对应一个测点的
                                //天气条件
//                            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
                                //主要声源
                                RecordValItem mainSound = (RecordValItem) dataMap.get("mainVibrationSource");
                                //地面状况
                                RecordValItem groundConditions = (RecordValItem) dataMap.get("groundConditions");
                                //检测结果值
                                RecordValItem VLNum = (RecordValItem) dataMap.get("VLNum");

                                int size = rrdList.size();
                                if ((i + 1) % 2 == 0) {
                                    //夜间
                                    RowRenderData rdata = rrdList.get(size - 1);
                                    List<CellRenderData> cells = rdata.getCells();
                                    //表单一共8列
                                    CellRenderData vlNumCell = Cells.of(BusiUtils.getSingleVal(VLNum)).center().create();
                                    cells.add(vlNumCell);//检测结果
                                    cells.add(Cells.of("").center().create());//标准限值
                                    StringBuilder weatherConditon = weatherMap.get(collectDate);
                                    if (weatherConditon != null && weatherConditon.indexOf("夜间") < 0) {
                                        weatherConditon.append("夜间:")
                                                .append(sampleProperties).append(",")
                                                .append("风速").append(BusiUtils.getSingleVal(windSpeed)).append("m/s");
                                    }
                                } else {
                                    RowRenderData rdata = Rows.of(collectDate,
                                            BusiUtils.getSingleVal(testNum),//测点编号
                                            BusiUtils.getSingleVal(groundConditions),//地面状况
                                            BusiUtils.getSingleVal(mainSound),//主要振源
                                            BusiUtils.getSingleVal(VLNum),//检测结果
                                            ""//标准限值
                                    ).center().create();
                                    rrdList.add(rdata);
                                    StringBuilder weatherConditon = weatherMap.get(collectDate);
                                    if (weatherConditon == null) {
                                        weatherConditon = new StringBuilder();
                                        weatherConditon.append(collectDate).append(",").append("昼间:")
                                                .append(sampleProperties).append(",")
                                                .append("风速").append(BusiUtils.getSingleVal(windSpeed)).append("m/s");
                                        weatherMap.put(collectDate, weatherConditon);
                                    }
                                }

                            }

                        } else {
                            //两天一个表单
                            RecordValItem bigCar = (RecordValItem) dataMap.get("bigCar");
                            String bigCarVal = "";
                            if (bigCar != null) {
                                bigCarVal = BusiUtils.getSingleVal(bigCar);
                            }

                            RecordValItem mediumCar = (RecordValItem) dataMap.get("mediumCar");
                            String mediumCarVal = "";
                            if (mediumCar != null) {
                                mediumCarVal = BusiUtils.getSingleVal(mediumCar);
                            }
                            //天气情况
                            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
                            StringBuilder sb = new StringBuilder("天气:");
                            if (weatherConditions != null) {
                                sb.append(BusiUtils.getSingleVal(weatherConditions));
                            }

                            sb.append("风速:");
                            if (windSpeed != null) {
                                sb.append(BusiUtils.getSingleVal(windSpeed)).append("m/s");
                            }
                            //开始时间
                            RecordValItem startTime = (RecordValItem) dataMap.get("startTime");
                            //结束时间
                            RecordValItem endTime = (RecordValItem) dataMap.get("endTime");
                            String collectTimeRange = "";
                            if (startTime != null) {
                                collectTimeRange = collectTimeRange + BusiUtils.getSingleVal(startTime);
                            }

                            if (endTime != null) {
                                collectTimeRange = collectTimeRange + "-" + BusiUtils.getSingleVal(endTime);
                            }
                            if ((i + 1) % 2 == 0) {
                                //夜间
                                List<NoiseType2TemplateData> tempDatas = modelTypeDateRrdMap.get(collectDate);
                                if (tempDatas != null) {
                                    int size = tempDatas.size();
                                    NoiseType2TemplateData templateData = tempDatas.get(size - 1);
                                    templateData.setDownBigCar(bigCarVal);
                                    templateData.setDownMediumCar(mediumCarVal);
                                    NoiseType2ItemTemplateData itemTemplateData = new NoiseType2ItemTemplateData();
                                    templateData.setDownItemData(itemTemplateData);
                                    List<RowRenderData> rrds = itemTemplateData.getRrdList();
                                    if (rrds == null) {
                                        rrds = new ArrayList<>();
                                        itemTemplateData.setRrdList(rrds);
                                    }
                                    templateData.setDownCollectDate(collectDate);
                                    RecordValItem lep = (RecordValItem) dataMap.get("lep");
                                    RowRenderData rdata = Rows.of(BusiUtils.getSingleVal(testNum),
                                            collectTimeRange,
                                            BusiUtils.getSingleVal(lep),
                                            "",
                                            sb.toString()
                                    ).center().create();
                                    rrds.add(rdata);
                                }

                            } else {
                                //昼间
                                NoiseType2TemplateData templateData = new NoiseType2TemplateData();
                                templateData.setUpBigCar(bigCarVal);
                                templateData.setUpMediumCar(mediumCarVal);
                                NoiseType2ItemTemplateData itemTemplateData = new NoiseType2ItemTemplateData();
                                templateData.setUpItemData(itemTemplateData);
                                List<RowRenderData> rrds = itemTemplateData.getRrdList();
                                if (rrds == null) {
                                    rrds = new ArrayList<>();
                                    itemTemplateData.setRrdList(rrds);
                                }
                                templateData.setFactorPoint(factorPoint);
                                templateData.setUpCollectDate(collectDate);

                                RecordValItem lep = (RecordValItem) dataMap.get("lep");
                                RowRenderData rdata = Rows.of(BusiUtils.getSingleVal(testNum),
                                        collectTimeRange,
                                        BusiUtils.getSingleVal(lep),
                                        "",
                                        sb.toString()
                                ).center().create();
                                rrds.add(rdata);
                                List<NoiseType2TemplateData> tempDatas = modelTypeDateRrdMap.get(collectDate);
                                if (tempDatas == null) {
                                    tempDatas = new ArrayList<>();
                                    modelTypeDateRrdMap.put(collectDate, tempDatas);
                                }
                                tempDatas.add(templateData);

                            }
                        }
                    }

                });
            });

            if (sedClassId.equals("004001")) {
                modelType0List.add(dateRrdMap);
                modelType0WeatherList.add(weatherMap);
            } else if (sedClassId.equals("004004")) {
                modelType1List.add(dateRrdMap);
                modelType1WeatherList.add(weatherMap);
            } else if (sedClassId.equals("004005")) {
                modelType2List.add(modelTypeDateRrdMap);
            } else if (sedClassId.equals("004002")) {
                modelType3List.add(dateRrdMap);
                modelType3WeatherList.add(weatherMap);
            } else if (sedClassId.equals("004003")) {
                modelType4List.add(dateRrdMap);
                modelType4WeatherList.add(weatherMap);
            }
            log.info("数据->" + secdName + "->" + dateRrdMap);
        });

        //区域环境噪声
        log.info("区域环境噪声->" + modelType0List);
        List<NoiseType01TemplateData> noiseType0List = buildNoiseType01TemplateData(secdName, modelType0List, modelType0WeatherList);
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(sourceFile));
        Configure config = Configure.builder().
                bind("rep_table", new NoiseType01TemplatePolicy(true)).
                build();
        for (int i = 0; i < noiseType0List.size(); i++) {
            //合并文件
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/other_noise_template.docx", config).render(noiseType0List.get(i));
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
        //工业企业厂界噪声
        log.info("工业企业厂界噪声->" + modelType3List);
        List<NoiseType01TemplateData> noiseType3List = buildNoiseType01TemplateData(secdName, modelType3List, modelType3WeatherList);
        for (int i = 0; i < noiseType3List.size(); i++) {
            //合并文件
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/other_noise_template.docx", config).render(noiseType3List.get(i));
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
        //社会环境噪声
        log.info("社会环境噪声->" + modelType4List);
        List<NoiseType01TemplateData> noiseType4List = buildNoiseType01TemplateData(secdName, modelType4List, modelType4WeatherList);
        for (int i = 0; i < noiseType4List.size(); i++) {
            //合并文件
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/other_noise_template.docx", config).render(noiseType4List.get(i));
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

        //城市环境振动
        log.info("城市环境振动->" + modelType1List);
        config = Configure.builder().
                bind("rep_table", new NoiseType01TemplatePolicy(false)).
                build();
        List<NoiseType01TemplateData> noiseType1List = buildNoiseType01TemplateData(secdName, modelType1List, modelType1WeatherList);
        for (int i = 0; i < noiseType1List.size(); i++) {
            //合并文件
            XWPFTemplate attachTemplate = null;
            try {
                attachTemplate = XWPFTemplate.compile(templatePath + "/urban_vibration_template.docx", config).render(noiseType1List.get(i));
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

        //道路交通噪声
        log.info("道路交通->" + modelType2List);
        config = Configure.builder()
                .bind("up_rep_table",
                        new NoiseType2TemplatePolicy(5, 5)).bind(
                        "down_rep_table",
                        new NoiseType2TemplatePolicy(14, 5)).build();
        for (LinkedHashMap<String, List<NoiseType2TemplateData>> dataMap : modelType2List) {
            for (Map.Entry<String, List<NoiseType2TemplateData>> entry : dataMap.entrySet()) {
                List<NoiseType2TemplateData> dataList = entry.getValue();
                for (NoiseType2TemplateData item : dataList) {
                    XWPFTemplate attachTemplate = null;
                    try {
                        attachTemplate = XWPFTemplate.compile(templatePath + "/road_noise_template.docx", config).render(item);
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
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

            if (temDoc != null) {
                temDoc.close();
            }
        }

//    }


    }

    /**
     * TODO 其他噪声模板
     *
     * @param modelType0List
     * @param modelType0WeatherList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/18 4:45 下午
     */
    private List<NoiseType01TemplateData> buildNoiseType01TemplateData(String secdName, List<LinkedHashMap<String, List<RowRenderData>>> modelType0List, List<Map<String, StringBuilder>> modelType0WeatherList) {
        List<NoiseType01TemplateData> noiseType0List = new ArrayList<>();
        for (int i = 0; i < modelType0List.size(); i++) {
            //dataMap 是一个点位多个日期的数据
            LinkedHashMap<String, List<RowRenderData>> dataMap = modelType0List.get(i);
            Map<String, StringBuilder> weatherMap = modelType0WeatherList.get(i);
            List<String> collectDateList = dataMap.keySet().stream().sorted().collect(Collectors.toList());
            List<List<String>> tableList = StringUtils.splistList(collectDateList, 2);
            tableList.stream().forEach(table -> {
                NoiseType01TemplateData dataItem = new NoiseType01TemplateData();
                dataItem.setSecdClassName(secdName);
                noiseType0List.add(dataItem);
                NoiseType01ItemTemplateData subDataItem = new NoiseType01ItemTemplateData();
                subDataItem.setTemplatePath(imagePath);
                dataItem.setItemData(subDataItem);
                LinkedHashMap<String, List<RowRenderData>> rowMap = subDataItem.getRowMap();
                table.stream().forEach(collectDate -> {
                    List<RowRenderData> rrdList = dataMap.get(collectDate);
                    rowMap.put(collectDate, rrdList);
                    StringBuilder sb = weatherMap.get(collectDate);
                    String weatherConditions = dataItem.getWeatherConditions();
                    if (weatherConditions != null) {
                        dataItem.setWeatherConditions(weatherConditions + "；\r" + (sb != null ? sb.toString() : "") + ";");
                    } else {
                        dataItem.setWeatherConditions((sb != null ? sb.toString() : ""));

                    }
                });
            });
        }
        return noiseType0List;
    }
}