package org.hj.chain.platform.word2pdf;

import com.aspose.words.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * <p>
 * word 转 pdf 工具类
 * </p>
 */
@Slf4j
public class Word2PdfUtil {

    /**
     * `word` 转 `pdf`
     *
     * @param wordBytes: word字节码
     * @return: 生成的`pdf`字节码
     */
    @SneakyThrows(Exception.class)
    public static byte[] wordBytes2PdfBytes(byte[] wordBytes) {
        Document document = new Document(new ByteArrayInputStream(wordBytes));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream, SaveFormat.PDF);
        // 返回生成的`pdf`字节码
        return outputStream.toByteArray();
    }

    /**
     * `word` 转 `pdf`
     *
     * @param wordBytes:   word字节码
     * @param pdfFilePath: 需转换的`pdf`文件路径
     * @return: 生成的`pdf`文件数据
     */
    @SneakyThrows(Exception.class)
    public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath, String imagePath) {

        Document doc = new Document(new ByteArrayInputStream(wordBytes));
        Document document = new Document();//新建一个空白pdf文档
//        DocumentBuilder builder = new DocumentBuilder(document);
//        Font font = builder.getFont();
//        font.setName("SimSun");
        document.removeAllChildren();
        document.appendDocument(doc, ImportFormatMode.USE_DESTINATION_STYLES);//保留样式
//        document.appendDocument(doc, ImportFormatMode.USE_DESTINATION_STYLES);//保留样式
        FontSettings fontSettings = FontSettings.getDefaultInstance();
        doc.setFontSettings(fontSettings);
        fontSettings.setEnableFontSubstitution(true);
        FontSourceBase[] originalFontSources = fontSettings.getFontsSources();
////        // Create a font source from a folder that contains fonts.
        FolderFontSource folderFontSource = new FolderFontSource("/usr/share/fonts/win", true);
//// Apply a new array of font sources that contains the original font sources, as well as our custom fonts.
        FontSourceBase[] updatedFontSources = {originalFontSources[0], folderFontSource};
        fontSettings.setFontsSources(updatedFontSources);
////
//// Verify that Aspose.Words has access to all required fonts before we render the document to PDF.
        if (imagePath != null) {
            try {
                WaterMark.insertWatermarkImage(document, imagePath);
            } catch (Exception e) {
            }
        }
        document.save(pdfFilePath, SaveFormat.PDF);
// Restore the original font sources.
        fontSettings.setFontsSources(originalFontSources);
        return new File(pdfFilePath);
    }

}
