package org.hj.chain.platform.vo.samplebak;

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
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //出库申请ID
    private Long drawApplyId;
    //样品编号
    private String sampItemId;
    //任务单号
    private String dispatchInfoId;
    //项目名称
    private String projectName;
    //检测位置（点位）
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //样品状态 0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampStatus;
    //采样员
    private String collectUser;
    //是否需要入库
    private String storeFlag;
    //库存位置
    private String storeLocation;
    //入库时间
    private LocalDateTime storeTime;
    /**
     * 受检单位
     */
    private String inspectionName;
    //样品二级类别ID
    private String secdClassId;

    //复核人ID
    private String reviewUserId;
    //复核人
    private String reviewUser;
}
