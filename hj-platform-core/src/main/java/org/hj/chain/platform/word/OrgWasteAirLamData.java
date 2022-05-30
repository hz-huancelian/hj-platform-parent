package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 有组织废气油烟
 * @Iteration : 1.0
 * @Date : 2021/7/2  10:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/02    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrgWasteAirLamData {
    //点位
    private String factorPoint;

    //采集日期
    private String collectDate;


    //烟道截面积
    private String sectionalArea;
    //排气筒高度
    private String exhaustHeight;

    //实测风量
    private String airVolume0;
    private String airVolume1;
    private String airVolume2;
    private String airVolume3;
    private String airVolume4;

    @Name("rep_table")
    private OrgWasteAirLamItemData itemData;
}