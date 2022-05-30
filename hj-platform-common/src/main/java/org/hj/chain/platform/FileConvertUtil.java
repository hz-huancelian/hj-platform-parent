package org.hj.chain.platform;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.doc2docx.Doc2DocxUtil;
import org.hj.chain.platform.word2pdf.Word2PdfUtil;

import java.io.File;

/**
 * TODO 文件转换工具类$
 *
 * @param
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2021/5/22 8:06 下午
 */
@Slf4j
public class FileConvertUtil {

    /**
     * `word` 转 `pdf`
     *
     * @param wordBytes:   word字节码
     * @param pdfFilePath: 需转换的`pdf`文件路径
     * @return: 生成的`pdf`文件数据
     */
    public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath) {
        MatchLicense.init();
        return Word2PdfUtil.wordBytes2PdfFile(wordBytes, pdfFilePath, null);
    }

    /**
     * `word` 转 `pdf`
     *
     * @param wordBytes:   word字节码
     * @param pdfFilePath: 需转换的`pdf`文件路径
     * @return: 生成的`pdf`文件数据
     */
    public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath, String imagePath) {
        MatchLicense.init();
        return Word2PdfUtil.wordBytes2PdfFile(wordBytes, pdfFilePath, imagePath);
    }

    /**
     * `doc` 转 `docx`
     *
     * @param docBytes: doc文件字节码
     * @return: 生成的`docx`文件字节码
     */
    public static byte[] docBytes2DocxBytes(byte[] docBytes) {
        MatchLicense.init();
        return Doc2DocxUtil.docBytes2DocxBytes(docBytes);
    }

    /**
     * `doc` 转 `docx`
     *
     * @param docBytes:     doc文件字节码
     * @param docxFilePath: 待生成的的`docx`文件路径
     * @return: 生成的`docx`文件数据
     */
    public static File docBytes2DocxFile(byte[] docBytes, String docxFilePath) {
        MatchLicense.init();
        return Doc2DocxUtil.docBytes2DocxFile(docBytes, docxFilePath);
    }


}
