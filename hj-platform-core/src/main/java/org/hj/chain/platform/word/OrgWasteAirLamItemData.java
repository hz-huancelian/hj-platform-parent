package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/2  10:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/02    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrgWasteAirLamItemData {

    private RowRenderData data;
}