package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  10:17 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleData implements Serializable {
    private static final long serialVersionUID = -1754208135897005387L;


    //备注
    private String remark;
    //测量仪器及编号
    private String fieldTestingEquipment;

    //校准仪器及编号
    private String flowEquipment;

    //因子map（程序内组装序号）
    private List<RowRenderData> factors;
}