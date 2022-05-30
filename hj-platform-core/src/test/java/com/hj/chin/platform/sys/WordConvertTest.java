package com.hj.chin.platform.sys;

import org.hj.chain.platform.Constants;
import org.hj.chain.platform.FileConvertUtil;
import org.hj.chain.platform.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/22  8:14 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/22    create
 */
public class WordConvertTest {

    private final String WORD_FILE_PATH2 = Constants.DEFAULT_FOLDER_TMP + "/abc.docx";
    @Test
    public void testWord2Pdf() throws Exception {
        File pdfFile = FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/rpt.docx"),
                Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-word.pdf");
        System.out.println(pdfFile);
    }

    @Test
    public void testSpireWord2Pdf() throws Exception {
//        Document doc = new Document();
//        doc.loadFromFile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/rpt.docx",FileFormat.Docx);
//        doc.saveToFile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/rpt.pdf", FileFormat.PDF);
        File pdfFile = FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/test/resources/rpt.docx"),
                Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-word.pdf");
        System.out.println(pdfFile);
    }


}