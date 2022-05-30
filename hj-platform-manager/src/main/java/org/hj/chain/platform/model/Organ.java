package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 机构信息
 * @Iteration : 1.0
 * @Date : 2021/5/7  10:34 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_organ")
public class Organ implements Serializable {
    private static final long serialVersionUID = 7570393730948970805L;

    @TableId(type = IdType.AUTO)
    private Long id;

    //机构号
    private String organId;

    //机构名称
    private String organName;

    //机构联系方式
    private String organPhone;

    //法人名称
    private String organJurPerson;

    //用户名
    private String username;

    //创建人
    private String createUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}