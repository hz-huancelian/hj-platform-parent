package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class SampleTableData implements Serializable {
    private static final long serialVersionUID = -1754208135897005387L;

    //项目名称
    private String projectName;

    //采样日期
    private String sampleDate;

    //采样依据
    private String sampleBasis;

    //采样单前缀
    private String samplePrefix;

    //检测项目
    private String checkItems;

    @Name("sample_table")
    private SampleData sampleData;
}