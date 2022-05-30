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
 * @Date : 2022/3/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_list")
public class SampleList implements Serializable {
    private static final long serialVersionUID = 1L;
    //合样列表ID
    @TableId(type = IdType.AUTO)
    private Long id;
    //采样任务ID
    private Long sampleTaskId;
    //调度任务单号
    private String jobId;
    //二级类别名称
    private String secdClassName;
    //二级类别ID
    private String secdClassId;
    //任务计划因子ID （多个逗号拼接）
    private String jobPlanFactorId;
    //因子名称（多个逗号拼接）
    private String factorName;
    //天数
    private Integer dayCount;
    //频次
    private Integer frequency;
    //采样组长ID
    private String sampleUserId;
    //采样组长名称
    private String sampleUser;
    //分包标志 0-自检；1-分包
    private String fbFlag;
    //检测位置
    private String factorPoint;
    //因子点位归类标识
    private String factorGroupKey;
}
