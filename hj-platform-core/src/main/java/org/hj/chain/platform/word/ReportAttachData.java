package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/26  12:02 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/26    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportAttachData {

    //二级分类
    private String secdClass;

    @Name("report_table")
    private ReportItemData itemData;
}