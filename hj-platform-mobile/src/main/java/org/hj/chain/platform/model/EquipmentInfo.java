package org.hj.chain.platform.model;

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
@TableName("t_equipment_info")
public class EquipmentInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 机构ID
     */
    private String organId;
    /**
     * 设备编号
     */
    private String equipmentNumber;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备一级类型
     */
    private Long equipmentFirstType;
    /**
     * 设备二级类型
     */
    private Long equipmentSecondType;
    /**
     * 设备品牌
     */
    private String equipmentBrand;
    /**
     * 设备型号
     */
    private String equipmentModel;
    /**
     * 上次鉴定时间
     */
    private LocalDateTime lastTime;
    /**
     * 鉴定周期：0-月；1-季度；2-半年；3-1年；4-2年；5-3年
     */
    private String checkCircle;
    /**
     * 备注
     */
    private String remark;
    /**
     * 设备状态：0-闲置中；1-使用中；2-维修中
     */
    private String equipmentStatus;
    private LocalDateTime createTime;
    private String createUserId;
    private LocalDateTime updateTime;
    private String updateUserId;
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
     * 点位信息
     */
    private String point;

}
