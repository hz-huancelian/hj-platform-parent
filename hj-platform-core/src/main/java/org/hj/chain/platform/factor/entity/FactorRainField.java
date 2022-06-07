package org.hj.chain.platform.factor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : zzl
 * @description 二级类别字段
 * @Date : 2022.6.6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factor_class_info_field")
public class FactorRainField {

    private static final long serialVersionUID = 1L;

    private String id;

    //二级类别id
    private String secdClassId;

    //二级类别名称
    private String secdClassName;

    //二级类别字段
    private String fieldName;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime updateTime;

    //是否删除：0-未删除；1-删除
    private int isDeleted;

}
