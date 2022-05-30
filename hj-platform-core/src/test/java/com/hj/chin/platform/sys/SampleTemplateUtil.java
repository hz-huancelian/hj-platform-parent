package com.hj.chin.platform.sys;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.word.SampleData;
import org.hj.chain.platform.word.SampleTableData;
import org.hj.chain.platform.word.SampleTablePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:32 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTemplateUtil {


    @Test
    public void testSample() throws IOException {

        List<SampleTableData> datamap = new ArrayList<>();
        RowRenderData factor = Rows.of("1", "水", "1.1", "A", "流量", "1", "1", null, "39", null, "12", null, "14", null, "16", null, "18", null, "20", null, "22", "11").center().create();
        RowRenderData factor2 = Rows.of("1", "水", "1.1", "A", "流量", "1", "1", null, "40", null, "13", null, "15", null, "17", null, "19", null, "21", null, "23", "11").center().create();
        RowRenderData factor3 = Rows.of("2", "水1", "1.1", "A", "流量", "1", "1", null, "39", null, "10", null, "8", null, "11", null, "16", null, "19", null, "21", "21").center().create();
        RowRenderData factor4 = Rows.of("2", "水1", "1.1", "A", "流量", "1", "1", null, "39", null, "8", null, "9", null, "13", null, "18", null, "12", null, "12", "22").center().create();

        SampleTableData data = new SampleTableData();
        data.setSampleDate("2021-05-23")
                .setSampleBasis("七大姑八大姨的测试")
                .setProjectName("测试项目");
        SampleData sampleData = new SampleData();
        List<RowRenderData> rowRenderData = Arrays.asList(factor, factor2, factor3, factor4);
        sampleData.setFactors(rowRenderData);
        data.setSampleData(sampleData);

        datamap.add(data);

        RowRenderData factor5 = Rows.of("3", "水", "1.1", "A", "流量", "1", "1", null, "39", null, "12", null, "14", null, "16", null, "18", null, "20", null, "22", "11").center().create();
        RowRenderData factor6 = Rows.of("3", "水", "1.1", "A", "流量", "1", "1", null, "40", null, "13", null, "15", null, "17", null, "19", null, "21", null, "23", "11").center().create();
        RowRenderData factor7 = Rows.of("4", "水1", "1.1", "A", "流量", "1", "1", null, "39", null, "10", null, "8", null, "11", null, "16", null, "19", null, "21", "21").center().create();
        RowRenderData factor8 = Rows.of("4", "水1", "1.1", "A", "流量", "1", "1", null, "39", null, "8", null, "9", null, "13", null, "18", null, "12", null, "12", "22").center().create();

        SampleTableData data2 = new SampleTableData();
        data2.setSampleDate("2021-05-23")
                .setSampleBasis("七大姑八大姨的测试")
                .setProjectName("测试项目2");
        SampleData sampleData2 = new SampleData();
        List<RowRenderData> rowRenderData2 = Arrays.asList(factor5, factor6, factor7, factor8);
        sampleData2.setFactors(rowRenderData2);
        data2.setSampleData(sampleData2);

        datamap.add(data2);
        Configure config = Configure.builder().bind("sample_table", new SampleTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/sample_template_loop.docx", config).render(new HashMap<String, Object>() {{
            put("list", datamap);
        }});
        template.writeToFile("out_sample.docx");

    }
}