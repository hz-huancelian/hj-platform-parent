package org.hj.chain.platform.word;

import cn.hutool.json.JSONUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.DateUtils;
import org.hj.chain.platform.FileConvertUtil;
import org.hj.chain.platform.FileUtil;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.report.biz.IReportStrategy;
import org.hj.chain.platform.report.component.BeanTools;
import org.hj.chain.platform.report.component.HandlerContext;
import org.hj.chain.platform.report.component.ReportGenUtils;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;
import org.hj.chain.platform.vo.report.FactorParam;
import org.hj.chain.platform.vo.report.ReportGenDataVo;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/17  10:27 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/17    create
 */
@Slf4j
public class WordUtils {


    /**
     * TODO 下载word文档
     *
     * @param response
     * @param templatePaths
     * @param fileName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/19 9:44 上午
     */
    public static void doDownLoadMerge(HttpServletResponse response,
                                       List<String> templatePaths,
                                       String fileName,
                                       List<Configure> configs,
                                       List<Object> models) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String nfileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + nfileName + ".docx");

            if (configs != null && !configs.isEmpty()) {
                NiceXWPFDocument niceXWPFDocument = null;
                for (int i = 0; i < configs.size(); i++) {
                    Configure config = configs.get(i);
                    Object model = models.get(i);
                    String templatePath = templatePaths.get(i);
                    NiceXWPFDocument niceXWPFDocumentItem = XWPFTemplate.compile(templatePath, config).render(model).getXWPFDocument();
                    if (niceXWPFDocument == null) {
                        niceXWPFDocument = niceXWPFDocumentItem;
                    } else {
                        niceXWPFDocument = niceXWPFDocument.merge(niceXWPFDocumentItem);
                    }

                }
                XWPFTemplate template = XWPFTemplate.compile(niceXWPFDocument);
                template.writeAndClose(response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.parseFromMap(map));
        }

    }

    /**
     * TODO 下载word文档
     *
     * @param response
     * @param templatePath
     * @param fileName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/19 9:44 上午
     */
    public static void doDownLoad(HttpServletResponse response,
                                  String templatePath,
                                  String fileName,
                                  Configure config,
                                  Object model) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String nfileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + nfileName + ".docx");
            XWPFTemplate template = XWPFTemplate.compile(templatePath, config).render(model);
            template.writeAndClose(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.parseFromMap(map));
        }

    }


    /**
     * TODO 下载word文档
     *
     * @param response
     * @param fileName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/19 9:44 上午
     */
    public static void doDownLoad(HttpServletResponse response,
                                  String fileName,
                                  NiceXWPFDocument template) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String nfileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + nfileName + ".docx");
            template.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.parseFromMap(map));
        }

    }

    public static void main(String[] args) throws Exception {
//        genSufaceWater();

//        doc2PDF("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/HSKY2021-05-20-00001 (1).docx", "out.pdf");
//        docx4jToPdf("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/HSKY2021-05-20-00001 (1).docx", "out11.pdf");

//        testOfferGen();

//        testListTable();

//        genContract();
        ReportTableData data = new ReportTableData();
        data.setAddress("江苏省南京市江宁区菲尼克斯路70号总部基地34号楼12层")
                .setCertCode("161012050340")
                .setCheckType("委托检测")
                .setProjectName("南京化工")
                .setConsignorName("光明顶")
                .setFax("090190")
                .setTel("0299910")
                .setReportDate("2021-05-18")
                .setOwnerOrganName("通标SGS（江苏）检测科技有限公司")
                .setPostCode("210000")
                .setWebAddress("www.baidu.com")
                .setReportCode("No.SGS-R-10000024")
                .setOrganLogo(Pictures.ofLocal("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/static/hzLogo.png").size(100, 70).create())
                .setCertImg(Pictures.ofLocal("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/static/CMA.png").size(100, 70).create());

        ReportMainData reportMainData = new ReportMainData();
        reportMainData.setCheckType("委托检测");
        reportMainData.setConsignorName("光明顶")
                .setCheckEmpNames("张三、李四")
                .setSecdClassNames("雨水：PH那哒哒哒")
                .setConsignorLinkerPhone("121782172192")
                .setCheckDetails("dbadadad")
                .setAddress("dadasdasd")
                .setCheckGoal("qweqeqwe")
                .setCheckPeriod("1212");
        data.setMainData(reportMainData);


        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/report_template.docx").render(data);
        template.writeToFile("report_out.docx");
        FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes("/Users/lijinku/Documents/projects/hj-platform-parent/abc.docx"), "report_out.pdf");


    }

    private static void genContract() throws IOException {
        ContractData data = new ContractData();
        data.setContCode("BJSKY210517-00003")
                .setContControlId("HT0001")
                .setSignDate("2021-05-18")
                .setSignLocation("江苏.南京")
                .setSmallTotalAmount("100")
                .setTotalAmount("壹佰元整")
                .setPayMethod("(1)")
                .setPayDesc1("怎么支付都可以,我们都有一个家名字叫中国，兄弟姐妹都很多")
                .setPayDesc2("/")
                .setPayDesc3("/");
        CusContBaseInfoVo partA = new CusContBaseInfoVo();//
        partA.setCompanyName("南京化工")
                .setBankName("中国银行")
                .setAddress("江苏南京")
                .setBankNo("9999998888")
                .setJurPerson("老管")
                .setAgentPerson("小馆")
                .setPostCode("90881")
                .setTaxNumber("898989121")
                .setTelFax("899901");
        data.setPartA(partA);

        OwnerContBaseInfoVo partB = new OwnerContBaseInfoVo();
        partB.setOrganName("通标SGS（江苏）检测科技有限公司")
                .setBankName("中国工商银行")
                .setAddress("上海")
                .setBankNo("7777777777")
                .setJurPerson("江风")
                .setAgentPerson("小鱼儿")
                .setPostCode("909011")
                .setTaxNumber("9099012132")
                .setTelFax("799989");
        data.setPartB(partB);

        OfferTableData tableData = getOfferTableData();
        data.setOfferTable(
                Includes.ofLocal("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/offer_template.docx").setRenderModel(tableData).create());

        Configure config = Configure.builder().bind("offer_table", new OfferTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/contract_template.docx", config).render(data);
        template.writeToFile("contract_out.docx");
    }


    public static void testListTable() throws IOException {
        List<Map<String, Object>> datamap = new ArrayList<>();

        List<HandoverItemData> subList = new ArrayList<>();

        HandoverItemData data = new HandoverItemData();
        data.setSampItemId("110")
                .setCollectLocation("测试0");
        subList.add(data);
        HandoverItemData data2 = new HandoverItemData();
        data2.setSampItemId("111")
                .setCollectLocation("测试1");
        subList.add(data2);
        HandoverItemData data3 = new HandoverItemData();
        data3.setSampItemId("112")
                .setCollectLocation("测试2");
        subList.add(data3);
        HandoverItemData data4 = new HandoverItemData();
        data4.setSampItemId("112")
                .setCollectLocation("测试2");
        subList.add(data4);
        HandoverItemData data5 = new HandoverItemData();
        data5.setSampItemId("112")
                .setCollectLocation("测试2");
        subList.add(data5);
        HandoverItemData data6 = new HandoverItemData();
        data6.setSampItemId("112")
                .setCollectLocation("测试2");
        subList.add(data6);
        HandoverItemData data7 = new HandoverItemData();
        data7.setSampItemId("112")
                .setCollectLocation("测试2");
        subList.add(data6);

        datamap.add(new HashMap<String, Object>() {{
            put("samplist", subList);
            put("companyName", "南京环测");
            put("pageNo", "1");
        }});


        List<HandoverItemData> subList2 = new ArrayList<>();
        HandoverItemData data8 = new HandoverItemData();
        data8.setSampItemId("113")
                .setCollectLocation("测试3");
        subList2.add(data8);
        HandoverItemData data9 = new HandoverItemData();
        data9.setSampItemId("114")
                .setCollectLocation("测试4");
        subList2.add(data9);
        HandoverItemData data10 = new HandoverItemData();
        data10.setSampItemId("115")
                .setCollectLocation("测试5");
        subList2.add(data10);

        datamap.add(new HashMap<String, Object>() {{
            put("samplist", subList2);
            put("companyName", "南京环测2");
            put("pageNo", "2");
        }});

        List<HandoverItemData> subList3 = new ArrayList<>();
        HandoverItemData data11 = new HandoverItemData();
        data11.setSampItemId("113")
                .setCollectLocation("测试3");
        subList3.add(data11);
        HandoverItemData data12 = new HandoverItemData();
        data12.setSampItemId("114")
                .setCollectLocation("测试4");
        subList3.add(data12);
        HandoverItemData data13 = new HandoverItemData();
        data13.setSampItemId("115")
                .setCollectLocation("测试5");
        subList3.add(data13);

        datamap.add(new HashMap<String, Object>() {{
            put("samplist", subList3);
            put("companyName", "南京环测3");
            put("pageNo", "2");
        }});
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();

        Configure config = Configure.builder()
                .bind("samplist", policy).build();

        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/handover_template.docx", config).render(
                new HashMap<String, Object>() {{
                    put("list", datamap);
                }}
        );

        template.writeToFile("handover_out.docx");

    }


    private static void testOfferGen() throws IOException {
        OfferTableData data = getOfferTableData();

        genOfferTableWord("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/offer_template.docx",
                data, "offer_out.docx");
    }

    private static OfferTableData getOfferTableData() {
        OfferTableData data = new OfferTableData();
        data.setCapitalAmount("壹佰元整");
        data.setCheckAmount("20")
                .setConsignorLinker("张三")
                .setConsignorLinkerPhone("13752168790")
                .setConsignorName("马超")
                .setCreateUserName("曹丕")
                .setDraftAmount("98")
                .setExpediteAmount("20")
                .setLaborAmount("70")
                .setOfferId("BJSKY210514-00001")
                .setOwnerOrganName("联通一侧")
                .setReportAmount("10")
                .setSysAmount("100")
                .setTaxAmount("0")
                .setTripAmount("0");
        FactorData factorData = new FactorData();
        LinkedHashMap<String, List<RowRenderData>> factorsMap = new LinkedHashMap<>();
        RowRenderData factor = Rows.of("1", "水", "1.1", "A", "流量", "1", "1", "1", "39", "39").center().create();
        RowRenderData factor2 = Rows.of("1", "水", "1.2", "A", "PH值", "1", "1", "1", "40", "40").center().create();
        RowRenderData factor3 = Rows.of("1", "水", "小计", "79").center().create();

        List<RowRenderData> factorList = Arrays.asList(factor, factor2, factor3);

        RowRenderData factor4 = Rows.of("2", "雨水", "2.1", "A", "流量", "1", "1", "1", "39", "39").center().create();
        RowRenderData factor5 = Rows.of("2", "雨水", "2.2", "A", "PH值", "1", "1", "1", "60", "60").center().create();
        RowRenderData factor6 = Rows.of("2", "雨水", "小计", "99").center().create();

        List<RowRenderData> factorList2 = Arrays.asList(factor4, factor5, factor6);
        factorsMap.put("水", factorList);
        factorsMap.put("雨水", factorList2);

        factorData.setTotalFactorAmount("178")
                .setFactorsMap(factorsMap);
        data.setFactorData(factorData);
        return data;
    }


    public static void genSufaceWater() throws IOException {
        ReportAttachData attachData = new ReportAttachData();
        ReportItemData itemData = new ReportItemData();
        attachData.setItemData(itemData);
        itemData.setHeadRow(0);
        itemData.setHeadStartColumn(3);
        itemData.setTailRow(8);
        itemData.setTotalRow(6);
        itemData.setStartRow(1);
        RowRenderData head = Rows.of(null, "null", "null", "A", "B", "C", "D", "E", "F", "D", "E", "F", "", "", "", "", "", "11").center().create();
        itemData.setHeadData(head);
        RowRenderData tail = Rows.of(null, "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        itemData.setTailStartColumn(1);
        itemData.setTailData(tail);
        RowRenderData data = Rows.of("a1", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "22").center().create();
        RowRenderData data2 = Rows.of("a2", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        RowRenderData data3 = Rows.of("a3", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        RowRenderData data4 = Rows.of("a4", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        RowRenderData data5 = Rows.of("a5", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        RowRenderData data6 = Rows.of("a6", "b", "d", "A1", "B1", "C1", "D1", "E1", "F1", "D1", "E1", "F1", "", "", "", "", "", "").center().create();
        List<RowRenderData> dataList = Arrays.asList(data, data2, data3, data4, data5, data6);
        itemData.setRows(dataList);
        attachData.setItemData(itemData);
        Configure config = Configure.builder().bind("report_table", new ReportTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile("/Users/lijinku/Documents/projects/hj-platform-parent/hj-platform-core/src/main/resources/templates/surfacewater_template.docx", config).render(attachData);
        template.writeToFile("sufferwater_out.docx");
    }


    /**
     * TODO
     *
     * @param resource 模板地址
     * @param datas    数据
     * @param outPath  输出文件
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/17 10:30 下午
     */
    public static void genContTableWord(String resource, ContractData datas, String outPath) throws IOException {
        Configure config = Configure.builder().bind("offer_table", new OfferTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(datas);
        template.writeToFile(outPath);
    }

    /**
     * TODO
     *
     * @param resource 模板地址
     * @param datas    数据
     * @param outPath  输出文件
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/17 10:30 下午
     */
    public static void genOfferTableWord(String resource, OfferTableData datas, String outPath) throws IOException {
        Configure config = Configure.builder().bind("offer_table", new OfferTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(datas);
        template.writeToFile(outPath);
    }


    /**
     * TODO
     *
     * @param reportTableData
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/25 11:50 下午
     */
    public static boolean genReport(String resource, ReportGenDataVo reportTableData, String templatePath, String reportGenPath) throws IOException {

        String reportCode = reportTableData.getTableData().getReportCode();
        String mainDocFile = reportGenPath + "/report_main.docx";
        XWPFTemplate template = XWPFTemplate.compile(resource).render(reportTableData.getTableData());
        template.writeToFile(mainDocFile);

        NiceXWPFDocument main = new NiceXWPFDocument(new FileInputStream(mainDocFile));
        //按照二级分类生成
        //样品ID对应因子检测值
//        Map<String, LinkedHashMap<String, String>> sampIdValMap = reportTableData.getSampIdValMap();
        //二级分类对应样品ID
        Map<String, List<String>> secdClassSampMap = reportTableData.getSecdClassSampMap();

        List<String> secdClassIds = Arrays.asList("001001", "001002");
        String sufferWarterId = "001002";

        AtomicBoolean flag = new AtomicBoolean(false);
        secdClassIds.stream().forEach(sedClassId -> {
            //样品列表
            List<String> list = secdClassSampMap.get(sedClassId);
            if (list != null && !list.isEmpty()) {
                flag.set(true);
                Map<String, LinkedHashMap<String, FactorParam>> secdClassFactorMap = reportTableData.getSecdClassLaborFactorMap();
                LinkedHashMap<String, FactorParam> headMap = secdClassFactorMap.get(sedClassId);
//                log.info("headMap:->" + headMap);

                List<FactorParam> headParams = new ArrayList<>();
                headMap.forEach((key, val) -> {
                    headParams.add(val);
                });


                List<ReportAttachData> fileDatas = new ArrayList<>();
                List<List<FactorParam>> headList = StringUtils.splistList(headParams, 15);
                headList.stream().forEach(item -> {
                    List<String> checkStandardIds = item.stream().map(subItem -> subItem.getCheckStandardId()).collect(Collectors.toList());
                    log.info("checkStandardIds:->" + checkStandardIds);
                    List<String> headNams = item.stream().map(subItem -> subItem.getFactorName()).collect(Collectors.toList());
                    RowRenderData headData = Rows.of("", "", "").center().create();
                    List<CellRenderData> cells = headData.getCells();
                    headNams.stream().forEach(subItem -> {
                        cells.add(Cells.of(subItem).create());
                    });
                    int size = cells.size();
                    if (size < 18) {
                        for (int i = 0; i < 18 - size; i++) {
                            cells.add(Cells.of("").create());
                        }
                    }

//                    log.info("headData->" + headData.getCells().size());

                    List<String> tailNames = item.stream().map(subItem -> subItem.getUnitVal()).collect(Collectors.toList());

                    RowRenderData tailData = Rows.of("").center().create();
                    List<CellRenderData> cells2 = tailData.getCells();
                    tailNames.stream().forEach(subItem -> {
                        cells2.add(Cells.of(subItem).create());
                    });
                    int size2 = cells2.size();
                    if (size2 < 16) {
                        for (int i = 0; i < 16 - size2; i++) {
                            cells2.add(Cells.of("").create());
                        }
                    }

//                    log.info("tailData->" + tailData.getCells().size());

                    //一次表头，至少lists的size个
                    List<List<String>> lists = StringUtils.splistList(list, 6);
                    lists.stream().forEach(sampItems -> {
                        ReportAttachData attachData = new ReportAttachData();
                        fileDatas.add(attachData);
                        ReportItemData itemData = new ReportItemData();
                        attachData.setItemData(itemData);
                        itemData.setHeadRow(0);
                        itemData.setHeadStartColumn(3);
                        itemData.setTailRow(8);
                        itemData.setTotalRow(6);
                        itemData.setStartRow(1);
                        itemData.setTailStartColumn(1)
                                .setHeadData(headData)
                                .setTailData(tailData);

                        List<RowRenderData> rows = new ArrayList<>();
                        itemData.setRows(rows);

                        for (int i = 0; i < sampItems.size(); i++) {
                            Map<String, ReportSampleVo> sampleVoMap = reportTableData.getSampleVoMap();
                            ReportSampleVo sampleVo = sampleVoMap.get(sampItems.get(i));
                            Rows.RowBuilder inst = Rows.of();
                            CellRenderData checkPointCell = Cells.of(sampleVo.getFactorPoint()).create();
                            inst.addCell(checkPointCell);
                            CellRenderData properties = Cells.of(sampleVo.getSampleProperties()).create();
                            inst.addCell(properties);
                            CellRenderData collectTime = Cells.of(DateUtils.getDefaultTimeDateYYYYmmddHHmm(sampleVo.getCollectTime())).create();
                            inst.addCell(collectTime);
//                            LinkedHashMap<String, String> rowFactor = sampIdValMap.get(sampItems.get(i));

                            //判断所有的key是否在表头里
                            for (int k = 0; k < checkStandardIds.size(); k++) {
//                                String val = rowFactor.get(checkStandardIds.get(k));
                                String val = null;
                                CellRenderData cellRenderData = Cells.of(val).create();
                                inst.addCell(cellRenderData);

                            }


                            RowRenderData data = inst.center().create();
                            List<CellRenderData> renderCellData = data.getCells();
                            int cellSize = renderCellData.size();
                            if (cellSize < 18) {
                                for (int j = 0; j < 18 - cellSize; j++) {
                                    renderCellData.add(Cells.of("").create());
                                }
                            }

                            rows.add(data);
                        }
                    });

                });


                //合并文件
                NiceXWPFDocument temDoc = main;
                Configure config = Configure.builder().bind("report_table", new ReportTablePolicy()).build();
                for (int k = 0; k < fileDatas.size(); k++) {
                    XWPFTemplate attachTemplate = XWPFTemplate.compile(templatePath + "/surfacewater_template.docx", config).render(fileDatas.get(k));
                    try {
                        NiceXWPFDocument xwpfDocument = attachTemplate.getXWPFDocument();
                        temDoc = temDoc.merge(xwpfDocument);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            temDoc.close();
                            attachTemplate.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } finally {
                        try {
                            attachTemplate.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(reportGenPath + "/" + reportCode + ".docx");
                    temDoc.write(out);
                    out.close();
                    temDoc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });

        return flag.get();
    }


    /**
     * TODO  生成报告
     *
     * @param reportTableData
     * @param templatePath
     * @param reportGenPath
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/8 9:46 下午
     */
    public static void genReportByCondition(ReportGenDataVo reportTableData, String templatePath, String reportGenPath) throws IOException {
        String reportCode = reportTableData.getTableData().getReportCode();
        //按照二级分类生成
        //二级分类对应样品ID
        Map<String, String> secdClassNameMap = reportTableData.getSecdClassNameMap();

        String resource = templatePath + "/report_template.docx";
        String source = reportGenPath + "/" + reportCode + ".docx";
        XWPFTemplate template = XWPFTemplate.compile(resource).render(reportTableData.getTableData());
        try {
            template.writeToFile(source);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (template != null) {
                template.close();
            }
        }

        //获取handlerContextBean
        HandlerContext context = BeanTools.getBean(HandlerContext.class);
        //构建报告
        Set<String> secdClassSet = secdClassNameMap.keySet();
        log.info("二级分类集合：" + secdClassSet.size());
        List<IReportStrategy> reportStrategyList = new ArrayList<>(secdClassSet.size());
        List<String> secdClassIds = new ArrayList<>(secdClassSet.size());
        secdClassSet.stream().sorted().forEach(item -> {
            secdClassIds.add(item);
            reportStrategyList.add(context.getInstance(item));
        });
        ReportGenUtils.buildReport(reportTableData, reportStrategyList, secdClassIds, source, reportGenPath, templatePath);

    }

}