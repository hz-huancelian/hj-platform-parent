package org.hj.chain.platform.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.hj.chain.platform.check.entity.CheckFactorSubset;
import org.hj.chain.platform.vo.check.CheckResParam;
import org.hj.chain.platform.vo.record.Pollutant;
import org.hj.chain.platform.vo.record.RecordItemValParam;
import org.hj.chain.platform.vo.record.RecordValItem;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/8/18  10:35 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/18    create
 */
public abstract class BusiUtils {

    /**
     * TODO 解析二级类别数据
     *
     * @param json
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 10:53 下午
     */
    public static Map<String, Object> parseSampleDataJson(String json) {

        if (StrUtil.isBlank(json)) {
            return new HashMap<>();
        }

        Map<String, Object> resMap = new HashMap<>();
        Map<String, JSONObject> params = (Map) JSON.parse(json);
        params.forEach((key, val) -> {
            RecordValItem value = val.toJavaObject(RecordValItem.class);
            resMap.put(key, value);
        });

        return resMap;
    }


    /**
     * TODO 解析值
     *
     * @param itemVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/29 11:16 上午
     */
    public static List<RecordItemValParam> parseItemVal(RecordValItem itemVo) {
        //0-数值；1-字符串
//        String numericType = sampleDataItemParam.getNumericType();
        String jsonVal = itemVo.getValue();
        List<RecordItemValParam> params = parseRecordItemJson(jsonVal);
        return params;
    }


    /**
     * TODO 获取单一值
     *
     * @param itemVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/29 11:17 上午
     */
    public static String getSingleVal(RecordValItem itemVo) {
        if (itemVo != null) {
            List<RecordItemValParam> params = parseItemVal(itemVo);
            if (params != null) {
                return params.get(0).getVal();
            }
        }
        return null;
    }


    /**
     * TODO 解析具体值
     *
     * @param json
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 11:05 下午
     */
    public static List<RecordItemValParam> parseRecordItemJson(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }

        List<RecordItemValParam> recordItemValParams = JSON.parseArray(json, RecordItemValParam.class);
        return recordItemValParams;
    }


    /**
     * TODO 获取单一值
     *
     * @param json
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/29 6:45 下午
     */
    public static String getSingleValByJson(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        List<RecordItemValParam> recordItemValParams = JSON.parseArray(json, RecordItemValParam.class);
        if (recordItemValParams != null && !recordItemValParams.isEmpty()) {
            return recordItemValParams.get(0).getVal();
        }

        return null;
    }


    /**
     * TODO 获取污染物采样记录数据
     *
     * @param pollutantInfo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/18 10:37 上午
     */
    public static List<Pollutant> getSamplePollutantData(String pollutantInfo) {

        List<Pollutant> datas = new ArrayList<>();
        if (StrUtil.isNotBlank(pollutantInfo)) {
            datas = JSON.parseArray(pollutantInfo, Pollutant.class);
        }

        return datas;
    }


    /**
     * TODO 子因子构建
     *
     * @param sampIdSubValMap        样品ID-》检测因子项主键ID-》子因子值集合
     * @param checkFactorSubsets
     * @param checkFactorSampleNoMap
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/30 10:38 上午
     */
    public static void buidSubCheckFactorSubSet(Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap, List<CheckFactorSubset> checkFactorSubsets, Map<Long, String> checkFactorSampleNoMap) {
        checkFactorSubsets.stream().forEach(item -> {
            Long checkFactorId = item.getCheckFactorId();
            String sampItemId = checkFactorSampleNoMap.get(checkFactorId);
            String checkSubRes = item.getCheckSubRes();
            if (StrUtil.isNotBlank(checkSubRes)) {
                Map<Long, LinkedHashMap<String, String>> factorSubFactorMap = sampIdSubValMap.get(sampItemId);
                if (factorSubFactorMap == null) {
                    factorSubFactorMap = new HashMap<>();
                    sampIdSubValMap.put(sampItemId, factorSubFactorMap);
                }
                LinkedHashMap<String, String> subFactorMap = factorSubFactorMap.get(checkFactorId);
                if (subFactorMap == null) {
                    subFactorMap = new LinkedHashMap<>();
                    factorSubFactorMap.put(checkFactorId, subFactorMap);
                }

                CheckResParam res = JSON.parseObject(checkSubRes, CheckResParam.class);
                String resVal = getFactorVal(res);
                subFactorMap.put(res.getName(), resVal);
            }
        });
    }

    /**
     * TODO 获取因子的值
     *
     * @param res
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/3 11:11 下午
     */
    public static String getFactorVal(CheckResParam res) {
        String resVal;
        if (res.getV2() != null) {
            resVal = res.getV1() + "×" + res.getV2();
        } else {
            if (res.getV1() != null && res.getV1().equals("0")) {
                resVal = "ND";
            } else {
                resVal = String.valueOf(res.getV1());
            }
        }
        return resVal;
    }


    /**
     * TODO 根据因子值拼接结果值
     *
     * @param val
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/15 4:57 下午
     */
    public static CellRenderData convertCellRenderDataByFactorVal(String val) {
        CellRenderData factorValCell;
        if (StrUtil.isNotBlank(val) && val.contains("×")) {
            factorValCell = XwpsUtils.buildCellRendData(val);
        } else {
            factorValCell = Cells.of(val).center().create();
        }
        return factorValCell;
    }


    /**
     * TODO 组装设备信息
     *
     * @param secdClassId   二级类别
     * @param laborEquipVos 实验室数据
     * @param sceneEquipVos 采样据
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/17 5:10 下午
     */
    public static Map<String, List<ReportEquipVo>> buildEquipMap(String secdClassId,
                                                                 List<ReportEquipVo> laborEquipVos,
                                                                 List<ReportEquipVo> sceneEquipVos) {

        List<ReportEquipVo> equipVoList = new ArrayList<>();
        if (laborEquipVos != null && !laborEquipVos.isEmpty()) {
            equipVoList.addAll(laborEquipVos);
        }
        if (sceneEquipVos != null && !sceneEquipVos.isEmpty()) {
            equipVoList.addAll(sceneEquipVos);
        }
        List<ReportEquipVo> distinctLists = equipVoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>((Comparator.comparing(item -> item.getFactorName() + ";" + item.getAnalysisMethod())))), ArrayList::new));

        if (distinctLists != null && !distinctLists.isEmpty()) {
            List<ReportEquipVo> resList = distinctLists.stream().sorted(Comparator.comparing(ReportEquipVo::getCheckStandardId)).collect(Collectors.toList());
            Map<String, List<ReportEquipVo>> map = new HashMap<>();
            map.put(secdClassId, resList);
            return map;
        }
        return null;

    }


    /**
     * TODO 插入以下空白
     *
     * @param table       表
     * @param imagePath   图片地址
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     * @param leftOffset  左偏移
     * @param topOffset   上偏移
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/18 12:08 下午
     */
    public static void insertBlankMark(XWPFTable table,
                                       String imagePath,
                                       int startRowNum,
                                       int imageWidth,
                                       int imageHeight,
                                       int leftOffset,
                                       int topOffset) throws Exception {
        XWPFTableRow row = table.getRow(startRowNum);
        XWPFTableCell cell = row.getCell(0);
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        InputStream stream = new FileInputStream(imagePath);
        int length = paragraphs.size();
        System.out.println(length);
        XWPFRun run = paragraphs.get(length - 1).createRun();
        run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(imageWidth), Units.toEMU(imageHeight));
        CTDrawing drawing = run.getCTR().getDrawingArray(0);
        CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();

        //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor anchor = getAnchorWithGraphic(graphicalobject,
                Units.toEMU(imageWidth),
                Units.toEMU(imageHeight),
                Units.toEMU(leftOffset),
                Units.toEMU(topOffset));
        //添加浮动属性
        drawing.setAnchorArray(new CTAnchor[]{anchor});
        //删除行内属性
        drawing.removeInline(0);
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


    /**
     * TODO 默认配置
     *
     * @param table
     * @param imagePath
     * @param startRowNum
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/18 12:19 下午
     */
    public static void insertBlankMarkDefaultConfig(XWPFTable table,
                                                    String imagePath,
                                                    int startRowNum) throws Exception {

        insertBlankMark(table, imagePath, startRowNum, 180, 50, 100, -10);

    }
}