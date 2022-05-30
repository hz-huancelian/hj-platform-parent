package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 道路交通噪声
 * @Iteration : 1.0
 * @Date : 2021/11/27  3:58 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/11/27    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NoiseType2TemplateData {

    //检测点位
    private String factorPoint;


    private String upCollectDate;

    //中型车辆
    private String upMediumCar;

    //大型车辆
    private String upBigCar;

    private String downCollectDate;

    //中型车辆
    private String downMediumCar;

    //大型车辆
    private String downBigCar;

    @Name("up_rep_table")
    private NoiseType2ItemTemplateData upItemData;


    @Name("down_rep_table")
    private NoiseType2ItemTemplateData downItemData;


}