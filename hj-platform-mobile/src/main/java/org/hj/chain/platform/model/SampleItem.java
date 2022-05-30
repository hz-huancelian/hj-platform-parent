package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 采集因子取样表
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_item")
public class SampleItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 采样任务ID
     */
    private Long sampleTaskId;

    /**
     * 样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
     */
    private String sampleStatus;

    /**
     * 是否需要入库（0：否 1：是）
     */
    private String storeFlag;

    /**
     * 机构ID
     */
    private String organId;

    /**
     * 样品采集开始时间
     */
    private String sampleStartDate;

    /**
     * 样品采集结束时间
     */
    private String sampleEndDate;

    /**
     * 采样组长
     */
    private String sampleUserId;

    /**
     * 厂方签字图片地址
     */
    private String signImageUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    //驳回原因
    private String auditReason;

    //检测位置（点位）
    private String factorPoint;

    //样品二级类别名称
    private String secdClassName;

    //可领次数
    private Integer avalDrawCount;

    //领样次数
    private Integer drawCount;

    //审核时间
    private LocalDateTime auditTime;

    //天数
    private Integer day;

    //频次数
    private Integer frequency;

    //样品类别 0-自检；1-外包
    private String fbFlag;

    //样品二级类别ID
    private String secdClassId;

    //样品按天分组key
    private String groupKey;

    //因子点位归类标识
    private String factorGroupKey;
}
