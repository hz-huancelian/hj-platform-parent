package org.hj.chain.platform.report.controller;

import org.hj.chain.platform.FileUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.report.model.ReportBaseInfo;
import org.hj.chain.platform.report.service.IReportBaseInfoService;
import org.hj.chain.platform.tdo.report.ReportBaseInfoTdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reportBase")
public class ReportBaseInfoController {
    @Autowired
    private IReportBaseInfoService reportBaseInfoService;

    @Value("${image.upload.report}")
    private String uploadReportImgPath;

    /**
     * TODO 报告基础信息图片上传（logo、水印）
     * @param file
     * @Author chh
     * @Date 2021-05-26 11:04
     * @Iteration 1.0
     */
    @RequestMapping(value = "/uploadReportImg", method = RequestMethod.POST)
    public Result<Object> uploadReportImg(@RequestParam("file") MultipartFile file) {
        String imgName = FileUtil.fileUpload(file, uploadReportImgPath);
        if (imgName == null) {
            return ResultUtil.busiError("程序错误，请重新上传！");
        }
        return ResultUtil.data(imgName);
    }

    /**
     * TODO 报告基础信息保存
     * @param tdo
     * @Author chh
     * @Date 2021-05-26 11:13
     * @Iteration 1.0
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@Validated @RequestBody ReportBaseInfoTdo tdo) {
        return reportBaseInfoService.save(tdo);
    }

    /**
     * TODO 获取当前机构报告模板信息
     * @Author chh
     * @Date 2021-05-26 11:24
     * @Iteration 1.0
     */
    @RequestMapping(value = "/getByOrganId", method = RequestMethod.GET)
    public Result<ReportBaseInfo> getByOrganId() {
        return reportBaseInfoService.getByOrganId();
    }
}
