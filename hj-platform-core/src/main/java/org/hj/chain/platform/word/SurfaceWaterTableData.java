package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 地表水策略
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:17 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SurfaceWaterTableData implements Serializable {
    private static final long serialVersionUID = -1754208135897005387L;

    //机构名称
    private String organName;

    //控制编号
    private String contContollerId;

    //项目名称
    private String consignorName;

    //采样日期
    private String collectDate;

    //天气
    private String weatherCondition;

    //保温箱是否完整
    private String heatBox;

    //保温箱内稳定
    private String temperature;

    //样品是否完整
    private String sampleComplete;

    //采样依据
    private String sampBasis;

    //采样单前缀
    private String samplePrefix;

    //检测项目
    private String checkItems;

    //理论值
    private String theoreticalValue;

    //测定值
    private String groundConditions;    //测定值
    private String groundConditions2;    //测定值
    private String groundConditions3;    //测定值
    private String groundConditions4;    //测定值
    private String groundConditions5;    //测定值

    //设备型号
    private String selectEquip;
    private String selectEquip2;
    private String selectEquip3;
    private String selectEquip4;
    private String selectEquip5;

    //设备编号
    private String selectEquipCode;
    private String selectEquipCode2;
    private String selectEquipCode3;
    private String selectEquipCode4;
    private String selectEquipCode5;

    //设备名称
    private String selectEquipLabel;


    //检测人：最多保留两个
    private String checkEmps;

    //审核人
    private String reviewerEmp;

    @Name("sample_table")
    private SampleData sampleData;
}