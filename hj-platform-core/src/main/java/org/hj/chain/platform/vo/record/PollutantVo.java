package org.hj.chain.platform.vo.record;

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
 * @Date : 2021/8/20  2:52 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/20    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PollutantVo implements Serializable {
    private static final long serialVersionUID = 6701118190789849375L;

    //污染物集合
    private List<Pollutant> pollist;
}