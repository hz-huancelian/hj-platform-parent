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
 * @description TODO 数据单位关系
 * @Iteration : 1.0
 * @Date : 2021/5/5  10:00 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_unit_group_related_info")
public class UnitGroupRelatedInfo implements Serializable {
    private static final long serialVersionUID = -714153999399913871L;

    //主键ID
    private String id;

    //数据单位ID
    private String unitId;

    //数据单位分组ID
    private String groupId;

    private LocalDateTime createTime;
}