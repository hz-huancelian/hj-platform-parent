package org.hj.chain.platform.factor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : zzl
 * @description 上传文件实体
 * @Date : 2022.6.8
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_factor_class_info_file")
public class FactorRainFile {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //文件类型
    private String fileType;

    //文件名称
    private String fileName;

    //机构id
    private String organId;

    //上传文件的id
    private String factorFileId;

    //控制编号
    private String fileNo;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime updateTime;

}
