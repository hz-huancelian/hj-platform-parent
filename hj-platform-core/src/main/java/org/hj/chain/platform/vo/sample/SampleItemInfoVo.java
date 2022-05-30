package org.hj.chain.platform.vo.sample;

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
 * @Iteration : 3.0
 * @Date : 2022/3/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleItemInfoVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //任务单号
    private String jobId;
    //项目名称
    private String projectName;
    //受检单位
    private String inspectionName;
    //采样人
    private String collectUserId;
    private String collectUser;
    //复核人
    private String reviewUserId;
    private String reviewUser;
    //确认人
    private String sampleUserId;
    private String sampleUser;
    //检测位置
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime finishTime;

    /**
     * 样品状态：0-待采样；1-已采样；2-待组长审核；3-待负责人审核；4-审核通过；5-审核失败；6-已入库;7-部分出库；8-已出库；9-入库中；10-待复核
     */
    private String sampleStatus;
}
