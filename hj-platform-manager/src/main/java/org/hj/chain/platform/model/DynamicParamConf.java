package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/8
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_dynamic_param_conf")
public class DynamicParamConf implements Serializable {
    private static final long serialVersionUID = -8619668536370323337L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //因子二级分类ID
    private String secdClassId;
    //二级分类名称
    private String secdClassName;
    //属性键
    private String pKey;
    //属性名
    private String pName;
    //必填标记
    private Boolean required;
    //数值类型
    private String numericType;
    //0-级联下拉框；1-输入框；2-选择框; 4-日期；5-时间；6-时段；7-单选下拉
    private String labelType;
    //0-采样依据；1-采样设备
    private String labelValue;
    //选择框值
    private String radioValue;
    //下拉框值
    private String selectValue;
    //分组
    private String groupKey;
    //分组名
    private String groupName;
    //排序
    private Integer sort;
    //提示值
    private String promptValue;
    //删除标记
    private String isDelete;

}
