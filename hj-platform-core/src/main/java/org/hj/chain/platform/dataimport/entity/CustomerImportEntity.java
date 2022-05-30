package org.hj.chain.platform.dataimport.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerImportEntity implements Serializable {
    @ExcelProperty(index = 0)
    private String companyName;

    @ExcelProperty(index = 1)
    private String address;

    @ExcelProperty(index = 2)
    private String telFax;

    @ExcelProperty(index = 3)
    private String postCode;

    @ExcelProperty(index = 4)
    private String jurPerson;

    @ExcelProperty(index = 5)
    private String agentPerson;

    @ExcelProperty(index = 6)
    private String bankName;

    @ExcelProperty(index = 7)
    private String bankNo;

    @ExcelProperty(index = 8)
    private String taxNumber;
}
