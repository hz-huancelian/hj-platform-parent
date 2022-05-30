package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipmentStatusStatictisVo implements Serializable {
    private static final long serialVersionUID = -6304699473300983590L;
    /**
     * 一级类型
     */
    private Long firstType;
    /**
     * 设备状态：0-闲置中；1-使用中；2-维修中
     */
    private String status;
    /**
     * 计数
     */
    private Integer cnt;
}
