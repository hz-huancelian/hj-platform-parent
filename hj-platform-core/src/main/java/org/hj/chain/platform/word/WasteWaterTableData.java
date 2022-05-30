package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 废水、雨水
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:17 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class WasteWaterTableData implements Serializable {
    private static final long serialVersionUID = -1754208135897005387L;

    private String secdClassId;

    //机构名称
    private String organName;

    //控制编号
    private String contContollerId;

    //项目名称
    private String consignorName;

    //采样日期
    private String collectDate;

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

    //设备型号
    private String selectEquip;
    //编号
    private String selectEquipCode;

    //生产工艺与工况
    private String processConditions;
    //废水处理设施运行情况
    private String wastewater;


    //标准缓存液体I定位值
    private String positioningOne;

    //标准缓存液体II理论值
    private String positioningTwo;

    //标准缓存液体II定位值
    private String positioningThree;

    //设备名称
    private String selectEquipLabel;


    //检测人：最多保留两个
    private String checkEmps;

    //审核人
    private String reviewerEmp;

    @Name("sample_table")
    private SampleData sampleData;
}