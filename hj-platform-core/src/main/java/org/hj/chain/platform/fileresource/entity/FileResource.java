package org.hj.chain.platform.fileresource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_file_resource")
public class FileResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 文件编号
     */
    private String fileCode;
    /**
     * 机构编号
     */
    private String organId;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件控制编号
     */
    private String fileNo;
    private String createUserId;
    private LocalDateTime createTime;
    private String updateUserId;
    private LocalDateTime updateTime;

}
