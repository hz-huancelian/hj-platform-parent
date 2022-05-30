package org.hj.chain.platform.vo.check;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckFactorInfoVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //检样列表ID
    private Long checkFactorId;
    //项目名称
    private String projectName;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //检测位置
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //监测因子
    private String factorName;
    //因子标记 0-因子；1-同系物套餐
    private String isFactor;
    //样品天数
    private int day;
    //样品频次
    private int frequency;
    //样品采集时间
    private String collectTime;
    //样品采集日期
    private String collectDate;
    //样品入库时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime storeTime;
    //天/次
    private String dayAndCount;
    //检测标准ID
    private String checkStandardId;
    //标准号
    private String standardNo;
    //检测标准
    private String standardName;
    //检测结果(json对象){"v1":"", "v2":""} 注：v1：实数；v2：指数
    private String checkRes;
    //同系物因子检测结果
    private List<CheckFactorSubsetVo> factorSubsetVos;
    //检测状态0-待领样；1-领样申请；2-待检测；3-已录入；4-待审核；5-审核通过；6-审核失败；
    private String checkStatus;
    //检验员ID
    private String assignUserId;
    //检验员
    private String assignUser;
    //因子单位
    private String unitName;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;
    //样品类型 0-自检；1-外包
    private String fbFlag;
    //报价单因子备注信息
    private String factorRemark;
    //驳回原因
    private String auditReason;
    //检测设备
    private String checkEquipment;
    //检测结果备注
    private String remark;

}
