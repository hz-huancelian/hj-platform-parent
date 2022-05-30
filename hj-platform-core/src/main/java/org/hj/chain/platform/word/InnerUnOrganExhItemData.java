package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 车间内无组织废气
 * @Iteration : 1.0
 * @Date : 2021/6/14  4:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InnerUnOrganExhItemData implements Serializable {

    private static final long serialVersionUID = 7177811718379568258L;

    //图片地址
    private String templatePath;

    //数据信息
    private LinkedHashMap<String, Map<String, Map<String, List<RowRenderData>>>> rowDataMap;
}