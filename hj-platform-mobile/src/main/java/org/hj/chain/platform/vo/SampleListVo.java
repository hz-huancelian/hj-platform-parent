package org.hj.chain.platform.vo;

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
 * @Date : 2022/4/3
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleListVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //样品ID
    private Long sampItemId;
    //样品编号
    private String sampleNo;
    //检测位置
    private String factorPoint;
    //检测类别
    private String secdClassName;
    //检测因子
    private String factorName;
    //采样人
    private String collectUser;
    //样品采样时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String collectTime;
}
