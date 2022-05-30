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
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_schedule_job")
public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(type = IdType.INPUT)
    private String id;
    //机构ID
    private String organId;
    //任务调度ID
    private Long taskId;
    //任务开始日期
    private String startDate;
    //任务结束日期
    private String endDate;
    //省份
    private String province;
    //城市
    private String city;
    //区县
    private String county;
    //项目地址
    private String projectAddress;
    //检测目的
    private String checkGoal;
    //调度任务状态：0-待确认；1：未完成；2:完成
    private String jobStatus;
    //调度人
    private String createUserId;
    //任务备注
    private String jobRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    //受检单位
    private String inspectionName;
    //受检联系人
    private String inspectionLinker;
    //受检人联系电话
    private String inspectionLinkerPhone;
    //委托单位
    private String consignorName;
    //委托联系人
    private String consignorLinker;
    //委托联系方式
    private String consignorLinkerPhone;
    //项目名称
    private String projectName;

}
