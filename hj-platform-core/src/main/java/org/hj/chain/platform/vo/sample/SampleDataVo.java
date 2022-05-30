package org.hj.chain.platform.vo.sample;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleDataVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //采样时间
    private String collectTime;
    //采样日期
    private LocalDate collectDate;
    //录样时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //采样员
    private String collectUser;
    //采样数据-因子 json存储
    private List<SampleFactorDataVo> factorDataVos;
    /**
     * 采样数据-二级类别 json存储
     */
    private String sampleData;
    //采样备注
    private String collectRemark;
    //录样位置
    private String collectLocation;
    //录样图片1
    private String collectIamge1;
    //录样图片2
    private String collectIamge2;
    //录样图片3
    private String collectIamge3;
    //录样图片4
    private String collectIamge4;
    //样品数量
    private Integer sampleNum;
    /**
     * 检测位置
     */
    private String factorPoint;
    //特别说明值
    private String specialNote;
}
