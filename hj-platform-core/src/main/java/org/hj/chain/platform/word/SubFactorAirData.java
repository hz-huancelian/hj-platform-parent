package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 环境空气、厂界无组织废气
 * @Iteration : 1.0
 * @Date : 2021/7/3  12:01 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/03    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SubFactorAirData {

    //采样日期
    private String collectDate;

    //二级分类
    private String secdClassName;

    //因子名称
    private String factorName;

    @Name("rep_table")
    private SubFactorAirItemData itemData;

}