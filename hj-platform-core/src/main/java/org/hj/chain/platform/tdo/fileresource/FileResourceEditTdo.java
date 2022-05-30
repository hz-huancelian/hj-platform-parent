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
public class FileResourceEditTdo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "文件ID不能为空！")
    private Long id;
    @NotBlank(message = "文件控制编号不能为空！")
    private String fileNo;
}
