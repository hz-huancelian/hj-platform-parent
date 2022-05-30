package org.hj.chain.platform.vo.fileresource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileResourceDisplayVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
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
    private String createUser;
    private LocalDateTime createTime;
    private String updateUser;
    private LocalDateTime updateTime;
}
