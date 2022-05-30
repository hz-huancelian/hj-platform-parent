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
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/11/27  4:01 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/11/27    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NoiseType01ItemTemplateData {

    //图片地址
    private String templatePath;

    //因子map（程序内组装序号）日期为key
    private LinkedHashMap<String, List<RowRenderData>> rowMap = new LinkedHashMap<>();
}