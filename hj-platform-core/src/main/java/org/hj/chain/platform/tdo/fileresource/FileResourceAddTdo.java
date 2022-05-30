package org.hj.chain.platform.tdo.fileresource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileResourceAddTdo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "文件类型不能为空！")
    private String fileType;
    @NotBlank(message = "文件名称不能为空！")
    private String fileName;
    @NotBlank(message = "文件控制编号不能为空！")
    private String fileNo;
}
