package org.hj.chain.platform.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car_record_info")
public class CarRecordInfo implements Serializable {
    private static final long serialVersionUID = -1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车辆ID
     */
    private Long carId;
    /**
     * 车辆状态：0-闲置中；1-使用中；2-维修中
     */
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
    /**
     * 管理任务ID
     */
    private String taskId;
    private LocalDateTime createTime;
    private String createUserId;
}
