package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 单位信息
 * @Iteration : 1.0
 * @Date : 2021/5/5  10:03 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_unit_info")
public class UnitInfo implements Serializable {
    private static final long serialVersionUID = 4909792447909247251L;

    //数据单位ID
    private String id;

    //数据单位名称
    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}