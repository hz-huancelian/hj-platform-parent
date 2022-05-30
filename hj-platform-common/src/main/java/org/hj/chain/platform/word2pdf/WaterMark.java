package org.hj.chain.platform.word2pdf;

import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/31  9:47 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/31    create
 */
@Slf4j
public class WaterMark {

    /**
     * Inserts a watermark into a document.  you need saved the doc by yourself.
     *
     * @param doc           The input document file.
     * @param watermarkImagePath Text of the watermark.
     */
    public static void insertWatermarkImage(Document doc, String watermarkImagePath) throws Exception {
        // 居中
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {

            // Place the watermark in the page center.
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.DEFAULT);
            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            watermark.setWrapType(WrapType.NONE); // TOP_BOTTOM : 将所设置位置的内容往上下顶出去
            watermark.setVerticalAlignment(VerticalAlignment.CENTER);
            watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
            log.info("居中");
            return null;
        });

        // 居中
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {

            // Place the watermark in the page center.
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.DEFAULT);
            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            watermark.setWrapType(WrapType.NONE); // TOP_BOTTOM : 将所设置位置的内容往上下顶出去
            watermark.setVerticalAlignment(VerticalAlignment.CENTER);
            watermark.setHorizontalAlignment(HorizontalAlignment.LEFT);
            log.info("居中");
            return null;
        });
//
//        // 居中
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {

            // Place the watermark in the page center.
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.DEFAULT);
            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            watermark.setWrapType(WrapType.NONE); // TOP_BOTTOM : 将所设置位置的内容往上下顶出去
            watermark.setVerticalAlignment(VerticalAlignment.CENTER);
            watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            log.info("居中");
            return null;
        });
        // 顶部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
//            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.TOP_MARGIN);
            watermark.setWrapType(WrapType.NONE);
            //  我们需要自定义距离顶部的高度
            watermark.setVerticalAlignment(VerticalAlignment.DEFAULT);
            watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // 设置距离顶部的高度
//            watermark.setTop(1.0);
            log.info("顶部");

            return null;
        });
        // 顶部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
//            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.INSIDE_MARGIN);
            watermark.setWrapType(WrapType.NONE);
            //  我们需要自定义距离顶部的高度
             watermark.setVerticalAlignment(VerticalAlignment.DEFAULT);
            watermark.setHorizontalAlignment(HorizontalAlignment.LEFT);
            // 设置距离顶部的高度
//            watermark.setTop(20);
            log.info("顶部");

            return null;
        });
        // 顶部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
//            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.TOP_MARGIN);
            watermark.setWrapType(WrapType.NONE);
            //  我们需要自定义距离顶部的高度
             watermark.setVerticalAlignment(VerticalAlignment.DEFAULT);
            watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            // 设置距离顶部的高度
//            watermark.setTop(20);
            log.info("顶部");

            return null;
        });
        // 底部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.MARGIN);
            watermark.setWrapType(WrapType.NONE);
            // 我们需要自定义距离顶部的高度
            // watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
            watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // 设置距离顶部的高度
            watermark.setTop(460);
            log.info("底部");

            return null;
        });

        // 底部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.MARGIN);
            watermark.setWrapType(WrapType.NONE);
            // 我们需要自定义距离顶部的高度
            // watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
            watermark.setHorizontalAlignment(HorizontalAlignment.LEFT);
            // 设置距离顶部的高度
            watermark.setTop(460);
            log.info("底部");

            return null;
        });
//        // 底部
        insertWatermarkImage(doc, watermarkImagePath, watermark -> {
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.MARGIN);
//            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.MARGIN);
            watermark.setWrapType(WrapType.NONE);
            // 我们需要自定义距离顶部的高度
            // watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
            watermark.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            // 设置距离顶部的高度
            watermark.setTop(460);
            log.info("底部");

            return null;
        });
    }

    private static void insertWatermarkImage(Document doc, String watermarkImagePath,
                                             Function<Shape, Object> watermaskPositionConfigFunc) throws Exception {
        // Create a watermark shape. This will be a WordArt shape.
        // You are free to try other shape types as watermarks.
        Shape watermark = new Shape(doc, ShapeType.IMAGE);
        // Set up the text of the watermark.
        watermark.getImageData().setImage(watermarkImagePath);


        // Set up the text of the watermark.
        // 这里设置为宋体可以保证在转换为PDF时中文不是乱码.
//        watermark.getTextPath().setFontFamily("宋体");//Arial;
        try {
            // 水印大小
            watermark.setWidth(100);
            watermark.setHeight(50);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Text will be directed from the bottom-left to the top-right corner.
        // 左下到右上
        watermark.setRotation(-40);

        // Remove the following two lines if you need a solid black text.
        final String colorStr = "E0E0E0";
        watermark.getFill().setColor(new java.awt.Color(Integer.parseInt(colorStr, 16))); // Try Color.lightGray to get more Word-style watermark
        watermark.setStrokeColor(new java.awt.Color(Integer.parseInt(colorStr, 16))); // Try Color.lightGray to get more Word-style watermark

        // Place the watermark in the special location of page .
        watermaskPositionConfigFunc.apply(watermark);

        // Create a new paragraph and append the watermark to this paragraph.
        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);

        // Insert the watermark into all headers of each document section.
        for (Section sect : doc.getSections()) {
            // There could be up to three different headers in each section, since we want
            // the watermark to appear on all pages, insert into all headers.
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            // 以下注释掉不影响效果, 未作深入研究, 时间有限.
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
        // 参考下API : https://apireference.aspose.com/java/words/com.aspose.words/ShapeBase
        //watermark.setZOrder(-100);
    }

    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect,
                                                  int headerType) {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);

        if (header == null) {
            // There is no header of the specified type in the current section, create it.
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }

        // Insert a clone of the watermark into the header.
        try {
            header.appendChild(watermarkPara.deepClone(true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}