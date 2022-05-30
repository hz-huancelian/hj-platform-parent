package org.hj.chain.platform.vo.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarInfoDisplayVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 机构编号
     */
    private String organId;
    /**
     * 车辆编号
     */
    private String carBaseNumber;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 车辆品牌类型
     */
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
     * 设备状态：0-闲置中；1-使用中；2-维修中
     */
    private String carStatus;
    private LocalDateTime createTime;
    private String createUser;
    private LocalDateTime updateTime;
    private String updateUser;
    /**
     * 删除标志 0-正常；1-删除
     */
    private String isDelete;
    /**
     * 使用者
     */
    private String username;
    /**
     * 使用人员联系方式
     */
    private String linkmethod;
    /**
     * 关联任务ID
     */
    private String taskId;
    /**
     * 车辆位置
     */
    private String location;
}
