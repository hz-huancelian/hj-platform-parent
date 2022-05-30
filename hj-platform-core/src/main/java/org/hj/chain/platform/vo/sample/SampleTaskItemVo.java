package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskItemVo implements Serializable {
    private static final long serialVersionUID = -6845353614442144308L;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //受检单位
    private String inspectionName;
    //样品类别
    private String secdClassName;
    //检查因子
    private String factorName;
    //点位信息
    private String factorPoint;
    //样品状态
    private String smapStatus;
    //样品类别 0-自检；1-外包
    private String fbType;
    //天/次
    private String dayAndCount;
    //样品天数
    private int day;
    //样品频次
    private int frequency;
}
