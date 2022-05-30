package org.hj.chain.platform.tdo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportBaseInfoTdo implements Serializable {
    private static final long serialVersionUID = 3245353614442144308L;
    //cma号
    @NotBlank(message = "cma号不能为空！")
    private String cmaNumber;

    //cnas号
    private String cnasNumber;

    //地址
    @NotBlank(message = "地址不能为空！")
    private String address;

    //邮编
    @NotBlank(message = "邮编不能为空！")
    private String postCode;

    //电话
    @NotBlank(message = "电话号不能为空！")
    private String tel;

    //传真
    @NotBlank(message = "传真不能为空！")
    private String fax;

    //网址
    @NotBlank(message = "网址不能为空！")
    private String website;

    //logo图片
    @NotBlank(message = "logo图片不能为空！")
    private String logoImageId;

    //水印图片
    @NotBlank(message = "水印图片不能为空！")
    private String watermarkImageId;

    //声明
    private String explains;
}
