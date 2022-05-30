package org.hj.chain.platform.report.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  6:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_report_info")
public class ReportInfo implements Serializable {
    private static final long serialVersionUID = 5171983298945975447L;

    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //报告编号
    private String reportCode;

    //报告状态'0': '未生成',
    //  '1': '制作中',
    //  '2': '待制作',
    //  '3': '待提交审批',
    //  '4': '待审核',
    //  '5': '待签发',
    //  '6': '完成',
    //  '7': '已驳回'
    private String reportStatus;

    //机构ID
    private String organId;

    /**
     * 调度任务单号
     */
    private String jobId;

    //报告制作人
    private String reportMakeUserId;

    //报告制作时间
    private LocalDateTime reportMakeTime;

    //报告文件ID
    private String reportFileId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}