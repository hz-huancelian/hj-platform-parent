package org.hj.chain.platform.dataimport.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserImportEntity implements Serializable {
    @ExcelProperty(index = 0)
    private String empCode;
    @ExcelProperty(index = 1)
    private String empName;
    @ExcelProperty(index = 2)
    private String phonenumber;
    @ExcelProperty(index = 3)
    private String email;
    //0男 1女 2未知
    @ExcelProperty(index = 4)
    private String sex;
    @ExcelProperty(index = 5)
    private String remark;
}
