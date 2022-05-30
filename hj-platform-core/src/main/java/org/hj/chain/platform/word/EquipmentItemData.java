package org.hj.chain.platform.word;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2022/5/17  7:25 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/05/17    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipmentItemData implements Serializable {
    private static final long serialVersionUID = 794104495912794411L;

    //模板地址
    private String templatePath;

    //数据
    private Map<String, List<ReportEquipVo>> dataMap;
}