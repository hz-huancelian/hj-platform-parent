package org.hj.chain.platform.word;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 道路交通噪声
 * @Iteration : 1.0
 * @Date : 2021/11/27  3:58 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/11/27    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NoiseType2ItemTemplateData {

//    private RowRenderData rowRenderData;
    //数据集合
    private List<RowRenderData> rrdList;


}