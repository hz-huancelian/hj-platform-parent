package org.hj.chain.platform.tdo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.SampleFactorDataVo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class MobileSampItemDetailTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    @NotNull(message = "样品ID不能为空！")
    //样品ID
    private Long sampleItemId;
//    @NotBlank(message = "样品录样位置不能为空！")
    //样品录样位置
    private String collectLocation;
    //样品录样位置（经纬度）
    private String collectAddress;
    /**
     * 采样数据-因子列表数据
     */
    List<SampleFactorDataVo> factorDataVos;
//    @NotBlank(message = "样品采集数据不能为空！")
    private String sampleData;
    @Size(max = 4)
    //样品采集图片
    private List<String> imageList;
    //录样备注
    private String collectRemark;
    //采样时间
    private String collectTime;
    //采样日期
    private LocalDate collectDate;
    //复核人
    private String reviewUserId;
    /**
     * 采样数据-特别说明值
     */
    private String specialNote;

    //样品性状
    private String sampleProperties;
    //样品固定剂
    private String sampleFixative;

    //二级类别属性结果值
    private String sampleDataValue;
    //污染物信息
    private String pollutantInfo;
}
