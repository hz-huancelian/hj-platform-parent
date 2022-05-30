package org.hj.chain.platform.dataimport.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserExportEntity implements Serializable {
    @ExcelProperty("员工工号")
    private String empCode;
    @ExcelProperty("员工姓名")
    private String empName;
    @ExcelProperty("登录账号")
    private String username;
    @ExcelProperty("部门")
    private String deptName;
    @ExcelProperty("岗位")
    private String postNames;
    @ExcelProperty("角色")
    private String roleNames;
    @ExcelProperty("手机号码")
    private String phonenumber;
    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("创建日期")
    private String createDate;
}
