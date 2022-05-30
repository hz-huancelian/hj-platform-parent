package org.hj.chain.platform.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-08
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MobileSampleItemDetailVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //项目名称
    private String projectName;
    //委托单位
    private String consignorName;
    //受检单位
    private String inspectionName;
    //受检单位联系人
    private String inspectionLinker;
    //受检单位联系方式
    private String inspectionLinkerPhone;
    //任务地址
    private String taskAddress;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
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
    //采样组长userId
    private String collectLeaderId;
    //采样组长
    private String collectLeader;
    //采样人userId
    private String collectUserId;
    //采样人
    private String collectUser;
    //审核人userId
    private String auditUserId;
    //审核人
    private String auditUser;
    //采样时间
    private String collectTime;
    //采样日期
    private LocalDate collectDate;
    //录样位置
    private String collectLocation;

    /**
     * 采样数据-二级类别 json存储
     */
    private String sampleData;

    /**
     * 采样数据-因子列表数据
     */
    List<SampleFactorDataVo> factorDataVos;


    //录样图片地址列表
    private List<String> imageList;
    //样品状态 样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampleStatus;
    //调度任务备注
    private String jobRemark;
    //不通过原因
    private String auditReason;
    //二级类别ID
    private String secdClassId;
    //复核人ID
    private String reviewUserId;
    //复核人
    private String reviewUser;
    //采样备注
    private String collectRemark;
    /**
     * 采样数据-特别说明值
     */
    private String specialNote;
    //污染物信息
    private String pollutantInfo;
    //频次数
    private Integer frequency;
    //样品按天分组key
    private String groupKey;
}
