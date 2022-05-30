package org.hj.chain.platform.dataimport.service;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IDataImportService {
    void downloadModel(String modelName, HttpServletRequest request, HttpServletResponse response) throws Exception;

    Result<Object> uploadEquipent(MultipartFile file);

    void exportEquipment(HttpServletResponse response, EquipmentSearchVo sv) throws IOException;

    Result<Object> uploadEmployee(MultipartFile file);

    void exportEmployee(HttpServletResponse response) throws IOException;

    Result<Object> uploadCustomer(MultipartFile file);

    void exportCustomer(HttpServletResponse response) throws IOException;
}
