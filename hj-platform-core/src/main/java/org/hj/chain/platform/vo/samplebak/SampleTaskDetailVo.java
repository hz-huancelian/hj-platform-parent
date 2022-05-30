package org.hj.chain.platform.vo.samplebak;

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
 * @Iteration : 1.0
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskDetailVo implements Serializable {
    private static final long serialVersionUID = -6845353614442144308L;
    //样品编号
    private String sampItemId;
    //受检单位
    private String inspectionName;
    //采样点位
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //采样时间
    private String collectTime;
    //样品状态 0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String smapStatus;
    //采样人
    private String collectUser;
    //入库时间
    private LocalDateTime storeTime;
    //采样日期
    private LocalDate collectDate;
    //驳回原因
    private String auditReason;
}
