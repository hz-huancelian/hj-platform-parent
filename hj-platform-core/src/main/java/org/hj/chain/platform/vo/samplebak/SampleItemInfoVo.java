package org.hj.chain.platform.vo.samplebak;

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
 * @Date : 2021-05-11
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleItemInfoVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //样品编号
    private String sampItemId;
    //任务单号
    private String dispatchInfoId;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //委托单位
    private String consignorName;
    //检测位置（点位）
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库;7-部分出库；8-已出库；9-入库中
     */
    private String sampleStatus;
    //入库时间
    private LocalDateTime storeTime;

    //样品类别 0-自检；1-外包
    private String sampleType;

    //天/次
    private String dayAndCount;
    //样品天数
    private int day;
    //样品频次
    private int frequency;
}
