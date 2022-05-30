package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 样品数据参数
 * @Iteration : 1.0
 * @Date : 2022/4/7  5:52 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleDataDetailParam extends SampleDataParam implements Serializable {
    private static final long serialVersionUID = -6795991452413432796L;

    //二级类别数据
    private String sampleData;

    //污染物信息
    private String pollutantInfo;

    //检测点位
    private String factorPoint;

    //采样日期
    private String collectDate;

    //采样备注
    private String collectRemark;

    //分组key
    private String groupKey;

    //采样人
    private String collectUserId;

    //审核人
    private String reviewUserId;
}