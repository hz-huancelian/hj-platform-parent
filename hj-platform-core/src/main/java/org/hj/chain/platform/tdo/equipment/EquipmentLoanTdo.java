package org.hj.chain.platform.tdo.equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EquipmentLoanTdo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空！")
    private Long equipmentId;
    /**
     * 使用者
     */
    @NotBlank(message = "使用者不能为空！")
    private String username;
    /**
     * 使用人员联系方式
     */
    @NotBlank(message = "使用者联系方式不能为空！")
    private String linkmethod;
    /**
     * 关联任务ID
     */
    @NotBlank(message = "任务单号不能为空！")
    private String taskId;
    /**
     * 点位信息
     */
    @NotBlank(message = "任务点位不能为空！")
    private String point;
}
