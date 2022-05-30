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
public class CustomerExportEntity implements Serializable {
    @ExcelProperty("单位名称")
    private String companyName;

    @ExcelProperty("通讯地址")
    private String address;

    @ExcelProperty("电话/传真")
    private String telFax;

    @ExcelProperty("邮政编码")
    private String postCode;

    @ExcelProperty("法定代表人")
    private String jurPerson;

    @ExcelProperty("委托代理人")
    private String agentPerson;

    @ExcelProperty("开户银行")
    private String bankName;

    @ExcelProperty("银行账号")
    private String bankNo;

    @ExcelProperty("企业税号")
    private String taxNumber;
}
