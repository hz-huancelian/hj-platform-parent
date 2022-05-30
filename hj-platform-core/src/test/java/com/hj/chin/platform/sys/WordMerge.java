package com.hj.chin.platform.sys;

import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/25  9:54 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class WordMerge {


    @Test
    public void mergeWord() throws Exception {
        NiceXWPFDocument main = new NiceXWPFDocument(new FileInputStream("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/main.docx"));

        NiceXWPFDocument sub = new NiceXWPFDocument(new FileInputStream("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/sub.docx"));
        NiceXWPFDocument sub2 = new NiceXWPFDocument(new FileInputStream("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/surfacewater.docx"));

// 合并两个文档
        NiceXWPFDocument newDoc = main.merge(sub);
        NiceXWPFDocument doc = newDoc.merge(sub2);

// 生成新文档
        FileOutputStream out = new FileOutputStream("new_doc.docx");
        doc.write(out);
        newDoc.close();
        doc.close();
        out.close();
    }
}