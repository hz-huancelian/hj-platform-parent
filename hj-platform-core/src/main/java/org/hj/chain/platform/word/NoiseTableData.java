package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 噪声
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:17 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NoiseTableData implements Serializable {
    private static final long serialVersionUID = -1754208135897005387L;

    //机构名称
    private String organName;

    //二级类别名称
    private String secdClassName;

    //控制编号
    private String contContollerId;

    //项目名称
    private String consignorName;

    //采样日期
    private String collectDate;

    //白天天气
    private String dayWeather;
    //晚上天气
    private String nightWeather;
    //白天风速
    private String dayWeedSpeed;
    //晚上风速
    private String nightWeedSpeed;

    //白天校准前
    private String dayCalibBefore;
    //白天校准后
    private String dayCalibAfter;


    //夜晚校准前
    private String nightCalibBefore;
    //夜晚校准后
    private String nightCalibAfter;

    //测量仪器及编号
    private String fieldTestingEquipment;

    //校准仪器及编号
    private String flowEquipment;

    //振动类别
    private String vibrationCategory;


    //检测人：最多保留两个
    private String checkEmps;

    //审核人
    private String reviewerEmp;


    @Name("sample_table")
    private SampleData sampleData;
}