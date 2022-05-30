package org.hj.chain.platform.dataimport.controller;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.dataimport.service.IDataImportService;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/dataImport")
public class DataImportController {

    @Autowired
    private IDataImportService dataImportService;

    /**
     * 下载设备导入excel模板
     * @param response
     */
    @RequestMapping(value = "/downloadEquipmentExcel", method = RequestMethod.GET)
    public void downloadEquipmentExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String modelName = "import_equipment.xlsx";
            dataImportService.downloadModel(modelName, request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 设备信息导入
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadEquipent", method = RequestMethod.POST)
    public Result<Object> uploadEquipent(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            return ResultUtil.busiError("设备导入文件不能为空！");
        }
        return dataImportService.uploadEquipent(file);
    }

    /**
     * 设备信息导出
     * @param response
     * @param sv
     * @throws IOException
     */
    @RequestMapping(value = "/exportEquipment", method = RequestMethod.GET)
    public void exportEquipment(HttpServletResponse response,
                                @ModelAttribute EquipmentSearchVo sv) throws IOException {
        dataImportService.exportEquipment(response, sv);
    }


    /**
     * 下载员工信息导入excel模板
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadEmployeeExcel", method = RequestMethod.GET)
    public void downloadEmployeeExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String modelName = "import_user.xlsx";
            dataImportService.downloadModel(modelName, request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 员工信息导入
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadEmployee", method = RequestMethod.POST)
    public Result<Object> uploadEmployee(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            return ResultUtil.busiError("员工导入文件不能为空！");
        }
        return dataImportService.uploadEmployee(file);
    }

    /**
     * 员工信息导出
     * @param response
     */
    @RequestMapping(value = "/exportEmployee", method = RequestMethod.GET)
    public void exportEmployee(HttpServletResponse response) throws IOException {
        dataImportService.exportEmployee(response);
    }

    /**
     * 下载客户导入信息模板
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadCustomerExcel", method = RequestMethod.GET)
    public void downloadCustomerExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String modelName = "import_customer.xlsx";
            dataImportService.downloadModel(modelName, request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 客户信息导入
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadCustomer", method = RequestMethod.POST)
    public Result<Object> uploadCustomer(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            return ResultUtil.busiError("客户导入文件不能为空！");
        }
        return dataImportService.uploadCustomer(file);
    }

    /**
     * 客户信息导出
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/exportCustomer", method = RequestMethod.GET)
    public void exportCustomer(HttpServletResponse response) throws IOException {
        dataImportService.exportCustomer(response);
    }
}
