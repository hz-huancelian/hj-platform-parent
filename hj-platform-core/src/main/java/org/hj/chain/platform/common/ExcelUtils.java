package org.hj.chain.platform.common;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : chem-erp
 * @Description : 利用alibaba的easyExcel进行简单封装实现excel的读取和写入
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2019/12/20  8:39 AM
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2019/12/20    create
 */
@Slf4j
public class ExcelUtils {

    /**
     * @Description: 写excel
     * @Param: []
     * @return: void
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2019/12/20 10:48 AM
     */
    public static void writeDymicHead(HttpServletResponse response, Class<?> clazz, List<?> dataList, List<List<String>> headList, String fileName, String sheetName) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String nfileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + nfileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), clazz).autoCloseStream(Boolean.FALSE)
                    .head(headList)
                    .sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            System.out.println("dataList " + dataList);
            System.out.println("headList " + headList);
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.parseFromMap(map));
        }
    }


    /**
     * @Description: 写excel
     * @Param: []
     * @return: void
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2019/12/20 10:48 AM
     */
    public static void write(HttpServletResponse response, Class<?> clazz, List<?> dataList, String fileName, String sheetName) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String nfileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + nfileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), clazz).autoCloseStream(Boolean.FALSE)
                    .sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.parseFromMap(map));
        }
    }
}