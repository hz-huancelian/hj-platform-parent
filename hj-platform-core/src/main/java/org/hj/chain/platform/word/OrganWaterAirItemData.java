package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 有组织废气
 * @Iteration : 1.0
 * @Date : 2021/6/14  4:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrganWaterAirItemData implements Serializable {

    private static final long serialVersionUID = 7177811718379568258L;

    //数据信息
    private LinkedHashMap<String, List<RowRenderData>> rowDataMap;
}