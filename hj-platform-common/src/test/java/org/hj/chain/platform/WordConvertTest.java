package org.hj.chain.platform;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 墨
 * @Iteration : 1.0
 * @Date : 2021/5/22  8:14 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/22    create
 */
public class WordConvertTest {

//    private final String WORD_FILE_PATH2 = Constants.DEFAULT_FOLDER_TMP + "/abc.docx";
    private final String WORD_FILE_PATH2 = "/Users/lijinku/Documents/nj-hc-core/3.0/hj-platform-parent/hj-platform-common/tmp-generate/HNJSB20220406-00009.docx";

    @Test
    public void testWord2Pdf() throws Exception {
        File pdfFile = FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(WORD_FILE_PATH2),
                Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-word.pdf");
        System.out.println(pdfFile);
    }


    @Test
    public void testWaterMark() throws Exception {
        MatchLicense.init();
        Document doc = new Document(WORD_FILE_PATH2);
        WaterMark.insertWatermarkImage(doc, "/Users/lijinku/Documents/projects/hj_project/hj-platform-parent/hj-platform-common/tmp/test.jpg");
        doc.save("asb.pdf", SaveFormat.PDF);

    }
}