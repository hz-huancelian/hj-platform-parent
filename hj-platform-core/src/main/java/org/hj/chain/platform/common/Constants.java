package org.hj.chain.platform.common;

import org.hj.chain.platform.constants.PlatformConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 常量
 * @Iteration : 1.0
 * @Date : 2021/5/7  9:16 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
public interface Constants {


    //平台机构
    String PLATFORM_ORGAN_ID = "0100";

    //职位ID
    Long POSITION_ID = 1003L;


    //需要获取上级用户的方法
    Map<String, Long> DEPT_PATH_MAP = new HashMap<String, Long>() {{
        put("/offer/saveOffer", PlatformConstants.BUSI_DEPT_ID);
    }};


    Map<String, String> SECD_CLASS_HANDOVER_MAP = new HashMap<String, String>() {
        {
            put("001001", "handover_water_template.docx");
            put("001002", "handover_water_template.docx");
            put("001003", "handover_water_template.docx");
            put("001004", "handover_water_template.docx");
            put("002001", "handover_unorg_template.docx");
            put("002005", "handover_unorg_template.docx");
            put("002003", "handover_unorg_template.docx");
            put("002004", "handover_unorg_template.docx");
            put("002002", "handover_org_template.docx");
            put("0020021", "handover_org_template.docx");
            put("0020022", "handover_org_template.docx");
            put("005001", "handover_soil_template.docx");
            put("003001", "handover_soil_template.docx");
            put("003002", "handover_soil_template.docx");
            put("006001", "handover_soil_template.docx");
        }
    };

    //设备一级类别
    Long EQUIPMENT_FIRST_TYPE = 1004L;

    //设备一级类别--实验室设备
    Long EQUIPMENT_FIRST_TYPE_LABORATORY = 100403L;

    //实验室设备
    Long EQUIPMENT_LABORATORY = 1005L;

    //设备二级类型
    Long EQUIPMENT_SECOND_TYPE = 1006L;


    public enum FileResource{
        FILE_RECORD_GF("FRS_GF", "现场记录单_固废"),
        FILE_RECORD_TR("FRS_TR", "现场记录单_土壤"),
        FILE_RECORD_KQ("FRS_KQ", "现场记录单_环境空气、无组织废气、室内空气"),
        FILE_RECORD_YY("FRS_YY", "现场记录单_饮食业油烟"),
        FILE_RECORD_FQ("FRS_FQ", "现场记录单_有组织废气"),
        FILE_RECORD_DBS("FRS_DBS", "现场记录单_地表水"),
        FILE_RECORD_DXS("FRS_DXS", "现场记录单_地下水"),
        FILE_RECORD_FYS("FRS_FYS", "现场记录单_废水、雨水"),
        FILE_RECORD_JTZ("FRS_JTZ", "现场记录单_道路交通噪声"),
        FILE_RECORD_ZS("FRS_ZS", "现场记录单_工业企业、区域环境、社会生活、城市振动"),
        FILE_RECORD_DN("FRS_DN", "现场记录单_底泥"),
        SAMPLE_HANDOVER_GFTR("SHL_GFTR", "样品交接单_固废和土壤"),
        SAMPLE_HANDOVER_S("SHL_S", "样品交接单_水（地表水、地下水、雨水、废水）"),
        SAMPLE_HANDOVER_WFQ("SHL_WFQ", "样品交接单_无组织废气"),
        SAMPLE_HANDOVER_YFQ("SHL_YFQ", "样品交接单_有组织废气");

        private String key;
        private String value;
        FileResource(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }

    }
    List<String> SPECIAL_NOTE_CLASS = Arrays.asList("001001","001002","001003","001004","003001","003002","005001");
}