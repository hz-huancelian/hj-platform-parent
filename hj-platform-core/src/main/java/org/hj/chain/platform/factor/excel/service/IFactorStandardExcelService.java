package org.hj.chain.platform.factor.excel.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IFactorStandardExcelService {


    /**
     * TODO 导出
     *
     * @param response
     * @param sheetName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 4:55 下午
     */
    void export(HttpServletResponse response, String sheetName) throws IOException;


    /**
     * TODO 倒入Excel
      * @param response
     * @param file
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 5:18 下午
     */
    void importExcel(HttpServletResponse response, MultipartFile file) throws IOException;
}
