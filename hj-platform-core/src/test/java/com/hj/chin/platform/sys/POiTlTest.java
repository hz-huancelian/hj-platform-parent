package com.hj.chin.platform.sys;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.deepoove.poi.data.style.Style;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class POiTlTest {

    public static void main(String[] args) throws IOException {

        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/template.docx").render(
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


                    CellRenderData cellRenderData = Cells.of("10").center().create();
                    ParagraphRenderData renderData = cellRenderData.getParagraphs().get(0);
                    TextRenderData data = new TextRenderData();
                    data.setText("6");
                    Style style = new Style();
                    style.setVertAlign("superscript");
                    data.setStyle(style);
                    renderData.addText(data);

                    // 合并第1行所有单元格的表格
                    RowRenderData row2 = Rows.of("列0", "列1", "列2").center().bgColor("4472C4").create();
                    row2.getCells().add(cellRenderData);
                    RowRenderData row3 = Rows.create("没有数据", null, null, null);
                    MergeCellRule rule = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 2)).build();
                    TableRenderData tableRenderData = Tables.of(row2, row3).mergeRule(rule).create();
                    put("table3", tableRenderData);


                    put("list", Numberings.create("Plug-in grammar",
                            "Supports word text, pictures, table...",
                            "Not just templates"));

                    put("announce", false);

                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("name", "Sayi");
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
}