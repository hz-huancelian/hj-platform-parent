package org.hj.chain.platform.equipment.entity;

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
@TableName("t_equipment_record_info")
public class EquipmentRecordInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 使用者
     */
    private String username;
    /**
     * 使用人联系方式
     */
    private String linkmethod;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务点位
     */
    private String point;
    /**
     * 创建人
     */
    private String createUserId;
    private LocalDateTime createTime;
    /**
     * 类型：0-借出；1-归还；2-维修
     */
    private String type;

}
