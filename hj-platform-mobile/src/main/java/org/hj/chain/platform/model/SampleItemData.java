package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@TableName("t_sample_item_data")
public class SampleItemData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 样品ID
     */
    private Long sampleItemId;

    /**
     * 录样图片1id（资源文件信息表id）
     */
    private String collectIamge1Id;

    /**
     * 录样图片2id（资源文件信息表id）
     */
    private String collectIamge2Id;

    /**
     * 录样图片3id（资源文件信息表id）
     */
    private String collectIamge3Id;

    private String collectIamge4Id;

    /**
     * 样品录样位置(详细地址)
     */
    private String collectLocation;
    /**
     * 样品定位位置(经纬度)
     */
    private String collectAddress;

    /**
     * 采样数据-二级类别 json存储
     */
    private String sampleData;

    //采样时间
    private String collectTime;
    //采样日期
    private LocalDate collectDate;
    //录样时间
    private LocalDateTime createTime;
    //采样员
    private String collectUserId;
    //录样备注
    private String collectRemark;
    //复核人
    private String reviewUserId;
    //特别说明值
    private String specialNote;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;
    //二级类别属性结果值
    private String sampleDataValue;
    //污染物信息
    private String pollutantInfo;
}
