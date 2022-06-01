package org.hj.chain.platform.factor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : zzl
 * @description 因子--雨水表单实体
 * @Date : 2022.5.31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factor_class_rain")
public class FactorRain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //采样任务id
    private Long sampleTaskId;

    //样品id
    private Long sampleItemId;

    //因子二级类别id
    private String secdClassId;

    //样品二级类别名称
    private String secdClassName;

    //受检单位
    private String inspectionName;

    //项目名称
    private String projectName;

    //采样日期
    private String collectDate;

    //二级类别--属性结果值
    private String sampleDataValue;

    //样品编号
    private String sampleNo;

    //采样点位
    private String factorPoint;

    //采样时间
    private String collectTime;

    //分析项目
    private String checkRes;

    //样品性状
    private String sampleProperties;

    //录样备注
    private String collectRemark;

    //特别说明--数据
    private String specialNote;

    //现场检测设备列表
    private String checkEquipment;

    //标准缓冲液I定位值
    private String positioningOne;

    //标准缓冲液II定位值
    private String positioningTwo;

    //标准缓冲液II理论值
    private String positioningThree;

    //理论值
    private String theoreticalVal;

    //测定值
    private String groundConditions;

    //采样人
    private String collectUserId;

    //复核人
    private String reviewUserId;

    //审批人
    private String sampleUserId;

    //因子数据结果集
    private String factorDataValue;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime updateTime;

    //是否删除：0-未删除；1-删除
    private int isDeleted;

}
