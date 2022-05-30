package org.hj.chain.platform.sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_item_factor_data")
public class SampleItemFactorData implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //样品ID
    private Long sampleItemId;
    //调度任务计划因子ID
    private Long jobPlanFactorId;
    //因子数据
    private String factorData;
    //现场检测设备列表
    private String checkEquipment;
    //校准设备列表
    private String calibrationEquipment;
    //因子数据结果集
    private String factorDataValue;

    //标准缓冲液I定位值
    private String positioningOne;
    //标准缓冲液II定位值
    private String positioningTwo;
    //标准缓冲液II理论值
    private String positioningThree;
    // 理论值
    private String theoreticalVal;
    // 测定值
    private String groundConditions;

    //因子计算结果集
    private String measuredFormVal;
}
