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


    //设备一级类别
    Long EQUIPMENT_FIRST_TYPE = 1004L;

    //设备一级类别--实验室设备
    Long EQUIPMENT_FIRST_TYPE_LABORATORY = 100403L;

    //设备一级类别--现场采样设备
    Long EQUIPMENT_FIRST_TYPE_SAMPLE = 100401L;

    //设备一级类别--校准设备
    Long EQUIPMENT_FIRST_TYPE_CALIBRATION = 100404L;

    //现场检测设备
    Long EQUIPMENT_FIRST_TYPE_CHECK = 100402L;

    //实验室设备
    Long EQUIPMENT_LABORATORY = 1005L;

    //设备二级类型
    Long EQUIPMENT_SECOND_TYPE = 1006L;

    List<String> SPECIAL_NOTE_CLASS = Arrays.asList("001001","001002","001003","001004","003001","005001");

}