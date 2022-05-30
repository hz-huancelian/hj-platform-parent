package com.hj.chin.platform.sys;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.word.WasteWaterData;
import org.hj.chain.platform.word.WasteWaterItemData;
import org.hj.chain.platform.word.WasteWaterPolicy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2022/5/19  3:54 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/19    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class WasteWaterTable {

    public static void main(String[] args) throws Exception {

        WasteWaterData wwData = new WasteWaterData();
        WasteWaterItemData itemData = new WasteWaterItemData();
        wwData.setItemData(itemData);
        itemData.setTemplatePath("blank_pic.png");
        itemData.setFreqs(Arrays.asList("1"));
        List<List<RowRenderData>> rows = new ArrayList<>();
        RowRenderData data = Rows.of("w001", "2022-05-19", "佛系", "锰", "0.2100").center().create();
        System.out.println(data);
        List<RowRenderData> list = new ArrayList<>();
        list.add(data);
        rows.add(list);
        itemData.setRows(rows);
        //合并文件
        NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream("output.docx"));
        Configure config = Configure.builder().bind("exh_table", new WasteWaterPolicy(1)).build();
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile("waste_water_template-1.docx", config).render(wwData);
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

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("output.docx");
            temDoc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (temDoc != null) {
                temDoc.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}