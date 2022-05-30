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
 * 岗位表 sys_post
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_post")
public class SysPost implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位序号
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位排序
     */
    private String postSort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;


    //机构码
    private String organId;

    //创建时间
    private LocalDateTime createTime;


    //更新时间
    private LocalDateTime updateTime;

    //备注
    private String remark;



}
