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
public class SampleDetailVo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //采样时间
    private String collectTime;
    //审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime auditTime;
    //入库时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime storeTime;
    //送样人员
    private String sendUser;
    //采集位置
    private String collectLocation;
    //库存位置
    private String storeLocation;
    //样品备注
    private String sampRemark;
    //可领次数
    private Integer avalDrawCount;
    //已领次数
    private Integer drawCount;
    //监测因子
    private String factorName;
    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;

}
