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
 * @description TODO 因子分类
 * @Iteration : 1.0
 * @Date : 2021/5/5  9:48 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_factor_class_info")
public class FactorClassInfo implements Serializable {
    private static final long serialVersionUID = 5406787532736789836L;

    //编号
    private String id;

    //因子分类名称
    private String name;

    //因子分类级别
    private String level;

    //父级ID
    private String parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}