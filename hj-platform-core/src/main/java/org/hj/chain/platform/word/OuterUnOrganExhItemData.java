package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 无组织废弃
 * @Iteration : 1.0
 * @Date : 2021/6/14  4:41 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OuterUnOrganExhItemData {

    //图片地址
    private String templatePath;

    //检测点位
    private List<String> factorPoints;

    //因子map（程序内组装序号）
    private List<List<RowRenderData>> rows;
}