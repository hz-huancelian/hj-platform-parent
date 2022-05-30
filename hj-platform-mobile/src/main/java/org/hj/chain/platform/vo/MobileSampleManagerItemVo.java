package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class MobileSampleManagerItemVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //检测ID
    private Long checkFactorId;
    //样品编号
    private String sampItemId;
    //检测位置
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //监测因子
    private String factorName;
    //采样人
    private String collectUser;
    //样品录样时间
    private String collectTime;
    //样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-入库中；7-入库完成；8-已出库
    private String sampleStatus;
    //不通过原因
    private String auditReason;
    //天/次
    private String dayAndCount;
    //任务编号
    private String taskNumber;
}
