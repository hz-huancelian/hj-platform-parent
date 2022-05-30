package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/25  4:27 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportItemData {

    //表头所在行
    private int headRow;

    //表头所在开始插入列
    private int headStartColumn;

    //头数据
    private RowRenderData headData;

    //表尾填充行
    private int tailRow;

    //表头所在开始插入列
    private int tailStartColumn;

    //尾数据
    private RowRenderData tailData;

    //起始行
    private int startRow;

    //起始列
    private int startColumn;

    //总列数
    private int tolalColumn;

    //插入总行数（除去表头填充行）
    private int totalRow;

    //图片地址
    private String templatePath;

    //因子map（程序内组装序号）
    private List<RowRenderData> rows;
}