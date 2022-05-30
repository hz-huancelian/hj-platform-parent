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
 * @Project : chem-erp
 * @Description : TODO
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/3/31  1:46 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/03/31    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_dict")
public class SysDict implements Serializable {
    private static final long serialVersionUID = -7706622418201192734L;

    @TableId(type = IdType.AUTO)
    private Long id;

    //字典key
    private Long dictKey;

    //字典值
    private String dictVal;

    //字典描述
    private String dictDesc;

    //上级key
    private Long parentDictKey;

    //字典状态：0-有效；1-无效
    private String dictStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}