package org.hj.chain.platform.vo.factor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : zzl
 * @description 二级分类字段实体
 * @Date : 2022.6.7
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorRainFieldVo implements Serializable {

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
