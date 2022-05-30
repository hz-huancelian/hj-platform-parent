package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleItemVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //样品状态 样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库；7-部分出库；8-已出库；9-入库中；10-待复核
    private String sampleStatus;
    //检测位置
    private String factorPoint;
    //二级类别ID
    private String secdClassId;
    //二级类别名称
    private String secdClassName;
    //监测因子
    private String factorName;
    //采样组长userId
    private String collectLeaderId;
    //采样组长
    private String collectLeader;
    //驳回原因
    private String auditReason;
    //频次数
    private Integer frequency;
    /**
     * 样品 项目任务信息
     */
    private SampleJobVo sampleJobVo;

    /**
     * 样品采样数据
     */
    private SampleDataVo sampleDataVo;

    /**
     * 样品因子列表数据
     */
    List<SampleFactorDataVo> factorDataVos;
}
