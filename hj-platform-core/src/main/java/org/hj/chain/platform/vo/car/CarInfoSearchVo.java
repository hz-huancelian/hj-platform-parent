package org.hj.chain.platform.vo.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarInfoSearchVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 车辆编号
     */
    private String carBaseNumber;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 设备状态：0-闲置中；1-使用中；2-维修中
     */
    private String carStatus;
}
