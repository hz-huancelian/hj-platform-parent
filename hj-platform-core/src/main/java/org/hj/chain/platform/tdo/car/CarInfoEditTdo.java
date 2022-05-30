package org.hj.chain.platform.tdo.car;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarInfoEditTdo implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车辆编号
     */
    private String carBaseNumber;
    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空！")
    private String carNumber;
    /**
     * 车辆品牌类型
     */
    @NotBlank(message = "车辆品牌类型不能为空！")
    private String carType;
    /**
     * 维保时间
     */
    private LocalDate repairTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 车辆位置
     */
    @NotBlank(message = "车辆位置不能为空！")
    private String location;
}
