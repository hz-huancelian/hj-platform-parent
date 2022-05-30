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
 * @Date : 2021/5/23  6:19 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_report_base_info")
public class ReportBaseInfo implements Serializable {
    private static final long serialVersionUID = -6001888894195630727L;

    @TableId(type = IdType.AUTO)
    private Long id;

    //机构ID
    private String organId;

    //cma号
    private String cmaNumber;

    //cnas号
    private String cnasNumber;

    //地址
    private String address;

    //邮编
    private String postCode;

    //电话
    private String tel;

    //传真
    private String fax;

    //网址
    private String website;

    //logo图片
    private String logoImageId;

    //水印图片
    private String watermarkImageId;

    //声明
    private String explains;

    //创建用户
    private String createUserId;

    private LocalDateTime createTime;

    private String updateUserId;

    private LocalDateTime updateTime;
}