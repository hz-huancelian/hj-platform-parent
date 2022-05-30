package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 固废
 * @Iteration : 1.0
 * @Date : 2021/6/14  4:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SolidWasteItemData {

    //图片地址
    private String templatePath;

    //因子map（程序内组装序号）日期为key
    private LinkedHashMap<String, List<RowRenderData>> rowMap = new LinkedHashMap<>();
}