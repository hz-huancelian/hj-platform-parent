package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 设备表单数据
 * @Iteration : 1.0
 * @Date : 2022/5/17  7:25 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/17    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipmentTableData implements Serializable {
    private static final long serialVersionUID = 794104495912794411L;

    @Name("equip_table")
    private EquipmentItemData itemData;
}