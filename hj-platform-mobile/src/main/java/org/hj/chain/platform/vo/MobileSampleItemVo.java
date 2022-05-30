package org.hj.chain.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-05
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleItemVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //出库申请ID
    private Long drawApplyId;
    //入库申请ID
    private Long storeApplyId;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //检测位置
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //监测因子
    private String factorName;
    //采样人
    private String collectUser;
    //样品采样时间
    private String collectTime;
    //样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampleStatus;
    //不通过原因
    private String auditReason;
    //天/次
    private String dayAndCount;
    //任务编号
    private String jobId;
    //样品天数
    private int day;
    //样品频次
    private int frequency;
    //样品采样日期
    private String collectDate;
    /**
     * 厂方签字图片地址
     */
    private String signImageUrl;
}
