package com.hj.chin.platform.sys;

import com.alibaba.fastjson.JSON;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import org.hj.chain.platform.vo.record.RecordValItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/17  1:11 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/17    create
 */

public class PoiTable {
    String resource = "/Users/lijinku/Documents/projects/hj_project/hj-platform-parent/hj-platform-core/src/test/resources/template3.docx";
    PaymentHackData data = new PaymentHackData();

    @BeforeEach
    public void init() {
        data.setTotal("总共：7200");

        List<Goods> goods = new ArrayList<>();
        Goods good = new Goods();
        good.setCount(4);
        good.setName("墙纸");
        good.setDesc("书房卧室");
        good.setDiscount(1500);
        good.setPrice(400);
        good.setTax(new Random().nextInt(10) + 20);
        good.setTotalPrice(1600);
//        good.setPicture(Pictures.ofLocal("src/test/resources/earth.png").size(24, 24).create());
        Labor labor1 = new Labor();
        labor1.setCategory("油漆工2");
        labor1.setPeople(22);
        labor1.setPrice(4002);
        labor1.setTotalPrice(16002);
        good.setLabor(labor1);
        goods.add(good);
        goods.add(good);
        goods.add(good);
        goods.add(good);
        data.setGoods(goods);

        List<Labor> labors = new ArrayList<>();
        Labor labor = new Labor();
        labor.setCategory("油漆工");
        labor.setPeople(2);
        labor.setPrice(400);
        labor.setTotalPrice(1600);
        labors.add(labor);
        labors.add(labor);
        labors.add(labor);
        labors.add(labor);
        data.setLabors(labors);

        // same line
        data.setGoods2(goods);
        data.setLabors2(labors);

        List<Map<String, Object>> good3Map = new ArrayList<>();
        Map<String, Object> goods3 = new HashMap<>();
        good3Map.add(goods3);
        goods3.put("count", new RecordValItem().setValue("zhangsan"));
        goods3.put("name", new RecordValItem().setValue("700"));

        Map<String, Object> goods4 = new HashMap<>();
        good3Map.add(goods4);
        goods4.put("count", new RecordValItem().setValue("zhangsan2"));
        goods4.put("name", new RecordValItem().setValue("7002"));
        goods4.put("aa", "adad");

        Map<String, Object> goods5 = new HashMap<>();
        good3Map.add(goods5);
        goods5.put("count", new RecordValItem().setValue("zhangsan2"));
        goods5.put("name", new RecordValItem().setValue("7002"));
        data.setGoods3(good3Map);

    }


    @Test
    public void testTable() throws IOException {
        LoopColumnTableRenderPolicy hackLoopTableRenderPolicy = new LoopColumnTableRenderPolicy();
        RenderPolicy hackLoopSameLineTableRenderPolicy = new LoopColumnTableRenderPolicy(true);
        Configure config = Configure.builder().bind("goods3", hackLoopSameLineTableRenderPolicy)
                .bind("labors", hackLoopTableRenderPolicy).bind("goods2", hackLoopSameLineTableRenderPolicy)
                .bind("labors2", hackLoopSameLineTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(data);
        template.writeToFile("out_render_loopcolumn.docx");
    }

    @Test
    public void testPoi() throws IOException {

        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj_project/hj-platform-parent/hj-platform-core/src/test/resources/template.docx").render(
                new HashMap<String, Object>() {{
                    put("title", "Hi, poi-tl Word模板引擎");
                    // 一个2行2列的表格
                    put("table0", Tables.of(new String[][]{
                            new String[]{"00", "01"},
                            new String[]{"10", "11"}
                    }).border(BorderStyle.DEFAULT).create());

                    // 第0行居中且背景为蓝色的表格
                    RowRenderData row0 = Rows.of("姓名", "学历").textColor("FFFFFF")
                            .bgColor("4472C4").center().create();
                    RowRenderData row1 = Rows.create("李四", "博士");
                    put("table1", Tables.create(row0, row1));


                    // 合并第1行所有单元格的表格
                    RowRenderData row2 = Rows.of("列0", "列1", "列2").center().bgColor("4472C4").create();
                    CellRenderData cellRenderData = row2.getCells().get(0);
                    ParagraphRenderData data = cellRenderData.getParagraphs().get(0);
                    String s = data.toString();
                    ParagraphRenderData aaa = Paragraphs.of(s + "aaa").create();
                    cellRenderData.getParagraphs().set(0, aaa);
                    System.out.println("rrrrr->" + cellRenderData);
                    RowRenderData row3 = Rows.create("没有数据", null, null);
                    MergeCellRule rule = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 2)).build();
                    put("table3", Tables.of(row2, row3).mergeRule(rule).create());


                    put("list", Numberings.create("Plug-in grammar",
                            "Supports word text, pictures, table...",
                            "Not just templates"));

                    put("announce", false);

//                    Map<String, Goods> dataMap = new HashMap<>();
//                    Goods good = new Goods();
//                    good.setCount(4);
//                    good.setName("墙纸");
//                    good.setDesc("书房卧室");
//                    good.setDiscount(1500);
//                    good.setPrice(400);
//                    dataMap.put("name", good);
                    Map<String, RecordValItem> dataMap = getDataMap();
                    put("person", dataMap);


                    List<Map<String, Object>> songs = new ArrayList<>();
                    Map<String, Object> song1 = new HashMap<>();
                    song1.put("name", "最炫民族风");
                    Map<String, Object> song2 = new HashMap<>();
                    song2.put("name", "小苹果");
                    Map<String, Object> song3 = new HashMap<>();
                    song3.put("name", "大风吹");
                    Map<String, Object> song4 = new HashMap<>();
                    song4.put("name", "吻别");

                    songs.add(song1);
                    songs.add(song2);
                    songs.add(song3);
                    songs.add(song4);

                    put("songs", songs);

                    List<String> produces = new ArrayList<>();
                    produces.add("application/json");
                    produces.add("application/xml");

                    put("produces", produces);

                }});
        template.write(new FileOutputStream("output.docx"));
        template.close();
    }


    private Map<String, RecordValItem> getDataMap() {
        String json = "[{\n" +
                "\t\"key\": \"weatherCondition\",\n" +
                "\t\"name\": \"天气状况\",\n" +
                "\t\"required\": false,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"晴天\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"sampleFluid\",\n" +
                "\t\"name\": \"采样流体(L/min)\",\n" +
                "\t\"required\": false,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"sampleVolume\",\n" +
                "\t\"name\": \"采样体积\",\n" +
                "\t\"required\": false,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"batterFluid\",\n" +
                "\t\"name\": \"参比体积\",\n" +
                "\t\"required\": false,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumFlow\",\n" +
                "\t\"name\": \"中流量校准仪器\",\n" +
                "\t\"required\": false,\n" +
                "\t\"label\": \"校准设备11号\",\n" +
                "\t\"samNavigator\": true,\n" +
                "\t\"arrow\": true,\n" +
                "\t\"value\": [\"JZ-001\"],\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumCalibrationDate\",\n" +
                "\t\"name\": \"校准日期\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": true,\n" +
                "\t\"value\": \"2021-07-22\",\n" +
                "\t\"picker\": \"date\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumCalibratioVal\",\n" +
                "\t\"name\": \"校准值（L/min）\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumBeforeMonitoring\",\n" +
                "\t\"name\": \"监测前（L/min)\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumAfterMonitoring\",\n" +
                "\t\"name\": \"监测后（L/min)\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"mediumCalibrator\",\n" +
                "\t\"name\": \"校准人\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"无名氏\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"smallFlow\",\n" +
                "\t\"name\": \"小流量校准仪器\",\n" +
                "\t\"required\": false,\n" +
                "\t\"label\": \"校准设备11号\",\n" +
                "\t\"samNavigator\": true,\n" +
                "\t\"arrow\": true,\n" +
                "\t\"value\": [\"JZ-001\"],\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"calibrationDate\",\n" +
                "\t\"name\": \"校准日期\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": true,\n" +
                "\t\"value\": \"2020-07-22\",\n" +
                "\t\"picker\": \"date\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"calibratioVal\",\n" +
                "\t\"name\": \"校准值（L/min）\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"beforeMonitoring\",\n" +
                "\t\"name\": \"监测前（L/min)\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"afterMonitoring\",\n" +
                "\t\"name\": \"监测后（L/min)\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"0.5\",\n" +
                "\t\"showType\": 1\n" +
                "}, {\n" +
                "\t\"key\": \"smallCalibrator\",\n" +
                "\t\"name\": \"校准人\",\n" +
                "\t\"required\": true,\n" +
                "\t\"arrow\": false,\n" +
                "\t\"value\": \"有名氏\",\n" +
                "\t\"showType\": 1\n" +
                "}]";

//        List<RecordItemVo> list = JSON.parseArray(json, RecordItemVo.class);

        return null;
    }
}