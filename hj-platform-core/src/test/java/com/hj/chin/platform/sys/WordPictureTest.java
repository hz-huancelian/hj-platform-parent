package com.hj.chin.platform.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2022/5/18  10:59 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/18    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class WordPictureTest {


    public static void main(String[] args) throws Exception {


        FileInputStream is = new FileInputStream("output.docx");
        XWPFDocument doc = new XWPFDocument(is);
        List<XWPFTable> tables = doc.getTables();
        if(tables.size()>0){
            XWPFTable table = tables.get(0);
            List<XWPFTableRow> rows = table.getRows();
            XWPFTableRow row = table.getRow(rows.size() - 2);
            XWPFTableCell cell = row.getCell(0);
            List<XWPFParagraph> paragraphs = cell.getParagraphs();
//            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            InputStream stream = new FileInputStream("blank_pic.png");
            int length = paragraphs.size();
            System.out.println(length);
            XWPFRun run = paragraphs.get(length - 1).createRun();
            run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(180), Units.toEMU(50));
            CTDrawing drawing = run.getCTR().getDrawingArray(0);
            CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();

            //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
            CTAnchor anchor = getAnchorWithGraphic(graphicalobject,
                    Units.toEMU(180),
                    Units.toEMU(50),
                    Units.toEMU(100),
                    Units.toEMU(-10));
            //添加浮动属性
            drawing.setAnchorArray(new CTAnchor[]{anchor});
            //删除行内属性
            drawing.removeInline(0);

        }
//        for (XWPFTable table : tables) {
//            List<XWPFTableRow> rows = table.getRows();
//            XWPFTableRow xwpfTableRow = rows.get(rows.size() - 1);
//            XWPFTableCell cell = xwpfTableRow.getCell(0);
//            List<XWPFParagraph> paragraphs = cell.getParagraphs();
////            List<XWPFParagraph> paragraphs = doc.getParagraphs();
//            InputStream stream = new FileInputStream("CNAS.png");
//            int length = paragraphs.size();
//            System.out.println(length);
//            XWPFRun run = paragraphs.get(length - 1).createRun();
//            run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(100), Units.toEMU(100));
//            CTDrawing drawing = run.getCTR().getDrawingArray(0);
//            CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
//
//            //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
//            CTAnchor anchor = getAnchorWithGraphic(graphicalobject,
//                    Units.toEMU(100),
//                    Units.toEMU(100),
//                    Units.toEMU(100),
//                    Units.toEMU(0));
//            //添加浮动属性
//            drawing.setAnchorArray(new CTAnchor[]{anchor});
//            //删除行内属性
//            drawing.removeInline(0);
//        }
        // 段落
//        List<XWPFParagraph> paragraphs = doc.getParagraphs();
//        InputStream stream = new FileInputStream("CNAS.png");
//        int length = paragraphs.size();
//        System.out.println(length);
//        XWPFRun run = paragraphs.get(length - 1).createRun();
//        run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(100), Units.toEMU(100));
//        CTDrawing drawing = run.getCTR().getDrawingArray(0);
//        CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
//
//        //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
//        CTAnchor anchor = getAnchorWithGraphic(graphicalobject,
//                Units.toEMU(100),
//                Units.toEMU(100),
//                Units.toEMU(100),
//                Units.toEMU(0));
//        //添加浮动属性
//        drawing.setAnchorArray(new CTAnchor[]{anchor});
//        //删除行内属性
//        drawing.removeInline(0);
        FileOutputStream out = new FileOutputStream("output2.docx");
        doc.write(out);
        out.close();
        doc.close();
        System.out.println("合并word成功！");

    }


    /**
     * @param ctGraphicalObject 图片数据
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     * @return
     * @throws Exception
     */
    private static CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject, int width, int height,
                                                 int leftOffset, int topOffset) {
        String anchorXML =
                "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                        + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"0\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                        + "<wp:simplePos x=\"0\" y=\"0\"/>"
                        + "<wp:positionH relativeFrom=\"column\">"
                        + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                        + "</wp:positionH>"
                        + "<wp:positionV relativeFrom=\"paragraph\">"
                        + "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                        "</wp:positionV>"
                        + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                        + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                        + "<wp:wrapNone/>"
                        + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"G:/11.png\"/><wp:cNvGraphicFramePr/>"
                        + "</wp:anchor>";

        CTDrawing drawing = null;
        try {
            drawing = CTDrawing.Factory.parse(anchorXML);
        } catch (XmlException e) {
            e.printStackTrace();
        }
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;
    }
}