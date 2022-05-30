package org.hj.chain.platform.common.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hj.chain.platform.DateUtils;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.Constants;
import org.hj.chain.platform.common.DownloadExcelUtil;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.service.IContractInfoService;
import org.hj.chain.platform.contract.service.IOwnerContractBaseInfoService;
import org.hj.chain.platform.fileresource.service.IFileResourceService;
import org.hj.chain.platform.offer.service.IOfferInfoService;
import org.hj.chain.platform.sample.service.ISampleDrawApplyService;
import org.hj.chain.platform.sample.service.ISampleItemService;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;
import org.hj.chain.platform.vo.record.*;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
import org.hj.chain.platform.word.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/19  9:57 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/19    create
 */
@Slf4j
@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private IOfferInfoService offerInfoService;
    @Autowired
    private IContractInfoService contractInfoService;
    @Autowired
    private ISampleDrawApplyService sampleDrawApplyService;
    @Autowired
    private IOwnerContractBaseInfoService ownerContractBaseInfoService;
    @Autowired
    private IFileResourceService fileResourceService;
    @Autowired
    private ISampleItemService sampleItemService;
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String templatePath;
    @Value("${file.ht.generate}")
    private String downLoadPath;

    @RequestMapping(value = "/downLoadOfferExcel", method = RequestMethod.GET)
    public void downLoadOfferExcel(HttpServletResponse response,
                                   @RequestParam String offerId) throws IOException {

        if (StrUtil.isBlank(offerId)) {
            return;
        }

        Map<String, Object> dataMap = offerInfoService.findDownloadExcelOfferInfoById(offerId);
        log.info("dataMap->" + dataMap);
        TemplateExportParams params = new TemplateExportParams(
                templatePath + "/offer_excel_template.xls");
        Workbook workbook = ExcelExportUtil.exportExcel(params, dataMap);
        //纵向合并
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 1);
        //横向合并
        int lastRow = 9;
        String mergeRows = (String) dataMap.get("101");
        if (mergeRows != null) {
            String[] mergeRowArray = mergeRows.split("\\|");
            int mergeIndex = 8;
            for (String mergeRow : mergeRowArray) {
                mergeIndex += (Integer.valueOf(mergeRow) + 1);
//                //CellRangeAddress(第几行开始，第几行结束，第几列开始，第几列结束)
                CellRangeAddress craOne = new CellRangeAddress(mergeIndex - 1, mergeIndex - 1, 2, 11);
                workbook.getSheetAt(0).addMergedRegion(craOne);
            }
            int sum = Arrays.stream(mergeRowArray).mapToInt(item -> Integer.valueOf(item)).sum();
            lastRow = lastRow + sum + mergeRowArray.length;
        }

        List<Map<String, Object>> selfCosts = (List<Map<String, Object>>) dataMap.get("selfCosts");
        if (selfCosts == null || selfCosts.isEmpty()) {
            lastRow = lastRow + 6;
            int lastRowNum = workbook.getSheetAt(0).getLastRowNum();
            workbook.getSheetAt(0).shiftRows(lastRow, lastRowNum, -1);
        } else {
            int size = selfCosts.size();
            if (size > 0) {
                int mergerRowIndex = lastRow + 4;
                for (int i = 0; i < size; i++) {
                    mergerRowIndex += 1;
//                //CellRangeAddress(第几行开始，第几行结束，第几列开始，第几列结束)
                    CellRangeAddress craOne = new CellRangeAddress(mergerRowIndex, mergerRowIndex, 0, 11);
                    workbook.getSheetAt(0).addMergedRegion(craOne);
                }

            }
        }

        String fileName = downLoadPath + "/" + offerId + ".xls";
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();

        FileInputStream is = new FileInputStream(fileName);
//        ClassPathResource resource = new ClassPathResource("templates/excel/" + fileName);
//        InputStream is = resource.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        //下载
        DownloadExcelUtil dUtil = new DownloadExcelUtil();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        hssfWorkbook.write(os);
        dUtil.download(os, response, offerId + ".xls");
        os.flush();
        os.close();
        is.close();
    }


    @RequestMapping(value = "/downLoadOffer", method = RequestMethod.GET)
    public void downLoadOffer(HttpServletResponse response,
                              @RequestParam String offerId) throws IOException {

        if (StrUtil.isBlank(offerId)) {
            return;
        }
        String offerTemplate = templatePath + "/offer_template.docx";
        String fileName = offerId;
        OfferTableData offerTable = offerInfoService.findDownloadOfferInfoById(offerId);
        Configure config = Configure.builder().bind("offer_table", new OfferTablePolicy()).build();
        WordUtils.doDownLoad(response, offerTemplate, fileName, config, offerTable);

//        Map<String, Object> dataMap = offerInfoService.findDownloadExcelOfferInfoById(offerId);
//        TemplateExportParams params = new TemplateExportParams(
//                templatePath + "/offer_excel_template.xls");
//        Workbook workbook = ExcelExportUtil.exportExcel(params, dataMap);
////        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 1);
////        File savefile = new File("/Users/lijinku/Documents/excel/");
////        if (!savefile.exists()) {
////            savefile.mkdirs();
////        }
//
//        fileName = templatePath + "/" + offerId + ".xls";
//        FileOutputStream fos = new FileOutputStream(fileName);
//        workbook.write(fos);
//        fos.close();
    }


    @RequestMapping(value = "/downLoadContract", method = RequestMethod.GET)
    public void downLoadContract(HttpServletResponse response,
                                 @RequestParam String contCode) throws IOException {

        if (StrUtil.isBlank(contCode)) {
            return;
        }
        ContractData contractData = contractInfoService.findLoadContractByContCode(contCode);
        if (contractData.getOfferId() != null) {
            OfferTableData offerTable = offerInfoService.findDownloadOfferInfoById(contractData.getOfferId());
            contractData.setTotalAmount(offerTable.getCapitalAmount())
                    .setSmallTotalAmount(offerTable.getDraftAmount());
            String offerTemplate = templatePath + "/offer_template.docx";
            contractData.setOfferTable(
                    Includes.ofLocal(offerTemplate).setRenderModel(offerTable).create());
        }
        Configure config = Configure.builder().bind("offer_table", new OfferTablePolicy()).build();
        String contTemplate = templatePath + "/contract_template.docx";
        String fileName = contCode;
        WordUtils.doDownLoad(response, contTemplate, fileName, config, contractData);
    }

    /**
     * TODO 交接单
     *
     * @param response
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/8 8:48 下午
     */
    @RequestMapping(value = "/downLoadHandover", method = RequestMethod.GET)
    public void downLoadHandover(HttpServletResponse response,
                                 @RequestParam String secdClassId,
                                 @RequestParam String sampleNos) throws IOException {
        if (StrUtil.isBlank(secdClassId)) {
            return;
        }

        if (StrUtil.isBlank(sampleNos)) {
            return;
        }

        List<Map<String, Object>> datamap = new ArrayList<>();
        List<String> sampIds = Arrays.asList(sampleNos.split(","));
        HandoverTableData data = sampleDrawApplyService.findHandoverTableDataBySampIds(sampIds);

        List<HandoverItemData> dataList = data.getItemDataList();
        if (dataList != null && !dataList.isEmpty()) {
            List<List<HandoverItemData>> splistList = StringUtils.splistList(dataList, 7);
            int size = splistList.size();
            List<HandoverItemData> lastTable = null;
            for (int i = 0; i < size; i++) {
                List<HandoverItemData> itemDataList = splistList.get(i);
                if (itemDataList != null && !itemDataList.isEmpty()) {
                    List<HandoverItemData> subList = itemDataList.stream().map(item -> {
                        HandoverItemData itemData = new HandoverItemData();
                        String sampItemId = item.getSampItemId();
                        itemData.setCollectLocation(item.getCollectLocation())
                                .setFactorNames(item.getFactorNames())
                                .setSampleProperties(item.getSampleProperties())
                                .setSampItemId(sampItemId)
                                .setSampleFixative(item.getSampleFixative())
                                .setSampleCount(item.getSampleCount());
                        return itemData;
                    }).collect(Collectors.toList());
                    Map<String, Object> tableMap = new HashMap<>();
                    tableMap.put("samplist", subList);
                    tableMap.put("companyName", data.getConsignorName());
                    tableMap.put("collectTime", data.getCollectDate());
                    tableMap.put("storeTime", data.getStoreTime());
                    tableMap.put("recieveUsers", data.getRecieveUsers());
                    tableMap.put("sendUsers", data.getSendUsers());
                    tableMap.put("pageNo", (i + 1));
                    tableMap.put("totalNum", size);
                    datamap.add(tableMap);
                    if (i == size - 1) {
                        lastTable = subList;
                    }
                }
            }
            //填充空行
            if (lastTable != null) {
                int lastTableSize = lastTable.size();
                if (lastTableSize < 7) {
                    for (int i = 0; i < 7 - lastTableSize; i++) {
                        lastTable.add(new HandoverItemData());
                    }
                }

            }
        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        OwnerContBaseInfoVo ownerContBaseInfoVo = ownerContractBaseInfoService.findByOrganId(organId);
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("samplist", policy).build();

        String resource = templatePath + "/" + Constants.SECD_CLASS_HANDOVER_MAP.get(secdClassId);
        String controllerId = null;
        String fileCode = Constants.FileResource.SAMPLE_HANDOVER_YFQ.getKey();
        if (secdClassId.equals("005001")
                || secdClassId.equals("003001")
                || secdClassId.equals("003002")
                || secdClassId.equals("006001")) {
            //固废和土壤
            fileCode = Constants.FileResource.SAMPLE_HANDOVER_GFTR.getKey();
        } else if (secdClassId.equals("001001")
                || secdClassId.equals("001002")
                || secdClassId.equals("001003")
                || secdClassId.equals("001004")) {
            //地表水、地下水、雨水、废水
            fileCode = Constants.FileResource.SAMPLE_HANDOVER_S.getKey();
        } else if (secdClassId.equals("002001")
                || secdClassId.equals("002005")
                || secdClassId.equals("002003")
                || secdClassId.equals("002004")) {
            //样品交接单_无组织废气
            fileCode = Constants.FileResource.SAMPLE_HANDOVER_WFQ.getKey();
        }
        controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());

        String contControlId = controllerId;
        String fileName = data.getConsignorName() + "" + DateUtils.getDefaultFormatDateStr(LocalDateTime.now());
        WordUtils.doDownLoad(response, resource, fileName, config, new HashMap<String, Object>() {{
            put("list", datamap);
            if (ownerContBaseInfoVo != null) {
                put("organName", ownerContBaseInfoVo.getOrganName());
                put("contControlId", contControlId);
            }
        }});

    }


    @RequestMapping(value = "/downBlankSampleRecordByTaskId", method = RequestMethod.GET)
    public void downBlankSampleRecordByTaskId(HttpServletResponse response,
                                              @RequestParam Long taskId) throws IOException {
        if (taskId == null) {
            return;
        }
        Map<String, RecordRenderData> renderDataMap = sampleItemService.findSampleRenderDataByTaskId(taskId);
        if (!renderDataMap.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            String fileName = templatePath + "/" + "record_blank.docx";
            NiceXWPFDocument temDoc = new NiceXWPFDocument(new FileInputStream(fileName));
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            for (Map.Entry<String, RecordRenderData> entry : renderDataMap.entrySet()) {
                String secdClassId = entry.getKey();
                String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                RecordRenderData renderData = entry.getValue();
                List<RecordRenderItemData> itemDatas = renderData.getItemDatas();
                if (secdClassId.equals("002002") || secdClassId.equals("0020022")) {//有组织废气、锅炉窑
                    LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                    Configure config = Configure.builder()
                            .bind("datas", policy)
                            .bind("pollist", policy)
                            .build();
                    Map<String, Object> resDataMap = buildOranWastAir(renderData, itemDatas, secdClassId, loginOutputVo);
                    temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/organ_waste_gas_record.docx");

                } else if (secdClassId.equals("0020021")) {
                    //油烟
                    Map<String, Object> resDataMap = buidCookingOilData(renderData, itemDatas, loginOutputVo);
                    Configure config = Configure.builder().build();
                    //合并文件
                    temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/cooking_oil_flume_record.docx");
                } else if (secdClassId.equals("002003")//厂界无组织废气
                        || secdClassId.equals("002004")//车间无组织废气
                        || secdClassId.equals("002005")//室内空气
                        || secdClassId.equals("002001")) {//环境空气

                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                        Configure config = Configure.builder()
                                .bind("datas", policy)
                                .bind("equpList", policy)
                                .build();
                        Map<String, Object> resDataMap = buildAmbientAirData(secdClassId, renderData, loginOutputVo, getAirDataMap(renderData, itemDatas), Constants.FileResource.FILE_RECORD_KQ);
                        //合并文件
                        temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/ambient_air_record.docx");
                    }

                } else if (secdClassId.equals("001002")) {
                    //地表水
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        Configure config = Configure.builder()
                                .bind("sample_table", new SampleTablePolicy(3, 22, new int[]{0, 1, 2, 3, 4, 5, 7, 9, 11, 13, 15, 17, 19, 20, 21}))
                                .build();
                        SurfaceWaterTableData tableData = buildSurfaceWaterTableData(renderData, itemDatas, loginOutputVo);
                        //合并
                        temDoc = mergeNiceXWPFDocument(temDoc, config, tableData, "/surface_water_record.docx");
                    }
                } else if (secdClassId.equals("001003")) {
                    //地下水
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        Configure config = Configure.builder()
                                .bind("sample_table", new SampleTablePolicy(4, 19, new int[]{0, 1, 2, 4, 5, 6, 7, 8, 10, 12, 14, 16, 17, 18}))
                                .build();
                        GroundWaterTableData tableData = buildGroundWaterTableData(renderData, itemDatas, loginOutputVo);

                        //合并
                        temDoc = mergeNiceXWPFDocument(temDoc, config, tableData, "/ground_water_record.docx");
                    }
                } else if (secdClassId.equals("001001") || secdClassId.equals("001004")) {
                    //雨水、废水
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        Configure config = Configure.builder()
                                .bind("sample_table", new SampleTablePolicy(2, 9, new int[]{0, 1, 2, 3, 5, 6, 7, 8}))
                                .build();
                        WasteWaterTableData tableData = buildWasteWaterTableData(secdClassId, renderData, itemDatas, loginOutputVo);
                        //合并
                        temDoc = mergeNiceXWPFDocument(temDoc, config, tableData, "/waste_water_record.docx");
                    }
                } else if (secdClassId.equals("005001") || secdClassId.equals("006001")) {
                    //固废、污泥和生活垃圾
                    LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                    Configure config = Configure.builder()
                            .bind("datas", policy)
                            .build();
                    Map<String, Object> resDataMap = buildAmbientAirData(secdClassId, renderData, loginOutputVo, getSolidWastDataMap(renderData, itemDatas), Constants.FileResource.FILE_RECORD_GF);
                    //合并
                    temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/solid_waste_record.docx");
                } else if (secdClassId.equals("003001")) {
                    //土壤
//                    SelfLoopColumnTableRenderPolicy policy = new SelfLoopColumnTableRenderPolicy(1, 7, new int[]{15, 16});
                    LoopColumnTableRenderPolicy policy = new LoopColumnTableRenderPolicy();
                    Configure config = Configure.builder()
                            .bind("datas", policy)
                            .build();
                    Map<String, Object> resDataMap = buildSoilData(secdClassId, renderData, itemDatas, loginOutputVo);
                    //合并
                    temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/soil_record.docx");
                } else if (secdClassId.equals("003002")) {
                    //底泥
                    LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                    Configure config = Configure.builder()
                            .bind("datas", policy)
                            .build();
                    Map<String, Object> resDataMap = buildSedimentData(secdClassId, renderData, itemDatas, loginOutputVo);
                    //合并
                    temDoc = mergeNiceXWPFDocument(temDoc, config, resDataMap, "/sediment_record.docx");
                } else if (secdClassId.equals("004001")) {
                    //噪声：区域环境噪音
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        List<RowRenderData> rrds = new ArrayList<>();
                        NoiseTableData modeType0TabelData = buildRegionNoiseTableData(loginOutputVo, renderData, itemDatas, rrds);
                        modeType0TabelData.setSecdClassName(secdClassName);
                        //合并表格输出
                        if (!rrds.isEmpty()) {
                            Configure config = Configure.builder()
                                    .bind("sample_table", new NoiseType0TablePolicy())
                                    .build();
                            //合并
                            temDoc = mergeNiceXWPFDocument(temDoc, config, modeType0TabelData, "/noise_record.docx");
                        }
                    }
                } else if (secdClassId.equals("004002")) {
                    //噪声：工业企业厂界噪声
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        List<RowRenderData> rrds = new ArrayList<>();
                        NoiseTableData modeType3TabelData = buildIndustryNoiseTableData(loginOutputVo, renderData, itemDatas, rrds);
                        modeType3TabelData.setSecdClassName(secdClassName);

                        if (!rrds.isEmpty()) {
                            Configure config = Configure.builder()
                                    .bind("sample_table", new NoiseType0TablePolicy())
                                    .build();

                            //合并
                            temDoc = mergeNiceXWPFDocument(temDoc, config, modeType3TabelData, "/noise_record.docx");
                        }
                    }
                } else if (secdClassId.equals("004003")) {
                    //噪声：社会生活环境噪声
                    if (itemDatas != null && !itemDatas.isEmpty()) {

                        List<RowRenderData> rrds = new ArrayList<>();
                        NoiseTableData modeType4TabelData = buildSocietyNoiseTableData(loginOutputVo, renderData, itemDatas, rrds);
                        modeType4TabelData.setSecdClassName(secdClassName);

                        if (!rrds.isEmpty()) {
                            Configure config = Configure.builder()
                                    .bind("sample_table", new NoiseType0TablePolicy())
                                    .build();
                            temDoc = mergeNiceXWPFDocument(temDoc, config, modeType4TabelData, "/noise_record.docx");
                        }
                    }
                } else if (secdClassId.equals("004004")) {
                    //噪声：城市环境振动
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        List<RowRenderData> rrds = new ArrayList<>();
                        NoiseTableData modeType1TabelData = getEnvirVibraNoiseTableData(loginOutputVo, renderData, itemDatas, rrds);

                        if (!rrds.isEmpty()) {
                            Configure config = Configure.builder()
                                    .bind("sample_table", new NoiseType1TablePolicy())
                                    .build();
                            temDoc = mergeNiceXWPFDocument(temDoc, config, modeType1TabelData, "/urban_area_record.docx");
                        }

                    }
                } else if (secdClassId.equals("004005")) {
                    //噪声：道路交通噪声
                    if (itemDatas != null && !itemDatas.isEmpty()) {
                        List<Map<String, Object>> datas = new ArrayList<>();
                        Map<String, Object> modeType2DataMap = buildRoadNoiseData(loginOutputVo, renderData, itemDatas, datas);

                        if (!datas.isEmpty()) {
                            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                            Configure config = Configure.builder()
                                    .bind("datas", policy)
                                    .build();
                            int size = datas.size();
                            if (size < 6) {
                                List<Map<String, Object>> ndatas = new ArrayList<>();
                                ndatas.addAll(datas);
                                for (int i = 0; i < 6 - size; i++) {
                                    ndatas.add(new HashMap<>());
                                }
                                modeType2DataMap.put("datas", ndatas);
                            }

                            temDoc = mergeNiceXWPFDocument(temDoc, config, modeType2DataMap, "/road_noise_record.docx");
                        }

                    }
                }
            }

            WordUtils.doDownLoad(response, (taskId + "-" + DateUtils.getDefaultFormatDateStr(now)), temDoc);
        }

    }

    /**
     * TODO 构建道路交通噪声
     *
     * @param loginOutputVo
     * @param renderData
     * @param itemDatas
     * @param datas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 6:23 下午
     */
    private Map<String, Object> buildRoadNoiseData(LoginOutputVo loginOutputVo,
                                                   RecordRenderData renderData,
                                                   List<RecordRenderItemData> itemDatas,
                                                   List<Map<String, Object>> datas) {
        String fileCode = Constants.FileResource.FILE_RECORD_ZS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        //道路交通
        Map<String, Object> modeType2DataMap = new HashMap<>();
        modeType2DataMap.put("consignorName", renderData.getConsignorName());
        modeType2DataMap.put("organName", loginOutputVo.getOrganName());
        modeType2DataMap.put("checkEmps", renderData.getCheckEmps());
        modeType2DataMap.put("reviewerEmp", renderData.getReviewerEmp());
        modeType2DataMap.put("contContollerId", controllerId);
        modeType2DataMap.put("datas", datas);

        //同一个因子的不同频次
        Map<String, List<RecordRenderItemData>> commonSample = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getGroupKey));
        commonSample.entrySet().stream().sorted(Map.Entry.<String, List<RecordRenderItemData>>comparingByKey().reversed()).forEach(x -> {
            String groupKey = x.getKey();

            log.info("groupKey->" + groupKey);
            List<RecordRenderItemData> sampleDatas = x.getValue();
            for (int i = 0; i < sampleDatas.size(); i++) {
                RecordRenderItemData item = sampleDatas.get(i);
                Map<String, Object> sampleDataMap = item.getDataMap();
                Map<String, Object> dataMap = new HashMap<>();
                sampleDataMap.forEach((key, val) -> {
                    if (val instanceof RecordValItem) {
                        RecordValItem valItem = (RecordValItem) val;
                        String value = valItem.getValue();
                        dataMap.put(key, BusiUtils.getSingleValByJson(value));
                    } else {
                        dataMap.put(key, val);
                    }
                });
                //道路交通噪声
                //循环生成
                if (i == 0) {
//                    RecordValItem collectDate = (RecordValItem) sampleDataMap.get("collectDate");
                    modeType2DataMap.put("collectDate", item.getCollectDate());
                    RecordValItem weatherConditions = (RecordValItem) sampleDataMap.get("weatherConditions");
                    modeType2DataMap.put("dayWeather", BusiUtils.getSingleVal(weatherConditions));
                    RecordValItem beforeTest = (RecordValItem) sampleDataMap.get("beforeTest");
                    modeType2DataMap.put("beforeTest", BusiUtils.getSingleVal(beforeTest));
                    RecordValItem afterTest = (RecordValItem) sampleDataMap.get("afterTest");
                    modeType2DataMap.put("afterTest", BusiUtils.getSingleVal(afterTest));

                    RecordValItem flowEquipment = (RecordValItem) sampleDataMap.get("flowEquipment");
                    modeType2DataMap.put("flowEquipment", BusiUtils.getSingleVal(flowEquipment));

                    RecordValItem fieldTestingEquipment = (RecordValItem) sampleDataMap.get("fieldTestingEquipment");
                    modeType2DataMap.put("fieldTestingEquipment", BusiUtils.getSingleVal(fieldTestingEquipment));

                } else if (i == 1) {
                    RecordValItem weatherConditions = (RecordValItem) sampleDataMap.get("weatherConditions");
                    modeType2DataMap.put("nightWeather", BusiUtils.getSingleVal(weatherConditions));
                }
                dataMap.put("factorPoint", item.getFactorPoint());
                datas.add(dataMap);

            }
        });
        return modeType2DataMap;
    }

    /**
     * TODO 构建环境振动
     *
     * @param loginOutputVo
     * @param renderData
     * @param itemDatas
     * @param rrds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 6:20 下午
     */
    private NoiseTableData getEnvirVibraNoiseTableData(LoginOutputVo loginOutputVo, RecordRenderData renderData, List<RecordRenderItemData> itemDatas, List<RowRenderData> rrds) {
        String fileCode = Constants.FileResource.FILE_RECORD_ZS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());

        NoiseTableData modeType1TabelData = new NoiseTableData();
        modeType1TabelData.setOrganName(loginOutputVo.getOrganName())
                .setConsignorName(renderData.getConsignorName())
                .setCheckEmps(renderData.getCheckEmps())
                .setReviewerEmp(renderData.getReviewerEmp());
        modeType1TabelData.setContContollerId(controllerId);
        SampleData sampleData = new SampleData();
        sampleData.setFactors(rrds);
        modeType1TabelData.setSampleData(sampleData);

        //同一个因子的不同频次
        Map<String, List<RecordRenderItemData>> commonSample = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getGroupKey));
        commonSample.entrySet().stream().sorted(Map.Entry.<String, List<RecordRenderItemData>>comparingByKey().reversed()).forEach(x -> {
            String groupKey = x.getKey();
            log.info("环境振动：groupKey->" + groupKey);
            List<RecordRenderItemData> sampleDatas = x.getValue();
            for (int i = 0; i < sampleDatas.size(); i++) {
                RecordRenderItemData item = sampleDatas.get(i);
                Map<String, Object> dataMap = item.getDataMap();
                //城市环境振动
                //动态表格（两条对应一条表格记录）
                getModeType1TabelData(modeType1TabelData, rrds, i, item, dataMap);


            }
        });
        return modeType1TabelData;
    }

    /**
     * TODO 构建工业噪声
     *
     * @param loginOutputVo
     * @param renderData
     * @param itemDatas
     * @param rrds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 6:17 下午
     */
    private NoiseTableData buildIndustryNoiseTableData(LoginOutputVo loginOutputVo, RecordRenderData renderData, List<RecordRenderItemData> itemDatas, List<RowRenderData> rrds) {
        String fileCode = Constants.FileResource.FILE_RECORD_ZS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        NoiseTableData modeType3TabelData = new NoiseTableData();
        modeType3TabelData.setOrganName(loginOutputVo.getOrganName())
                .setConsignorName(renderData.getConsignorName())
                .setCheckEmps(renderData.getCheckEmps())
                .setReviewerEmp(renderData.getReviewerEmp());
        modeType3TabelData.setContContollerId(controllerId);
        SampleData sampleData = new SampleData();
        sampleData.setFactors(rrds);
        modeType3TabelData.setSampleData(sampleData);

        //同一个因子的不同频次
        Map<String, List<RecordRenderItemData>> commonSample = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getGroupKey));
        commonSample.entrySet().stream().sorted(Map.Entry.<String, List<RecordRenderItemData>>comparingByKey().reversed()).forEach(x -> {
            String groupKey = x.getKey();

            log.info("groupKey->" + groupKey);
            List<RecordRenderItemData> sampleDatas = x.getValue();
            for (int i = 0; i < sampleDatas.size(); i++) {
                RecordRenderItemData item = sampleDatas.get(i);
                Map<String, Object> dataMap = item.getDataMap();
                //工业企业厂界噪声
                //动态表格（两条对应一条表格记录）
                getModeType0Table(modeType3TabelData, rrds, i, item, dataMap);

            }
        });
        return modeType3TabelData;
    }

    /**
     * TODO 构建区域环境噪声
     *
     * @param loginOutputVo
     * @param renderData
     * @param itemDatas
     * @param rrds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 6:10 下午
     */
    private NoiseTableData buildRegionNoiseTableData(LoginOutputVo loginOutputVo, RecordRenderData renderData, List<RecordRenderItemData> itemDatas, List<RowRenderData> rrds) {
        NoiseTableData modeType0TabelData = new NoiseTableData();
        modeType0TabelData.setOrganName(loginOutputVo.getOrganName())
                .setConsignorName(renderData.getConsignorName())
                .setCheckEmps(renderData.getCheckEmps())
                .setReviewerEmp(renderData.getReviewerEmp());
        SampleData sampleData = new SampleData();
        sampleData.setFactors(rrds);
        modeType0TabelData.setSampleData(sampleData);
        String fileCode = Constants.FileResource.FILE_RECORD_ZS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        modeType0TabelData.setContContollerId(controllerId);
        //同一个因子的不同频次
        Map<String, List<RecordRenderItemData>> commonSample = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getGroupKey));
        commonSample.entrySet().stream().sorted(Map.Entry.<String, List<RecordRenderItemData>>comparingByKey().reversed()).forEach(x -> {
            String groupKey = x.getKey();
            log.info("groupKey->" + groupKey);
            List<RecordRenderItemData> sampleDatas = x.getValue();
            for (int i = 0; i < sampleDatas.size(); i++) {
                RecordRenderItemData item = sampleDatas.get(i);
                Map<String, Object> dataMap = item.getDataMap();
                //动态表格（两条对应一条表格记录）
                getModeType0Table(modeType0TabelData, rrds, i, item, dataMap);

            }
        });
        return modeType0TabelData;
    }

    /**
     * TODO 构建社会环境噪声
     *
     * @param loginOutputVo
     * @param renderData
     * @param itemDatas
     * @param rrds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 6:01 下午
     */
    private NoiseTableData buildSocietyNoiseTableData(LoginOutputVo loginOutputVo, RecordRenderData renderData, List<RecordRenderItemData> itemDatas, List<RowRenderData> rrds) {
        NoiseTableData modeType4TabelData = buildIndustryNoiseTableData(loginOutputVo, renderData, itemDatas, rrds);
        return modeType4TabelData;
    }

    /**
     * TODO 合并渲染
     *
     * @param temDoc
     * @param config
     * @param resDataMap
     * @param templateSourceFile
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 4:13 下午
     */
    private NiceXWPFDocument mergeNiceXWPFDocument(NiceXWPFDocument temDoc, Configure config, Object resDataMap, String templateSourceFile) {
        //合并文件
        XWPFTemplate attachTemplate = null;
        try {
            attachTemplate = XWPFTemplate.compile(templatePath + templateSourceFile, config).render(resDataMap);
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
        return temDoc;
    }


    @RequestMapping(value = "/downSampleRecordByTaskIdAndSecdClassId", method = RequestMethod.GET)
    public void downSampleRecordByTaskIdAndSecdClassId(HttpServletResponse response,
                                                       @RequestParam Long taskId,
                                                       @RequestParam String secdClassId) throws IOException {
        if (taskId == null) {
            return;
        }

        if (StrUtil.isBlank(secdClassId)) {
            return;
        }

        String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
        RecordRenderData data = sampleItemService.findSampleRenderDataByTaskIdAndClassId(taskId, secdClassId);
        List<RecordRenderItemData> itemDatas = data.getItemDatas();


        LocalDateTime now = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        if (secdClassId.equals("002002") || secdClassId.equals("0020022")) {//有组织废气、锅炉窑
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder()
                    .bind("datas", policy)
                    .bind("pollist", policy)
                    .build();
            Map<String, Object> resDataMap = buildOranWastAir(data, itemDatas, secdClassId, loginOutputVo);
            String resource = templatePath + "/organ_waste_gas_record.docx";
            String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
            WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);
        } else if (secdClassId.equals("0020021")) {
            //油烟
            Map<String, Object> resDataMap = buidCookingOilData(data, itemDatas, loginOutputVo);
            String resource = templatePath + "/cooking_oil_flume_record.docx";
            String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
            Configure config = Configure.builder().build();
            WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);

        } else if (secdClassId.equals("002003")
                || secdClassId.equals("002004")
                || secdClassId.equals("002005")
                || secdClassId.equals("002001")) {//环境空气

            if (itemDatas != null && !itemDatas.isEmpty()) {
                LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                Configure config = Configure.builder()
                        .bind("datas", policy)
                        .bind("equpList", policy)
                        .build();
                Map<String, Object> resDataMap = buildAmbientAirData(secdClassId, data, loginOutputVo, getAirDataMap(data, itemDatas), Constants.FileResource.FILE_RECORD_KQ);

                String resource = templatePath + "/ambient_air_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);

            }

        } else if (secdClassId.equals("001002")) {
            //地表水
            if (itemDatas != null && !itemDatas.isEmpty()) {
                Configure config = Configure.builder()
                        .bind("sample_table", new SampleTablePolicy(3, 22, new int[]{0, 1, 2, 3, 4, 5, 7, 9, 11, 13, 15, 17, 19, 20, 21}))
                        .build();
                SurfaceWaterTableData tableData = buildSurfaceWaterTableData(data, itemDatas, loginOutputVo);

                String resource = templatePath + "/surface_water_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, tableData);
            }
            return;
        } else if (secdClassId.equals("001003")) {
            //地下水
            if (itemDatas != null && !itemDatas.isEmpty()) {
                Configure config = Configure.builder()
                        .bind("sample_table", new SampleTablePolicy(4, 19, new int[]{0, 1, 2, 4, 5, 6, 7, 8, 10, 12, 14, 16, 17, 18}))
                        .build();
                GroundWaterTableData tableData = buildGroundWaterTableData(data, itemDatas, loginOutputVo);


                String resource = templatePath + "/ground_water_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, tableData);
            }
            return;

        } else if (secdClassId.equals("001001") || secdClassId.equals("001004")) {
            //雨水、废水
            if (itemDatas != null && !itemDatas.isEmpty()) {
                Configure config = Configure.builder()
                        .bind("sample_table", new SampleTablePolicy(2, 9, new int[]{0, 1, 2, 3, 5, 6, 7, 8}))
                        .build();
                WasteWaterTableData tableData = buildWasteWaterTableData(secdClassId, data, itemDatas, loginOutputVo);

                String resource = templatePath + "/waste_water_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, tableData);
            }
            return;
        } else if (secdClassId.equals("005001") || secdClassId.equals("006001")) {
            //固废
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder()
                    .bind("datas", policy)
                    .build();
            Map<String, Object> resDataMap = buildAmbientAirData(secdClassId, data, loginOutputVo, getSolidWastDataMap(data, itemDatas), Constants.FileResource.FILE_RECORD_GF);
            String resource = templatePath + "/solid_waste_record.docx";
            String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
            WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);
        } else if (secdClassId.equals("003001")) {
            //土壤
            LoopColumnTableRenderPolicy policy = new LoopColumnTableRenderPolicy();
            Configure config = Configure.builder()
                    .bind("datas", policy)
                    .build();
            Map<String, Object> resDataMap = buildSoilData(secdClassId, data, itemDatas, loginOutputVo);
            String resource = templatePath + "/soil_record.docx";
            String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
            WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);

        } else if (secdClassId.equals("003002")) {
            //底泥
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder()
                    .bind("datas", policy)
                    .build();
            Map<String, Object> resDataMap = buildSedimentData(secdClassId, data, itemDatas, loginOutputVo);
            String resource = templatePath + "/sediment_record.docx";
            String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
            WordUtils.doDownLoad(response, resource, fileName, config, resDataMap);

        } else if (secdClassId.equals("004001")) {
            List<RowRenderData> rrds = new ArrayList<>();
            NoiseTableData modeType0TabelData = buildRegionNoiseTableData(loginOutputVo, data, itemDatas, rrds);
            modeType0TabelData.setSecdClassName(secdClassName);
            //合并表格输出
            if (!rrds.isEmpty()) {
                Configure config = Configure.builder()
                        .bind("sample_table", new NoiseType0TablePolicy())
                        .build();
                String resource = templatePath + "/noise_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, modeType0TabelData);
            }
        } else if (secdClassId.equals("004002")) {
            //噪声：工业企业厂界噪声
            if (itemDatas != null && !itemDatas.isEmpty()) {
                List<RowRenderData> rrds = new ArrayList<>();
                NoiseTableData modeType3TabelData = buildIndustryNoiseTableData(loginOutputVo, data, itemDatas, rrds);
                modeType3TabelData.setSecdClassName(secdClassName);
                if (!rrds.isEmpty()) {
                    Configure config = Configure.builder()
                            .bind("sample_table", new NoiseType0TablePolicy())
                            .build();
                    String resource = templatePath + "/noise_record.docx";
                    String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                    WordUtils.doDownLoad(response, resource, fileName, config, modeType3TabelData);
                }
            }
        } else if (secdClassId.equals("004003")) {

            List<RowRenderData> rrds = new ArrayList<>();
            NoiseTableData noiseTableData = buildSocietyNoiseTableData(loginOutputVo, data, itemDatas, rrds);
            noiseTableData.setSecdClassName(secdClassName);
            if (!rrds.isEmpty()) {
                Configure config = Configure.builder()
                        .bind("sample_table", new NoiseType0TablePolicy())
                        .build();

                String resource = templatePath + "/noise_record.docx";
                String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                WordUtils.doDownLoad(response, resource, fileName, config, noiseTableData);
            }
        } else if (secdClassId.equals("004004")) {
            //噪声：城市环境振动
            if (itemDatas != null && !itemDatas.isEmpty()) {
                List<RowRenderData> rrds = new ArrayList<>();
                NoiseTableData modeType1TabelData = getEnvirVibraNoiseTableData(loginOutputVo, data, itemDatas, rrds);

                if (!rrds.isEmpty()) {
                    Configure config = Configure.builder()
                            .bind("sample_table", new NoiseType1TablePolicy())
                            .build();
                    String resource = templatePath + "/urban_area_record.docx";
                    String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                    WordUtils.doDownLoad(response, resource, fileName, config, modeType1TabelData);
                }

            }
        } else if (secdClassId.equals("004005")) {
            //噪声：道路交通噪声
            if (itemDatas != null && !itemDatas.isEmpty()) {
                List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> modeType2DataMap = buildRoadNoiseData(loginOutputVo, data, itemDatas, datas);

                if (!datas.isEmpty()) {
                    LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
                    Configure config = Configure.builder()
                            .bind("datas", policy)
                            .build();
                    int size = datas.size();
                    if (size < 6) {
                        List<Map<String, Object>> ndatas = new ArrayList<>();
                        ndatas.addAll(datas);
                        for (int i = 0; i < 6 - size; i++) {
                            ndatas.add(new HashMap<>());
                        }
                        modeType2DataMap.put("datas", ndatas);
                    }

                    String resource = templatePath + "/road_noise_record.docx";
                    String fileName = taskId + "" + DateUtils.getDefaultFormatDateStr(now);
                    WordUtils.doDownLoad(response, resource, fileName, config, modeType2DataMap);
                }

            }
        } else {
            return;
        }

    }

    /**
     * TODO 构建土壤数据
     *
     * @param secdClassId
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:55 下午
     */
    private Map<String, Object> buildSoilData(String secdClassId, RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        Map<String, Object> resDataMap = new HashMap<>();
        resDataMap.put("organName", loginOutputVo.getOrganName());
        resDataMap.put("checkEmps", data.getCheckEmps());
        resDataMap.put("reviewerEmp", data.getReviewerEmp());
        List<Map<String, Object>> renderDataList = new ArrayList<>();
        getSoil(data, itemDatas, renderDataList, secdClassId);
        String fileCode = Constants.FileResource.FILE_RECORD_TR.getKey();
        if (secdClassId.equals("003002")) {
            fileCode = Constants.FileResource.FILE_RECORD_DN.getKey();
        }
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        resDataMap.put("contContollerId", controllerId);
        resDataMap.put("list", renderDataList);
        return resDataMap;
    }


    /**
     * TODO 构建底泥
     *
     * @param secdClassId
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/19 10:56 上午
     */
    private Map<String, Object> buildSedimentData(String secdClassId, RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        Map<String, Object> resDataMap = new HashMap<>();
        resDataMap.put("organName", loginOutputVo.getOrganName());
        resDataMap.put("checkEmps", data.getCheckEmps());
        resDataMap.put("reviewerEmp", data.getReviewerEmp());
        List<Map<String, Object>> renderDataList = new ArrayList<>();
        getSediment(data, itemDatas, renderDataList, secdClassId);
        String fileCode = Constants.FileResource.FILE_RECORD_TR.getKey();
        if (secdClassId.equals("003002")) {
            fileCode = Constants.FileResource.FILE_RECORD_DN.getKey();
        }
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        resDataMap.put("contContollerId", controllerId);
        resDataMap.put("list", renderDataList);
        return resDataMap;
    }

    /**
     * TODO 构建废水数据
     *
     * @param secdClassId
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:50 下午
     */
    private WasteWaterTableData buildWasteWaterTableData(@RequestParam String secdClassId, RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        WasteWaterTableData tableData = getWasteWaterTableData(data, itemDatas);
        String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
        tableData.setSecdClassId(secdClassName)
                .setReviewerEmp(data.getReviewerEmp())
                .setCheckEmps(data.getCheckEmps());

        String fileCode = Constants.FileResource.FILE_RECORD_FYS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        tableData.setOrganName(loginOutputVo.getOrganName())
                .setContContollerId(controllerId);
        return tableData;
    }

    /**
     * TODO 构建地下水数据
     *
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:47 下午
     */
    private GroundWaterTableData buildGroundWaterTableData(RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        GroundWaterTableData tableData = getGroundWaterTableData(data, itemDatas);
        tableData.setOrganName(loginOutputVo.getOrganName())
                .setCheckEmps(data.getCheckEmps())
                .setReviewerEmp(data.getReviewerEmp());
        String fileCode = Constants.FileResource.FILE_RECORD_DXS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        tableData.setContContollerId(controllerId);
        return tableData;
    }

    /**
     * TODO 构建地表水数据
     *
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:45 下午
     */
    private SurfaceWaterTableData buildSurfaceWaterTableData(RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        SurfaceWaterTableData tableData = getSurfaceWaterTableData(data, itemDatas);
        tableData.setOrganName(loginOutputVo.getOrganName())
                .setCheckEmps(data.getCheckEmps())
                .setReviewerEmp(data.getReviewerEmp());
        String fileCode = Constants.FileResource.FILE_RECORD_DBS.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        tableData.setContContollerId(controllerId);
        return tableData;
    }

    /**
     * TODO 构建环境空气等数据
     *
     * @param secdClassId
     * @param data
     * @param loginOutputVo
     * @param airDataMap
     * @param fileRecordKq
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:42 下午
     */
    private Map<String, Object> buildAmbientAirData(String secdClassId, RecordRenderData data, LoginOutputVo loginOutputVo, Map<String, Object> airDataMap, Constants.FileResource fileRecordKq) {
        Map<String, Object> resDataMap = new HashMap<>();
        resDataMap.put("organName", loginOutputVo.getOrganName());
        resDataMap.put("checkEmps", data.getCheckEmps());
        resDataMap.put("reviewerEmp", data.getReviewerEmp());
//        Map<String, Object> dataMap = new HashMap<>();
//        airDataMap.forEach((key, val) -> {
//            if (val instanceof RecordValItem) {
//                String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
//                dataMap.put(key, singleVal);
//            } else {
//                dataMap.put(key, val);
//            }
//        });
        resDataMap.putAll(airDataMap);
        String fileCode = fileRecordKq.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        resDataMap.put("contContollerId", controllerId);
        String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
        resDataMap.put("secdClassId", secdClassName);
        return resDataMap;
    }

    /**
     * TODO 构建油烟数据
     *
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:37 下午
     */
    private Map<String, Object> buidCookingOilData(RecordRenderData data, List<RecordRenderItemData> itemDatas, LoginOutputVo loginOutputVo) {
        List<Map<String, Object>> renderDataList = new ArrayList<>();
        getCookOilFlume(data, itemDatas, renderDataList);
        String fileCode = Constants.FileResource.FILE_RECORD_YY.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        Map<String, Object> resDataMap = new HashMap<>();
        resDataMap.put("organName", loginOutputVo.getOrganName());
        resDataMap.put("checkEmps", data.getCheckEmps());
        resDataMap.put("reviewerEmp", data.getReviewerEmp());
        resDataMap.put("contContollerId", controllerId);
        resDataMap.put("list", renderDataList);
        return resDataMap;
    }

    /**
     * TODO 有组织废气、锅炉窑数据
     *
     * @param data
     * @param itemDatas
     * @param loginOutputVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/11 5:32 下午
     */
    private Map<String, Object> buildOranWastAir(RecordRenderData data, List<RecordRenderItemData> itemDatas, String scedClassId, LoginOutputVo loginOutputVo) {
        List<Map<String, Object>> renderDataList = new ArrayList<>();
        String fileCode = Constants.FileResource.FILE_RECORD_FQ.getKey();
        String controllerId = fileResourceService.getFileNoByFileCodeAndOrganId(fileCode, loginOutputVo.getOrganId());
        Map<String, Object> resDataMap = new HashMap<>();
        resDataMap.put("organName", loginOutputVo.getOrganName());
        resDataMap.put("checkEmps", data.getCheckEmps());
        resDataMap.put("reviewerEmp", data.getReviewerEmp());
        resDataMap.put("contContollerId", controllerId);
        getOrganWaste(data, scedClassId, itemDatas, renderDataList);
        resDataMap.put("list", renderDataList);
        return resDataMap;
    }


    /**
     * TODO 模板类型2 城市振动
     *
     * @param modeType1TabelData
     * @param rrds2
     * @param i
     * @param item
     * @param dataMap
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/9/5 6:32 下午
     */
    private void getModeType1TabelData(NoiseTableData modeType1TabelData, List<RowRenderData> rrds2, int i, RecordRenderItemData item, Map<String, Object> dataMap) {
        if (i == 0) {
            RecordValItem vibrationCategory = (RecordValItem) dataMap.get("vibrationCategory");
            modeType1TabelData.setVibrationCategory(BusiUtils.getSingleVal(vibrationCategory));
//            RecordValItem collectDate = (RecordValItem) dataMap.get("collectDate");
            modeType1TabelData.setCollectDate(item.getCollectDate());
            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
            modeType1TabelData.setDayWeather(BusiUtils.getSingleVal(weatherConditions));
            RecordValItem windSpeed = (RecordValItem) dataMap.get("windSpeed");
            modeType1TabelData.setDayWeedSpeed(BusiUtils.getSingleVal(windSpeed));

            RecordValItem beforeTest = (RecordValItem) dataMap.get("beforeTest");
            modeType1TabelData.setDayCalibBefore(BusiUtils.getSingleVal(beforeTest));

            RecordValItem afterTest = (RecordValItem) dataMap.get("afterTest");
            modeType1TabelData.setDayCalibAfter(BusiUtils.getSingleVal(afterTest));

            RecordValItem fieldTestingEquipment = (RecordValItem) dataMap.get("fieldTestingEquipment");
            modeType1TabelData.setFieldTestingEquipment(BusiUtils.getSingleVal(fieldTestingEquipment));

        } else if (i == 1) {
            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
            modeType1TabelData.setNightWeather(BusiUtils.getSingleVal(weatherConditions));
            RecordValItem windSpeed = (RecordValItem) dataMap.get("windSpeed");
            modeType1TabelData.setNightWeedSpeed(BusiUtils.getSingleVal(windSpeed));

            RecordValItem beforeTest = (RecordValItem) dataMap.get("beforeTest");
            modeType1TabelData.setNightCalibBefore(BusiUtils.getSingleVal(beforeTest));

            RecordValItem afterTest = (RecordValItem) dataMap.get("afterTest");
            modeType1TabelData.setNightCalibAfter(BusiUtils.getSingleVal(afterTest));
        }

        if ((i + 1) % 2 == 0) {
            //偶数行
            int size = rrds2.size();
            RowRenderData lastRrd = rrds2.get(size - 1);
            List<CellRenderData> cells = lastRrd.getCells();
            RecordValItem VLNum = (RecordValItem) dataMap.get("VLNum");
            CellRenderData crd = Cells.of(BusiUtils.getSingleVal(VLNum)).center().create();
            cells.set(4, crd);
        } else {
            //奇数行
            RecordValItem testNum = (RecordValItem) dataMap.get("testNum");
            RecordValItem mainSound = (RecordValItem) dataMap.get("mainVibrationSource");
            RecordValItem VLNum = (RecordValItem) dataMap.get("VLNum");
            RecordValItem groundConditions = (RecordValItem) dataMap.get("groundConditions");
            RowRenderData rdd = Rows.of(
                    BusiUtils.getSingleVal(testNum),
                    BusiUtils.getSingleVal(groundConditions),
                    BusiUtils.getSingleVal(mainSound),
                    BusiUtils.getSingleVal(VLNum),
                    null,
                    null
            ).center().create();
            rrds2.add(rdd);
        }
    }


    /**
     * TODO 模板类型0的数据
     *
     * @param modeType0TabelData
     * @param rrds
     * @param i
     * @param item
     * @param dataMap
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/9/5 6:31 下午
     */
    private void getModeType0Table(NoiseTableData modeType0TabelData, List<RowRenderData> rrds, int i, RecordRenderItemData item, Map<String, Object> dataMap) {
        RecordValItem modelType = (RecordValItem) dataMap.get("modelType");
        log.info("modelType->" + modelType + "  index->" + i);
        if (i == 0) {
//            RecordValItem collectDate = (RecordValItem) dataMap.get("collectDate");
            modeType0TabelData.setCollectDate(item.getCollectDate());
            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
            modeType0TabelData.setDayWeather(BusiUtils.getSingleVal(weatherConditions));
            RecordValItem windSpeed = (RecordValItem) dataMap.get("windSpeed");
            modeType0TabelData.setDayWeedSpeed(BusiUtils.getSingleVal(windSpeed));

            RecordValItem beforeTest = (RecordValItem) dataMap.get("beforeTest");
            modeType0TabelData.setDayCalibBefore(BusiUtils.getSingleVal(beforeTest));

            RecordValItem afterTest = (RecordValItem) dataMap.get("afterTest");
            modeType0TabelData.setDayCalibAfter(BusiUtils.getSingleVal(afterTest));

            SampleData sampleData = modeType0TabelData.getSampleData();
            RecordValItem fieldTestingEquipment = (RecordValItem) dataMap.get("checkEquipment");
            sampleData.setFieldTestingEquipment(BusiUtils.getSingleVal(fieldTestingEquipment));


            RecordValItem flowEquipment = (RecordValItem) dataMap.get("flowEquipment");
            sampleData.setFlowEquipment(BusiUtils.getSingleVal(flowEquipment));
            sampleData.setRemark(item.getCollectRemark());

        } else if (i == 1) {
            RecordValItem weatherConditions = (RecordValItem) dataMap.get("weatherConditions");
            modeType0TabelData.setNightWeather(BusiUtils.getSingleVal(weatherConditions));
            RecordValItem windSpeed = (RecordValItem) dataMap.get("windSpeed");
            modeType0TabelData.setNightWeedSpeed(BusiUtils.getSingleVal(windSpeed));

            RecordValItem beforeTest = (RecordValItem) dataMap.get("beforeTest");
            modeType0TabelData.setNightCalibBefore(BusiUtils.getSingleVal(beforeTest));

            RecordValItem afterTest = (RecordValItem) dataMap.get("afterTest");
            modeType0TabelData.setNightCalibAfter(BusiUtils.getSingleVal(afterTest));
        }

        if ((i + 1) % 2 == 0) {
            //偶数行
            int size = rrds.size();
            log.info("size->" + size);
            RowRenderData lastRrd = rrds.get(size - 1);
            List<CellRenderData> cells = lastRrd.getCells();
            RecordValItem leqdb = (RecordValItem) dataMap.get("leqdb");
            CellRenderData crd = Cells.of(BusiUtils.getSingleVal(leqdb)).center().create();
            cells.set(3, crd);
        } else {
            //奇数行
            RecordValItem testNum = (RecordValItem) dataMap.get("testNum");
            RecordValItem mainSound = (RecordValItem) dataMap.get("mainSound");
            RecordValItem leqdb = (RecordValItem) dataMap.get("leqdb");
            RowRenderData rdd = Rows.of(
                    BusiUtils.getSingleVal(testNum),
                    BusiUtils.getSingleVal(mainSound),
                    BusiUtils.getSingleVal(leqdb),
                    null,
                    null,
                    null,
                    null
            ).center().create();
            rrds.add(rdd);
        }
    }


    /**
     * TODO 构建土壤
     *
     * @param data
     * @param itemDatas
     * @param renderDataList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/25 11:48 上午
     */
    private void getSoil(RecordRenderData data, List<RecordRenderItemData> itemDatas, List<Map<String, Object>> renderDataList, String secdClassId) {
        if (itemDatas != null && !itemDatas.isEmpty()) {
            List<List<RecordRenderItemData>> lists = StringUtils.splistList(itemDatas, 6);
            List<Map<String, Object>> resDataMapList = lists.stream().map(subList -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("consignorName", data.getConsignorName());
                dataMap.put("checkEmps", data.getCheckEmps());
                dataMap.put("reviewerEmp", data.getReviewerEmp());
                RecordRenderItemData lastData = subList.get(0);
                Map<String, Object> lastDataDataMap = lastData.getDataMap();
//                RecordValItem collectDate = (RecordValItem) lastDataDataMap.get("collectDate");
                dataMap.put("collectDate", lastData.getCollectDate());
                RecordValItem sampBasisItem = (RecordValItem) lastDataDataMap.get("sampBasis");
                dataMap.put("sampBasis", BusiUtils.getSingleVal(sampBasisItem));
                String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                dataMap.put("secdClassId", secdClassName);
                RecordValItem transport = (RecordValItem) lastDataDataMap.get("transport");
                String transportValue = BusiUtils.getSingleVal(transport);
                dataMap.put("transport", transportValue);

//                if (transportValue != null) {
//                    if (transportValue.equals("0")) {
//                        transportValue = "冷藏";
//                    } else if (transportValue.equals("1")) {
//                        transportValue = "常温";
//                    } else {
//                        transportValue = "其他";
//                    }
//                    dataMap.put("transport", transportValue);
//                }

                Map<String, Object> lastDataMap = lastDataDataMap;
                RecordValItem checkEquipment = (RecordValItem) lastDataMap.get("checkEquipment");
                dataMap.put("checkEquipment", BusiUtils.getSingleVal(checkEquipment));
                RecordValItem sampEquipment = (RecordValItem) lastDataMap.get("sampEquipment");
                dataMap.put("sampEquipment", BusiUtils.getSingleVal(sampEquipment));
                RecordValItem heatBox = (RecordValItem) lastDataMap.get("heatBox");
                String heatBoxVal = BusiUtils.getSingleVal(heatBox);
                dataMap.put("heatBox", heatBoxVal != null ? heatBoxVal : null);
                RecordValItem temperature = (RecordValItem) lastDataMap.get("temperature");
                dataMap.put("temperature", BusiUtils.getSingleVal(temperature));
                RecordValItem sampleComplete = (RecordValItem) lastDataMap.get("sampleComplete");
                String sampleCompleteVal = BusiUtils.getSingleVal(sampleComplete);
                dataMap.put("sampleComplete", sampleCompleteVal != null ? sampleCompleteVal : null);

                List<Map<String, Object>> dataList = new ArrayList<>();
                int size = subList.size();
                for (int i = 0; i < size; i++) {
                    Map<String, Object> datas = new HashMap<>();
                    RecordRenderItemData itemData = subList.get(i);
                    datas.put("factorPoint", itemData.getFactorPoint());
                    datas.put("sampItemId", itemData.getSampItemId());
                    datas.put("collectTime", DateUtils.getDefaultTimeDateStr(itemData.getCollectTime()));
                    datas.put("checkItems", itemData.getFactorItems());
                    Map<String, Object> dataMap1 = itemData.getDataMap();
                    Map<String, Object> resMap = new HashMap<>();
                    dataMap1.forEach((key, val) -> {
                        if (val instanceof RecordValItem) {
                            String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
                            resMap.put(key, singleVal);
                        } else {
                            resMap.put(key, val);
                        }
                    });
                    datas.putAll(resMap);
                    dataList.add(datas);
                }
                if (size < 6) {
                    for (int i = 0; i < 6 - size; i++) {
                        dataList.add(new HashMap<>());
                    }
                }
                dataMap.put("datas", dataList);
                return dataMap;
            }).collect(Collectors.toList());
            renderDataList.addAll(resDataMapList);
        }
    }


    /**
     * TODO 构建底泥
     *
     * @param data
     * @param itemDatas
     * @param renderDataList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/25 11:48 上午
     */
    private void getSediment(RecordRenderData data, List<RecordRenderItemData> itemDatas, List<Map<String, Object>> renderDataList, String secdClassId) {
        if (itemDatas != null && !itemDatas.isEmpty()) {
            List<List<RecordRenderItemData>> lists = StringUtils.splistList(itemDatas, 6);
            List<Map<String, Object>> resDataMapList = lists.stream().map(subList -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("consignorName", data.getConsignorName());
                dataMap.put("checkEmps", data.getCheckEmps());
                dataMap.put("reviewerEmp", data.getReviewerEmp());
                RecordRenderItemData lastData = subList.get(0);
                Map<String, Object> lastDataDataMap = lastData.getDataMap();
                dataMap.put("collectDate", lastData.getCollectDate());
                //采样工具
                RecordValItem sampBasisItem = (RecordValItem) lastDataDataMap.get("sampEquipment");
                dataMap.put("sampEquipment", BusiUtils.getSingleVal(sampBasisItem));
                //样品现场处理情况
                RecordValItem onSampleItem = (RecordValItem) lastDataDataMap.get("onSample");
                dataMap.put("onSample", BusiUtils.getSingleVal(onSampleItem));

                List<Map<String, Object>> dataList = new ArrayList<>();
                for (int i = 0; i < subList.size(); i++) {
                    Map<String, Object> datas = new HashMap<>();
                    RecordRenderItemData itemData = subList.get(i);
                    datas.put("factorPoint", itemData.getFactorPoint());
                    datas.put("sampItemId", itemData.getSampItemId());
                    datas.put("collectTime", DateUtils.getDefaultTimeDateStr(itemData.getCollectTime()));
                    datas.put("checkItems", itemData.getFactorItems());
                    Map<String, Object> dataMap1 = itemData.getDataMap();
                    Map<String, Object> resMap = new HashMap<>();
                    dataMap1.forEach((key, val) -> {
                        if (val instanceof RecordValItem) {
                            String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
                            resMap.put(key, singleVal);
                        } else {
                            resMap.put(key, val);
                        }
                    });
                    datas.putAll(resMap);
                    dataList.add(datas);
                }
                dataMap.put("datas", dataList);
                return dataMap;
            }).collect(Collectors.toList());

            renderDataList.addAll(resDataMapList);
        }
    }

    /**
     * TODO 构建固废
     *
     * @param data
     * @param itemDatas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/26 3:50 下午
     */
    private Map<String, Object> getSolidWastDataMap(RecordRenderData data, List<RecordRenderItemData> itemDatas) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("consignorName", data.getConsignorName());
        if (itemDatas != null && !itemDatas.isEmpty()) {
            RecordRenderItemData lastData = itemDatas.get(0);
            Map<String, Object> lastDataMap = lastData.getDataMap();
//            RecordValItem collectDate = (RecordValItem) lastDataMap.get("collectDate");
            dataMap.put("collectDate", lastData.getCollectDate());
            RecordValItem sampBasisItem = (RecordValItem) lastDataMap.get("sampBasis");
            dataMap.put("sampBasis", BusiUtils.getSingleVal(sampBasisItem));
            RecordValItem transport = (RecordValItem) lastDataMap.get("transport");
            String transportVal = BusiUtils.getSingleVal(transport);
            if (transportVal != null) {
                String val = null;
                if (transportVal.equals("0")) {
                    val = "冷藏";
                } else if (transportVal.equals("1")) {
                    val = "常温";
                } else {
                    val = "其他";
                }
                dataMap.put("transport", val);
            }

            RecordValItem heatBox = (RecordValItem) lastDataMap.get("heatBox");
            String heatBoxVal = BusiUtils.getSingleVal(heatBox);
            dataMap.put("heatBox", heatBoxVal);
            RecordValItem temperature = (RecordValItem) lastDataMap.get("temperature");
            dataMap.put("temperature", BusiUtils.getSingleVal(temperature));
            RecordValItem sampleComplete = (RecordValItem) lastDataMap.get("sampleComplete");
            String sampleCompleteVal = BusiUtils.getSingleVal(sampleComplete);
            dataMap.put("sampleComplete", sampleCompleteVal);
            RecordValItem sampEquipment = (RecordValItem) lastDataMap.get("sampEquipment");
            dataMap.put("sampEquipment", BusiUtils.getSingleVal(sampEquipment));
            List<Map<String, Object>> dataList = new ArrayList<>();
            List<Map<String, Object>> resDataList = itemDatas.stream().map(item -> {
                Map<String, Object> itemDataMap = new HashMap<>();
                Map<String, Object> resMap = new HashMap<>();
                Map<String, Object> innerDataMap = item.getDataMap();
                innerDataMap.forEach((key, val) -> {
                    if (val instanceof RecordValItem) {
                        String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
                        resMap.put(key, singleVal);
                    } else {
                        resMap.put(key, val);
                    }
                });
                itemDataMap.putAll(resMap);
                itemDataMap.put("factorPoint", item.getFactorPoint());
                itemDataMap.put("checkItems", item.getFactorItems());
                itemDataMap.put("sampItemId", item.getSampItemId());
                itemDataMap.put("collectTime", DateUtils.getDefaultTimeDateStr(item.getCollectTime()));
                itemDataMap.put("sampleFixative", item.getSampleFixative());
                itemDataMap.put("sampleProperties", item.getSampleProperties());
                RecordValItem sampleNumItem = (RecordValItem) innerDataMap.get("sampleNum");
                itemDataMap.put("sampleNum", BusiUtils.getSingleVal(sampleNumItem));
                RecordValItem gps = (RecordValItem) item.getDataMap().get("gps");
                dataMap.put("gps", BusiUtils.getSingleVal(gps));
                RecordValItem weight = (RecordValItem) item.getDataMap().get("weight");
                dataMap.put("weight", (BusiUtils.getSingleVal(weight)));
                return itemDataMap;
            }).collect(Collectors.toList());
            dataList.addAll(resDataList);
            int size = itemDatas.size();
            if (size < 7) {
                for (int i = 0; i < 7 - size; i++) {
                    dataList.add(new HashMap<>());
                }
            }
            dataMap.put("datas", dataList);
        }
        return dataMap;
    }


    /**
     * TODO 获取废水
     *
     * @param data
     * @param itemDatas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/29 7:28 下午
     */
    private WasteWaterTableData getWasteWaterTableData(RecordRenderData data, List<RecordRenderItemData> itemDatas) {
        //样品编号
        WasteWaterTableData tableData = new WasteWaterTableData();
        tableData.setConsignorName(data.getConsignorName());
        if (itemDatas != null && !itemDatas.isEmpty()) {
            RecordRenderItemData lastData = itemDatas.get(0);
            Map<String, SampleFactorDataParam> lastDataFactorMap = (lastData.getFactorMap() != null ? lastData.getFactorMap() : new HashMap<>());
            Map<String, SampleFactorDataParam> lastFactorNameMap = lastDataFactorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));
            //PH
            SampleFactorDataParam lastPh = lastFactorNameMap.get("pH值");
            ImmutablePair<String, String> selectEquipOrCode = getSelectEquipOrCode(lastPh);
            tableData.setSelectEquip(selectEquipOrCode.left);
            tableData.setSelectEquipCode(selectEquipOrCode.right);
//

            tableData.setPositioningOne(lastPh != null ? lastPh.getPositioningOne() : null);
            tableData.setPositioningTwo(lastPh != null ? lastPh.getPositioningTwo() : null);
            tableData.setPositioningThree(lastPh != null ? lastPh.getPositioningThree() : null);


            Map<String, Object> lastDataMap = lastData.getDataMap();
            RecordValItem heatBox = (RecordValItem) lastDataMap.get("heatBox");
            String heatBoxVal = BusiUtils.getSingleVal(heatBox);
            tableData.setHeatBox(heatBoxVal != null ? heatBoxVal : null);
            RecordValItem temperature = (RecordValItem) lastDataMap.get("temperature");
            String temperatureVal = BusiUtils.getSingleVal(temperature);
            tableData.setTemperature(temperatureVal);

            RecordValItem sampleComplete = (RecordValItem) lastDataMap.get("sampleComplete");
            String sampleCompleteVal = BusiUtils.getSingleVal(sampleComplete);
            tableData.setSampleComplete(sampleCompleteVal != null ? sampleCompleteVal : null);
            RecordValItem wastewater = (RecordValItem) lastDataMap.get("wastewater");
            String wastewaterVal = BusiUtils.getSingleVal(wastewater);
            tableData.setWastewater(wastewaterVal);
            RecordValItem processConditions = (RecordValItem) lastDataMap.get("processConditions");
            String processConditionsVal = BusiUtils.getSingleVal(processConditions);
            tableData.setProcessConditions(processConditionsVal);

            RecordValItem sampBasisItem = (RecordValItem) lastDataMap.get("sampBasis");
//            RecordValItem collectDate = (RecordValItem) lastDataMap.get("collectDate");
            tableData.setCollectDate(lastData.getCollectDate())
                    .setSampBasis(BusiUtils.getSingleVal(sampBasisItem));
            SampleData sampleData = new SampleData();
            tableData.setSampleData(sampleData);
            List<RowRenderData> rrds = new ArrayList<>();
            sampleData.setFactors(rrds);
            itemDatas.stream().forEach(item -> {
                Map<String, Object> dataMap1 = item.getDataMap();
                Map<String, SampleFactorDataParam> factorMap = (item.getFactorMap() != null ? item.getFactorMap() : new HashMap<>());
                Map<String, SampleFactorDataParam> factorNameMap = factorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));
                RecordValItem organoleptic = (RecordValItem) dataMap1.get("organoleptic");//感官描述
                String organolepticVal = BusiUtils.getSingleVal(organoleptic);
                //PH
                List<RecordItemValParam> phParms = null;
                SampleFactorDataParam ph = factorNameMap.get("pH值");
                if (ph != null) {
                    String factorData = ph.getFactorData();
                    phParms = BusiUtils.parseRecordItemJson(factorData);
                }
                RecordValItem sampleNumItem = (RecordValItem) dataMap1.get("sampleNum");
                RowRenderData rrd = Rows.of(
                        item.getSampItemId(),
                        item.getFactorPoint(),
                        DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                        organolepticVal,//感官
                        (phParms != null && phParms.size() > 0) ? phParms.get(0).getVal() : null,//PH值
                        (phParms != null && phParms.size() > 2) ? phParms.get(2).getVal() : null,
                        item.getFactorItems(),
                        BusiUtils.getSingleVal(sampleNumItem),
                        item.getCollectRemark()

                ).center().create();

                RowRenderData rrd2 = Rows.of(
                        item.getSampItemId(),
                        item.getFactorPoint(),
                        DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                        organolepticVal,//感官
                        (phParms != null && phParms.size() > 1) ? phParms.get(1).getVal() : null,//PH值
                        (phParms != null && phParms.size() > 2) ? phParms.get(2).getVal() : null,
                        item.getFactorItems(),
                        BusiUtils.getSingleVal(sampleNumItem),
                        item.getCollectRemark()

                ).center().create();

                rrds.add(rrd);
                rrds.add(rrd2);

            });

        }
        return tableData;
    }

    /**
     * TODO 获取地下水
     *
     * @param data
     * @param itemDatas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/29 7:28 下午
     */
    private GroundWaterTableData getGroundWaterTableData(RecordRenderData data, List<RecordRenderItemData> itemDatas) {
        GroundWaterTableData tableData = new GroundWaterTableData();
        tableData.setConsignorName(data.getConsignorName());
        if (itemDatas != null && !itemDatas.isEmpty()) {
            RecordRenderItemData lastData = itemDatas.get(0);
            Map<String, SampleFactorDataParam> lastFactorMap = (lastData.getFactorMap() != null ? lastData.getFactorMap() : new HashMap<>());
            Map<String, SampleFactorDataParam> lastFactorNameMap = lastFactorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));
            String checkItems = itemDatas.stream().map(item -> item.getFactorItems()).collect(Collectors.joining(","));
            checkItems = Arrays.stream(checkItems.split(",")).distinct().collect(Collectors.joining(","));
            tableData.setCheckItems(checkItems);
            //PH
            SampleFactorDataParam lastPh = lastFactorNameMap.get("pH值");
            ImmutablePair<String, String> lastPhEquip = getSelectEquipOrCode(lastPh);
            tableData.setSelectEquip(lastPhEquip.left);
            tableData.setSelectEquipCode(lastPhEquip.right);
            tableData.setGroundConditions(lastPh != null ? lastPh.getGroundConditions() : null);

            //浊度
            SampleFactorDataParam lastTurbidity = lastFactorNameMap.get("浊度");
            tableData.setSelectEquip2(lastTurbidity != null ? lastTurbidity.getCheckEquipment() : null);
            tableData.setGroundConditions2(lastTurbidity != null ? lastTurbidity.getGroundConditions() : null);


            //溶解氧
            SampleFactorDataParam lastDissOxygen = lastFactorNameMap.get("溶解氧");
            ImmutablePair<String, String> lastDissOxygenEquip = getSelectEquipOrCode(lastDissOxygen);
            tableData.setSelectEquip3(lastDissOxygenEquip.left);
            tableData.setSelectEquipCode3(lastDissOxygenEquip.right);
            tableData.setGroundConditions3(lastDissOxygen != null ? lastDissOxygen.getGroundConditions() : null);

            //水温
            SampleFactorDataParam lastWaterTemp = lastFactorNameMap.get("水温");
            ImmutablePair<String, String> lastWaterTempEquip = getSelectEquipOrCode(lastWaterTemp);
            tableData.setSelectEquip4(lastWaterTempEquip.left);
            tableData.setSelectEquipCode4(lastWaterTempEquip.right);
            tableData.setGroundConditions4(lastWaterTemp != null ? lastWaterTemp.getGroundConditions() : null);

            //电导率
            SampleFactorDataParam lastConductivity = lastFactorNameMap.get("电导率");
            ImmutablePair<String, String> lastConductivityEquip = getSelectEquipOrCode(lastConductivity);
            tableData.setSelectEquip5(lastConductivityEquip.left);
            tableData.setSelectEquipCode5(lastConductivityEquip.right);
            tableData.setGroundConditions5(lastConductivity != null ? lastConductivity.getGroundConditions() : null);
            //氧化还原电位
            SampleFactorDataParam lastRedoxPotential = lastFactorNameMap.get("氧化还原电位");
            ImmutablePair<String, String> lastRedoxPotentialEquip = getSelectEquipOrCode(lastRedoxPotential);
            tableData.setSelectEquip6(lastRedoxPotentialEquip.left);
            tableData.setSelectEquipCode6(lastRedoxPotentialEquip.right);
            tableData.setGroundConditions6(lastRedoxPotential != null ? lastRedoxPotential.getGroundConditions() : null);

            Map<String, Object> lastDataMap = lastData.getDataMap();
            RecordValItem heatBox = (RecordValItem) lastDataMap.get("heatBox");
            String heatBoxVal = BusiUtils.getSingleVal(heatBox);
            tableData.setHeatBox(heatBoxVal != null ? heatBoxVal : null);
            RecordValItem temperature = (RecordValItem) lastDataMap.get("temperature");
            tableData.setTemperature(BusiUtils.getSingleVal(temperature));

            RecordValItem sampleComplete = (RecordValItem) lastDataMap.get("sampleComplete");
            String sampleCompleteVal = BusiUtils.getSingleVal(sampleComplete);
            tableData.setSampleComplete(sampleCompleteVal != null ? sampleCompleteVal : null);

            RecordValItem heavyRain = (RecordValItem) lastDataMap.get("heavyRain");
            String heavyRainVal = BusiUtils.getSingleVal(heavyRain);
            tableData.setHeavyRain(heavyRainVal != null ? heavyRainVal : null);


            RecordValItem stagnantWater = (RecordValItem) lastDataMap.get("stagnantWater");
            String stagnantWaterVal = BusiUtils.getSingleVal(stagnantWater);
            tableData.setStagnantWater(stagnantWaterVal != null ? stagnantWaterVal : null);


            RecordValItem weatherCondition = (RecordValItem) lastDataMap.get("weatherCondition");
            RecordValItem sampBasis = (RecordValItem) lastDataMap.get("sampBasis");
//            RecordValItem collectDate = (RecordValItem) lastDataMap.get("collectDate");
            tableData.setWeatherCondition(BusiUtils.getSingleVal(weatherCondition))
                    .setCollectDate(lastData.getCollectDate())
                    .setSampBasis(BusiUtils.getSingleVal(sampBasis));
            SampleData sampleData = new SampleData();
            tableData.setSampleData(sampleData);
            List<RowRenderData> rrds = new ArrayList<>();
            sampleData.setFactors(rrds);
            itemDatas.stream().forEach(item -> {
                Map<String, Object> dataMap1 = item.getDataMap();
                Map<String, SampleFactorDataParam> factorMap = (item.getFactorMap() != null ? item.getFactorMap() : new HashMap<>());
                Map<String, SampleFactorDataParam> factorNameMap = factorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));
                RecordValItem depth = (RecordValItem) dataMap1.get("depth");
                String depthVal = BusiUtils.getSingleVal(depth);
                RecordValItem sectionWidth = (RecordValItem) dataMap1.get("sectionWidth");
                String sectionWidthVal = BusiUtils.getSingleVal(sectionWidth);
                RecordValItem organoleptic = (RecordValItem) dataMap1.get("organoleptic");
                String organolepticVal = BusiUtils.getSingleVal(organoleptic);


                //浊度
                SampleFactorDataParam turbidity = factorNameMap.get("浊度");
                List<RecordItemValParam> turbidityParams = null;
                if (turbidity != null && turbidity.getFactorData() != null) {
                    turbidityParams = BusiUtils.parseRecordItemJson(turbidity.getFactorData());
                }
                //溶解氧
                SampleFactorDataParam dissOxygen = factorNameMap.get("溶解氧");
                List<RecordItemValParam> dissOxygenParams = null;
                if (dissOxygen != null && dissOxygen.getFactorData() != null) {
                    dissOxygenParams = BusiUtils.parseRecordItemJson(dissOxygen.getFactorData());
                }
                //水温
                SampleFactorDataParam waterTemp = factorNameMap.get("水温");
                List<RecordItemValParam> waterTempParams = null;
                if (waterTemp != null && waterTemp.getFactorData() != null) {
                    waterTempParams = BusiUtils.parseRecordItemJson(waterTemp.getFactorData());
                }
                //PH
                SampleFactorDataParam ph = factorNameMap.get("pH值");
                List<RecordItemValParam> phParams = null;
                if (ph != null && ph.getFactorData() != null) {
                    phParams = BusiUtils.parseRecordItemJson(ph.getFactorData());
                }
                //电导率
                SampleFactorDataParam conductivity = factorNameMap.get("电导率");
                List<RecordItemValParam> conductivityParams = null;
                if (conductivity != null && conductivity.getFactorData() != null) {
                    conductivityParams = BusiUtils.parseRecordItemJson(conductivity.getFactorData());
                }
                //氧化还原电位
                SampleFactorDataParam redoxPotential = factorNameMap.get("氧化还原电位");
                List<RecordItemValParam> redoxPotentialParams = null;
                if (redoxPotential != null && redoxPotential.getFactorData() != null) {
                    redoxPotentialParams = BusiUtils.parseRecordItemJson(redoxPotential.getFactorData());
                }

                RecordValItem sampleNumItem = (RecordValItem) dataMap1.get("sampleNum");

                RowRenderData rrd = Rows.of(
                        item.getSampItemId(),
                        item.getFactorPoint(),
                        DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                        depthVal,
                        null,
                        sectionWidthVal,
                        organolepticVal,//感官
                        (redoxPotentialParams != null && redoxPotentialParams.size() > 0) ? redoxPotentialParams.get(0).getVal() : null,//氧化还原电位
                        (dissOxygenParams != null && dissOxygenParams.size() > 0) ? dissOxygenParams.get(0).getVal() : null,//溶解氧
                        (turbidityParams != null && turbidityParams.size() > 1) ? turbidityParams.get(1).getVal() : null,//浊度
                        (turbidityParams != null && turbidityParams.size() > 2) ? turbidityParams.get(2).getVal() : null,
                        (waterTempParams != null && waterTempParams.size() > 1) ? waterTempParams.get(1).getVal() : null,//水温
                        (waterTempParams != null && waterTempParams.size() > 2) ? waterTempParams.get(2).getVal() : null,//水温
                        (phParams != null && phParams.size() > 1) ? phParams.get(1).getVal() : null,//PH值
                        (phParams != null && phParams.size() > 2) ? phParams.get(2).getVal() : null,//PH值
                        (conductivityParams != null && conductivityParams.size() > 1) ? conductivityParams.get(1).getVal() : null,//电导率
                        (conductivityParams != null && conductivityParams.size() > 2) ? conductivityParams.get(2).getVal() : null,
                        BusiUtils.getSingleVal(sampleNumItem),
                        item.getCollectRemark()

                ).center().create();

                RowRenderData rrd2 = Rows.of(
                        item.getSampItemId(),
                        item.getFactorPoint(),
                        DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                        depthVal,
                        null,
                        sectionWidthVal,
                        organolepticVal,//感官
                        (redoxPotentialParams != null && redoxPotentialParams.size() > 0) ? redoxPotentialParams.get(0).getVal() : null,//氧化还原电位
                        (dissOxygenParams != null && dissOxygenParams.size() > 1) ? dissOxygenParams.get(1).getVal() : null,//溶解氧
                        (turbidityParams != null && turbidityParams.size() > 0) ? turbidityParams.get(0).getVal() : null,//浊度
                        (turbidityParams != null && turbidityParams.size() > 2) ? turbidityParams.get(2).getVal() : null,
                        (waterTempParams != null && waterTempParams.size() > 0) ? waterTempParams.get(0).getVal() : null,//水温
                        (waterTempParams != null && waterTempParams.size() > 2) ? waterTempParams.get(2).getVal() : null,//水温
                        (phParams != null && phParams.size() > 0) ? phParams.get(0).getVal() : null,//PH值
                        (phParams != null && phParams.size() > 2) ? phParams.get(2).getVal() : null,//PH值
                        (conductivityParams != null && conductivityParams.size() > 0) ? conductivityParams.get(0).getVal() : null,//电导率
                        (conductivityParams != null && conductivityParams.size() > 2) ? conductivityParams.get(2).getVal() : null,
                        BusiUtils.getSingleVal(sampleNumItem),
                        item.getCollectRemark()

                ).center().create();

                rrds.add(rrd);
                rrds.add(rrd2);

            });

        }
        return tableData;
    }

    /**
     * TODO 获取地表水
     *
     * @param data
     * @param itemDatas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/29 7:28 下午
     */
    private SurfaceWaterTableData getSurfaceWaterTableData(RecordRenderData data, List<RecordRenderItemData> itemDatas) {
        SurfaceWaterTableData tableData = new SurfaceWaterTableData();
        tableData.setConsignorName(data.getConsignorName());
        if (itemDatas != null && !itemDatas.isEmpty()) {
            RecordRenderItemData lastData = itemDatas.get(0);
            Map<String, SampleFactorDataParam> lastFactorMap = lastData.getFactorMap();
            if (lastFactorMap != null && !lastFactorMap.isEmpty()) {
                Map<String, SampleFactorDataParam> lastFactorNameMap = lastFactorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));
                //PH
                SampleFactorDataParam lastPh = lastFactorNameMap.get("pH值");
                //获取型号或编号
                ImmutablePair<String, String> phPair = getSelectEquipOrCode(lastPh);
                tableData.setSelectEquip(phPair.left)
                        .setSelectEquipCode(phPair.getRight())
                        .setGroundConditions(lastPh != null ? lastPh.getGroundConditions() : null);

                //浊度
                SampleFactorDataParam lastTurbidity = lastFactorNameMap.get("浊度");
                ImmutablePair<String, String> turbidityPair = getSelectEquipOrCode(lastTurbidity);
                tableData.setSelectEquip2(turbidityPair.left)
                        .setSelectEquipCode2(turbidityPair.getRight())
                        .setGroundConditions2(lastTurbidity != null ? lastTurbidity.getGroundConditions() : null);

                //溶解氧
                SampleFactorDataParam lastDissOxygen = lastFactorNameMap.get("溶解氧");
                ImmutablePair<String, String> dissOxygenPair = getSelectEquipOrCode(lastDissOxygen);
                tableData.setSelectEquip3(dissOxygenPair.left)
                        .setSelectEquipCode3(dissOxygenPair.getRight())
                        .setGroundConditions3(lastDissOxygen != null ? lastDissOxygen.getGroundConditions() : null);

                //水温
                SampleFactorDataParam lastWaterTemp = lastFactorNameMap.get("水温");
                ImmutablePair<String, String> waterTempPair = getSelectEquipOrCode(lastWaterTemp);
                tableData.setSelectEquip4(waterTempPair.left)
                        .setSelectEquipCode4(waterTempPair.getRight())
                        .setGroundConditions4(lastWaterTemp != null ? lastWaterTemp.getGroundConditions() : null);
                //电导率
                SampleFactorDataParam lastConductivity = lastFactorNameMap.get("电导率");
                ImmutablePair<String, String> conductivityPair = getSelectEquipOrCode(lastConductivity);
                tableData.setSelectEquip5(conductivityPair.left)
                        .setSelectEquipCode5(conductivityPair.getRight())
                        .setGroundConditions5(lastConductivity != null ? lastConductivity.getGroundConditions() : null);

                Map<String, Object> lastDataMap = lastData.getDataMap();
                RecordValItem heatBox = (RecordValItem) lastDataMap.get("heatBox");
                String heatBoxVal = BusiUtils.getSingleVal(heatBox);
                tableData.setHeatBox(heatBoxVal != null ? heatBoxVal : null);
                RecordValItem temperature = (RecordValItem) lastDataMap.get("temperature");
                String temperatureVal = BusiUtils.getSingleVal(temperature);
                tableData.setTemperature(temperatureVal);

                RecordValItem sampleComplete = (RecordValItem) lastDataMap.get("sampleComplete");
                String sampleCompleteVal = BusiUtils.getSingleVal(sampleComplete);
                tableData.setSampleComplete(sampleCompleteVal != null ? sampleCompleteVal : null);


                RecordValItem weatherCondition = (RecordValItem) lastDataMap.get("weatherCondition");
                RecordValItem sampBasisItem = (RecordValItem) lastDataMap.get("sampBasis");
//                RecordValItem collectDate = (RecordValItem) lastDataMap.get("collectDate");
                String weatherConditionVal = BusiUtils.getSingleVal(weatherCondition);
                tableData.setWeatherCondition(weatherConditionVal)
                        .setCollectDate(lastData.getCollectDate())
                        .setSampBasis(BusiUtils.getSingleVal(sampBasisItem));
                SampleData sampleData = new SampleData();
                tableData.setSampleData(sampleData);
                List<RowRenderData> rrds = new ArrayList<>();
                sampleData.setFactors(rrds);
                String checkItems = itemDatas.stream().map(item -> item.getFactorItems()).collect(Collectors.joining(","));
                checkItems = Arrays.stream(checkItems.split(",")).distinct().collect(Collectors.joining(","));
                tableData.setCheckItems(checkItems);
                itemDatas.stream().forEach(item -> {
                    Map<String, Object> dataMap1 = item.getDataMap();
                    Map<String, SampleFactorDataParam> factorMap = item.getFactorMap();
                    if (factorMap != null && !factorMap.isEmpty()) {
                        Map<String, SampleFactorDataParam> factorNameMap = factorMap.values().stream().collect(Collectors.toMap(SampleFactorDataParam::getFactorName, Function.identity()));

                        RecordValItem depth = (RecordValItem) dataMap1.get("depth");
                        String depthVal = BusiUtils.getSingleVal(depth);
                        RecordValItem sectionWidth = (RecordValItem) dataMap1.get("sectionWidth");
                        String sectionWidthVal = BusiUtils.getSingleVal(sectionWidth);
                        RecordValItem organoleptic = (RecordValItem) dataMap1.get("organoleptic");
                        String organolepticVal = BusiUtils.getSingleVal(organoleptic);

                        //浊度
                        SampleFactorDataParam turbidity = factorNameMap.get("浊度");
                        List<RecordItemValParam> turbidityParams = null;
                        if (turbidity != null && turbidity.getFactorData() != null) {
                            turbidityParams = BusiUtils.parseRecordItemJson(turbidity.getFactorData());
                        }
                        //溶解氧
                        SampleFactorDataParam dissOxygen = factorNameMap.get("溶解氧");
                        List<RecordItemValParam> dissOxygenParams = null;
                        if (dissOxygen != null && dissOxygen.getFactorData() != null) {
                            dissOxygenParams = BusiUtils.parseRecordItemJson(dissOxygen.getFactorData());
                        }
                        //水温
                        SampleFactorDataParam waterTemp = factorNameMap.get("水温");
                        List<RecordItemValParam> waterTempParams = null;
                        if (waterTemp != null && waterTemp.getFactorData() != null) {
                            waterTempParams = BusiUtils.parseRecordItemJson(waterTemp.getFactorData());
                        }
                        //PH
                        SampleFactorDataParam ph = factorNameMap.get("pH值");
                        List<RecordItemValParam> phParams = null;
                        if (ph != null && ph.getFactorData() != null) {
                            phParams = BusiUtils.parseRecordItemJson(ph.getFactorData());
                        }
                        //电导率
                        SampleFactorDataParam conductivity = factorNameMap.get("电导率");
                        List<RecordItemValParam> conductivityParams = null;
                        if (conductivity != null && conductivity.getFactorData() != null) {
                            conductivityParams = BusiUtils.parseRecordItemJson(conductivity.getFactorData());
                        }

                        RecordValItem sampleNumItem = (RecordValItem) dataMap1.get("sampleNum");
                        RowRenderData rrd = Rows.of(
                                item.getSampItemId(),
                                item.getFactorPoint(),
                                DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                                depthVal,
                                sectionWidthVal,
                                organolepticVal,
                                (turbidityParams != null && turbidityParams.size() > 0) ? turbidityParams.get(0).getVal() : null,//浊度
                                (turbidityParams != null && turbidityParams.size() > 2) ? turbidityParams.get(2).getVal() : null,
                                null,
                                null,
                                (dissOxygenParams != null && dissOxygenParams.size() > 0) ? dissOxygenParams.get(0).getVal() : null,//溶解氧
                                (dissOxygenParams != null && dissOxygenParams.size() > 2) ? dissOxygenParams.get(2).getVal() : null,//溶解氧
                                (waterTempParams != null && waterTempParams.size() > 0) ? waterTempParams.get(0).getVal() : null,//水温
                                (waterTempParams != null && waterTempParams.size() > 2) ? waterTempParams.get(2).getVal() : null,//水温
                                (phParams != null && phParams.size() > 0) ? phParams.get(0).getVal() : null,//PH值
                                (phParams != null && phParams.size() > 2) ? phParams.get(2).getVal() : null,//PH值
                                (conductivityParams != null && conductivityParams.size() > 0) ? conductivityParams.get(0).getVal() : null,//电导率
                                (conductivityParams != null && conductivityParams.size() > 2) ? conductivityParams.get(2).getVal() : null,
                                null,
                                null,
                                BusiUtils.getSingleVal(sampleNumItem),
                                item.getCollectRemark()

                        ).center().create();


                        RowRenderData rrd2 = Rows.of(
                                item.getSampItemId(),
                                item.getFactorPoint(),
                                DateUtils.getDefaultTimeDateStr(item.getCollectTime()),
                                depthVal,
                                sectionWidthVal,
                                organolepticVal,
                                (turbidityParams != null && turbidityParams.size() > 1) ? turbidityParams.get(1).getVal() : null,//浊度
                                (turbidityParams != null && turbidityParams.size() > 2) ? turbidityParams.get(2).getVal() : null,
                                null,
                                null,
                                (dissOxygenParams != null && dissOxygenParams.size() > 1) ? dissOxygenParams.get(1).getVal() : null,//溶解氧
                                (dissOxygenParams != null && dissOxygenParams.size() > 2) ? dissOxygenParams.get(2).getVal() : null,//溶解氧
                                (waterTempParams != null && waterTempParams.size() > 1) ? waterTempParams.get(1).getVal() : null,//水温
                                (waterTempParams != null && waterTempParams.size() > 2) ? waterTempParams.get(2).getVal() : null,//水温
                                (phParams != null && phParams.size() > 1) ? phParams.get(1).getVal() : null,//PH值
                                (phParams != null && phParams.size() > 2) ? phParams.get(2).getVal() : null,//PH值
                                (conductivityParams != null && conductivityParams.size() > 1) ? conductivityParams.get(1).getVal() : null,//电导率
                                (conductivityParams != null && conductivityParams.size() > 2) ? conductivityParams.get(2).getVal() : null,
                                null,
                                null,
                                BusiUtils.getSingleVal(sampleNumItem),
                                item.getCollectRemark()

                        ).center().create();

                        rrds.add(rrd);
                        rrds.add(rrd2);

                    }

                });

            }
        }
        return tableData;
    }


    /**
     * TODO 构建环境空气、室内空气，厂界无组织废气
     *
     * @param data
     * @param itemDatas
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/26 3:50 下午
     */
    private Map<String, Object> getAirDataMap(RecordRenderData data, List<RecordRenderItemData> itemDatas) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("consignorName", data.getConsignorName());

        if (itemDatas != null && !itemDatas.isEmpty()) {

            RecordRenderItemData lastData = itemDatas.get(0);
            Map<String, Object> lastDataDataMap = lastData.getDataMap();
//            RecordValItem collectDate = (RecordValItem) lastDataDataMap.get("collectDate");
            dataMap.put("collectDate", lastData.getCollectDate());
            RecordValItem sampBasisItem = (RecordValItem) lastDataDataMap.get("sampBasis");
            dataMap.put("sampBasis", BusiUtils.getSingleVal(sampBasisItem));
            RecordValItem weatherCondition = (RecordValItem) lastDataDataMap.get("weatherCondition");
            dataMap.put("weatherCondition", BusiUtils.getSingleVal(weatherCondition));

            RecordValItem mediumFlow = (RecordValItem) lastDataDataMap.get("mediumFlow");
            dataMap.put("mediumFlow", BusiUtils.getSingleVal(mediumFlow));

            RecordValItem mediumCalibrationDate = (RecordValItem) lastDataDataMap.get("mediumCalibrationDate");
            dataMap.put("mediumCalibrationDate", BusiUtils.getSingleVal(mediumCalibrationDate));


            RecordValItem smallFlow = (RecordValItem) lastDataDataMap.get("smallFlow");
            dataMap.put("smallFlow", BusiUtils.getSingleVal(smallFlow));


            RecordValItem calibrationDate = (RecordValItem) lastDataDataMap.get("calibrationDate");
            dataMap.put("calibrationDate", BusiUtils.getSingleVal(calibrationDate));
            RecordValItem sampEquipment = (RecordValItem) lastDataDataMap.get("sampEquipment");
            dataMap.put("sampEquipment", BusiUtils.getSingleVal(sampEquipment));

            List<Map<String, Object>> equpList = itemDatas.stream().map(
                    item -> {
                        Map<String, Object> resMap = new HashMap<>();
                        Map<String, Object> innerDataMap = item.getDataMap();
                        innerDataMap.forEach((key, val) -> {
                            if (val instanceof RecordValItem) {
                                String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
                                resMap.put(key, singleVal);
                            } else {
                                resMap.put(key, val);
                            }
                        });
                        return resMap;
                    }
            ).collect(Collectors.toList());
            dataMap.put("equpList", equpList);

            List<Map<String, Object>> dataList = itemDatas.stream().map(item -> {
                Map<String, Object> itemDataMap = new HashMap<>();
                Map<String, Object> sampleDataMap = item.getDataMap();
                sampleDataMap.forEach((key, val) -> {
                    if (val instanceof RecordValItem) {
                        String singleVal = BusiUtils.getSingleVal((RecordValItem) val);
                        itemDataMap.put(key, singleVal);
                    } else {
                        itemDataMap.put(key, val);
                    }
                });
                itemDataMap.putAll(itemDataMap);
                itemDataMap.put("checkItems", item.getFactorItems());
                itemDataMap.put("factorPoint", item.getFactorPoint());
                itemDataMap.put("sampItemId", item.getSampItemId());
                return itemDataMap;
            }).collect(Collectors.toList());
            dataMap.put("datas", dataList);
        }
        return dataMap;
    }


    /**
     * TODO 构建油烟
     *
     * @param data
     * @param itemDatas
     * @param renderDataList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/25 11:48 上午
     */
    private void getCookOilFlume(RecordRenderData data, List<RecordRenderItemData> itemDatas, List<Map<String, Object>> renderDataList) {
        if (itemDatas != null && !itemDatas.isEmpty()) {
            Map<String, List<RecordRenderItemData>> groupData = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getFactorPoint));
            groupData.forEach((factorPoint, dataList) -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("consignorName", data.getConsignorName());
                dataMap.put("checkEmps", data.getCheckEmps());
                dataMap.put("reviewerEmp", data.getReviewerEmp());
                RecordRenderItemData lastData = dataList.get(0);
//                RecordValItem collectDate = (RecordValItem) lastData.getDataMap().get("collectDate");
                dataMap.put("collectDate", lastData.getCollectDate());
                dataMap.put("factorPoint", factorPoint);
                RecordValItem mode = (RecordValItem) lastData.getDataMap().get("model");
                dataMap.put("model", BusiUtils.getSingleVal(mode));
                RecordValItem production = (RecordValItem) lastData.getDataMap().get("production");
                dataMap.put("production", BusiUtils.getSingleVal(production));
                RecordValItem useTime = (RecordValItem) lastData.getDataMap().get("useTime");
                dataMap.put("useTime", BusiUtils.getSingleVal(useTime));

                RecordValItem projectionArea = (RecordValItem) lastData.getDataMap().get("projectionArea");
                String projectionAreaVal = BusiUtils.getSingleVal(projectionArea);
                dataMap.put("projectionArea", projectionAreaVal);

                RecordValItem actualNumber = (RecordValItem) lastData.getDataMap().get("actualNumber");
                dataMap.put("actualNumber", BusiUtils.getSingleVal(actualNumber));

                RecordValItem workNumber = (RecordValItem) lastData.getDataMap().get("workNumber");
                dataMap.put("workNumber", BusiUtils.getSingleVal(workNumber));

                RecordValItem conversionNumber = (RecordValItem) lastData.getDataMap().get("conversionNumber");
                dataMap.put("conversionNumber", BusiUtils.getSingleVal(conversionNumber));

                RecordValItem tmodel = (RecordValItem) lastData.getDataMap().get("tmodel");
                dataMap.put("tmodel", BusiUtils.getSingleVal(tmodel));

                RecordValItem pProduction = (RecordValItem) lastData.getDataMap().get("pProduction");
                dataMap.put("pProduction", BusiUtils.getSingleVal(pProduction));

                RecordValItem completionUseTime = (RecordValItem) lastData.getDataMap().get("completionUseTime");
                dataMap.put("completionUseTime", BusiUtils.getSingleVal(completionUseTime));

                RecordValItem usageHours = (RecordValItem) lastData.getDataMap().get("usageHours");
                dataMap.put("usageHours", BusiUtils.getSingleVal(usageHours));

                RecordValItem operating = (RecordValItem) lastData.getDataMap().get("operating");
                dataMap.put("operating", BusiUtils.getSingleVal(operating));

                RecordValItem exhaustHeight = (RecordValItem) lastData.getDataMap().get("exhaustHeight");
                dataMap.put("exhaustHeight", BusiUtils.getSingleVal(exhaustHeight));

                RecordValItem checkType = (RecordValItem) lastData.getDataMap().get("checkType");
                String checkTypeVal = BusiUtils.getSingleVal(checkType);
                dataMap.put("checkType", checkTypeVal != null ? checkTypeVal : null);

                RecordValItem area = (RecordValItem) lastData.getDataMap().get("area");
                dataMap.put("area", BusiUtils.getSingleVal(area));

                RecordValItem kpa = (RecordValItem) lastData.getDataMap().get("kpa");
                dataMap.put("kpa", BusiUtils.getSingleVal(kpa));

                RecordValItem temperature = (RecordValItem) lastData.getDataMap().get("temperature");
                dataMap.put("temperature", BusiUtils.getSingleVal(temperature));

                for (int i = 0; i < dataList.size(); i++) {
                    RecordRenderItemData itemData = dataList.get(i);
                    if (i == 0) {
                        Map<String, Object> dataMap1 = itemData.getDataMap();
                        RecordValItem sampEquipmentItem = (RecordValItem) dataMap1.get("sampEquipment");
                        dataMap.put("sampEquipment1", BusiUtils.getSingleVal(sampEquipmentItem));
                    } else if (i == 1) {
                        Map<String, Object> dataMap1 = itemData.getDataMap();
                        RecordValItem sampEquipmentItem = (RecordValItem) dataMap1.get("sampEquipment");
                        dataMap.put("sampEquipment2", BusiUtils.getSingleVal(sampEquipmentItem));
                    }

                    dataMap.put("sampItemId" + i, itemData.getSampItemId());
                    RecordValItem airVolume = (RecordValItem) itemData.getDataMap().get("airVolume");
                    dataMap.put("airVolume" + i, BusiUtils.getSingleVal(airVolume));
                    RecordValItem volume = (RecordValItem) itemData.getDataMap().get("volume");
                    dataMap.put("volume" + i, BusiUtils.getSingleVal(volume));
                }
                renderDataList.add(dataMap);
            });
        }
    }


    /**
     * TODO 构建有组织废气
     *
     * @param data
     * @param itemDatas
     * @param renderDataList
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/22 6:08 下午
     */
    private void getOrganWaste(RecordRenderData data, String secdClassId, List<RecordRenderItemData> itemDatas, List<Map<String, Object>> renderDataList) {
        if (itemDatas != null && !itemDatas.isEmpty()) {
            Map<String, List<RecordRenderItemData>> groupData = itemDatas.stream().collect(Collectors.groupingBy(RecordRenderItemData::getFactorPoint));
            groupData.forEach((factorPoint, dataList) -> {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("consignorName", data.getConsignorName());
                RecordRenderItemData lastData = dataList.get(0);
//                RecordValItem collectDate = (RecordValItem) lastData.getDataMap().get("collectDate");
                dataMap.put("collectDate", lastData.getCollectDate());
                dataMap.put("factorPoint", factorPoint);
                dataMap.put("checkEmps", data.getCheckEmps());
                dataMap.put("reviewerEmp", data.getReviewerEmp());

                RecordValItem calibrationDate = (RecordValItem) lastData.getDataMap().get("calibrationDate");
                dataMap.put("calibrationDate", BusiUtils.getSingleVal(calibrationDate));

                RecordValItem smallFlow = (RecordValItem) lastData.getDataMap().get("smallFlow");
                dataMap.put("smallFlowLabel", BusiUtils.getSingleVal(smallFlow));

                RecordValItem exhaustHeight = (RecordValItem) lastData.getDataMap().get("exhaustHeight");
                dataMap.put("exhaustHeight", BusiUtils.getSingleVal(exhaustHeight));

                RecordValItem sectionalArea = (RecordValItem) lastData.getDataMap().get("sectionalArea");
                dataMap.put("sectionalArea", BusiUtils.getSingleVal(sectionalArea));


                RecordValItem treatment = (RecordValItem) lastData.getDataMap().get("treatment");
                dataMap.put("treatment", BusiUtils.getSingleVal(treatment));

                RecordValItem amTemperature = (RecordValItem) lastData.getDataMap().get("temperature");
                dataMap.put("temperature", BusiUtils.getSingleVal(amTemperature));

                RecordValItem smokeCode;
                if (secdClassId.equals("002002")) {
                    smokeCode = (RecordValItem) lastData.getDataMap().get("checkEquipment");
                } else {
                    smokeCode = (RecordValItem) lastData.getDataMap().get("smokeCode");
                }
                dataMap.put("smokeCode", BusiUtils.getSingleVal(smokeCode));

                RecordValItem kpa = (RecordValItem) lastData.getDataMap().get("kpa");
                dataMap.put("kpa", BusiUtils.getSingleVal(kpa));

                RecordValItem oxygen = (RecordValItem) lastData.getDataMap().get("oxygen");
                dataMap.put("oxygen", BusiUtils.getSingleVal(oxygen));

                RecordValItem conversionOther = (RecordValItem) lastData.getDataMap().get("conversionOther");
                dataMap.put("conversionOther", BusiUtils.getSingleVal(conversionOther));

                RecordValItem fuel = (RecordValItem) lastData.getDataMap().get("fuel");
                dataMap.put("fuel", BusiUtils.getSingleVal(fuel));

                RecordValItem emissionMode = (RecordValItem) lastData.getDataMap().get("emissionMode");
                String emissionModeVal = BusiUtils.getSingleVal(emissionMode);
                if (emissionModeVal != null) {
                    dataMap.put("emissionMode", emissionModeVal);
                }
                List<Map<String, Object>> dataMapList = new ArrayList<>();
                List<Pollutant> pollists = new ArrayList<>();
                dataList.stream().forEach(item -> {
                    Map<String, Object> resDataMap = new HashMap<>();
                    Map<String, Object> dataMap1 = item.getDataMap();
                    if (dataMap1 != null && !dataMap1.isEmpty()) {
                        dataMap1.forEach((key, val) -> {
                            if (val instanceof RecordValItem) {
                                RecordValItem itemVal = (RecordValItem) val;
                                String singleVal = BusiUtils.getSingleVal(itemVal);
                                resDataMap.put(key, singleVal);
                            } else {
                                resDataMap.put(key, val);
                            }

                        });
//                        resDataMap.putAll(dataMap1);
                    }
                    Map<String, SampleFactorDataParam> factorMap = item.getFactorMap();
                    if (factorMap != null && !factorMap.isEmpty()) {//二氧化硫//一氧化碳//二氧化碳
                        resDataMap.putAll(factorMap);
                        factorMap.forEach((key, val) -> {
                            String factorName = val.getFactorName();
                            if (factorName.equals("二氧化硫")) {
                                key = "10000116-002";
                            } else if (factorName.equals("二氧化碳")) {
                                key = "10000192-001";
                            } else if (factorName.equals("一氧化碳")) {
                                key = "10000173-001";
                            } else if (factorName.equals("氮氧化物")) {
                                key = "10000118-002";
                            }
                            String factorData = val.getFactorData();
                            resDataMap.put(key, BusiUtils.getSingleValByJson(factorData));
                        });
                    }

                    dataMapList.add(resDataMap);
                    List<Pollutant> pollist = item.getPollist();
                    if (pollist != null && !pollist.isEmpty()) {
                        pollists.addAll(pollist);
                    }

                });
                dataMap.put("datas", dataMapList);
                dataMap.put("pollist", pollists);

                renderDataList.add(dataMap);

            });
        }
    }

    /**
     * TODO 获取型号和编号
     *
     * @param factorDataParam
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/1/7 4:11 下午
     */
    private ImmutablePair<String, String> getSelectEquipOrCode(SampleFactorDataParam factorDataParam) {
        String selectEquip = null;
        String selectEquipCode = null;
        if (factorDataParam != null) {
            String checkEquipment = factorDataParam.getCheckEquipment();
            if (checkEquipment != null) {
                String[] split = checkEquipment.split("\\^_\\^");
                selectEquip = split[2];
                selectEquipCode = split[1];
            }

        }
        return ImmutablePair.of(selectEquip, selectEquipCode);
    }

}