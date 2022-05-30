package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/3  12:01 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/03    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SubFactorAirItemData {

    //采集点位
    private List<String> points;

    //头记录所在位置
    private int headRowPos;

    //数据开始位置
    private int dataRowStartPos;

    //最大采集点位次个数
    private int maxPointCount;

    //单元格数
    private int totalCellCount;

    //图片地址
    private String templatePath;

    //组装数据
    private List<RowRenderData> dataList;

}