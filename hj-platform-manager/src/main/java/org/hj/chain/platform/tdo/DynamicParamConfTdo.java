package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/9
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DynamicParamConfTdo implements Serializable {
    private static final long serialVersionUID = -8619668536370323337L;
    @NotBlank(message = "二级分类编号不能为空！")
    private String secdClassId;
    @NotBlank(message = "二级分类名称不能为空！")
    private String secdClassName;
    @NotBlank(message = "属性键不能为空！")
    private String key;
    @NotBlank(message = "属性名不能为空！")
    private String name;
    @NotBlank(message = "必填标记不能为空！")
    private Boolean required;
    @NotBlank(message = "数值类型不能为空！")
    private String numericType;
    //0-下拉框；1-输入框；2-选择框
    @NotBlank(message = "标签类型不能为空！")
    private String labelType;
    //0-采样依据；1-采样设备
    private String labelValue;
    //选择框值
    private String radioValue;
    //下拉框值
    private String selectValue;
    @NotNull(message = "展示类型不能为空！")
    private Integer showType;
    @NotBlank(message = "分组key不能为空！")
    private String groupKey;
    //分组名
    private String groupName;
    @NotNull(message = "顺序值不能为空！")
    private Integer sort;
}
