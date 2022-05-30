package org.hj.chain.platform.vo.report;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.word.NoiseType2ItemTemplateData;

import java.util.List;

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
public class NoiseType2Data {
    //检测点位
    private String factorPoint;

    private String collectDate;

    //中型车辆
    private String mediumCar;

    //大型车辆
    private String bigCar;

    //数据集合
    private List<RowRenderData> rrds;


}