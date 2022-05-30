package org.hj.chain.platform.tdo.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarScheduTdo implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空！")
    private Long carId;
    /**
     * 车辆状态：0-闲置中；1-使用中；2-维修中
     */
    @NotBlank(message = "车辆状态不能为空！")
    private String carStatus;
    /**
     * 车辆位置
     */
    private String location;
    /**
     * 使用人员名称
     */
    private String username;
    /**
     * 使用人员联系方式
     */
    private String linkmethod;
}
