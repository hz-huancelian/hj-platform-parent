package org.hj.chain.platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.FileNameUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 文件操作
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-07
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-07
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Value("${image.upload.url.cy}")
    private String cyUploadFilePath;

    @Value("${image.upload.url.qz}")
    private String qzUploadFilePath;

    /**
      * TODO 采样信息图片上传
      * @Author chh
      * @param files
      * @Date 2021-05-07 19:29
      * @Iteration 1.0
      */
    @RequestMapping(value = "/cyFileUpload", method = RequestMethod.POST)
    public Result<Object> cyFileUpload(@RequestParam("files") MultipartFile[] files) {
        if(files == null || files.length == 0) {
            return ResultUtil.error("上传失败，请选择文件！");
        }
        StringBuilder fileNames = new StringBuilder();
        for(int i = 0; i < files.length; i++) {
            String fileName = FileNameUtil.getFileName(files[i].getOriginalFilename());
            fileNames.append(fileName).append(",");
            File dest = new File(cyUploadFilePath + "/" + fileName);
            if(!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try{
                files[i].transferTo(dest);
            }catch (Exception e){
                log.error("{}", e);
                return ResultUtil.error("程序错误，请重新上传！");
            }
        }
        return ResultUtil.data(fileNames.toString(), "文件上传成功！");
    }

    /**
      * TODO 签字图片上传
      * @Author chh
      * @param files
      * @Date 2021-05-07 19:30
      * @Iteration 1.0
      */
    @RequestMapping(value = "/qzFileUpload", method = RequestMethod.POST)
    public Result<Object> qzFileUpload(@RequestParam("files") MultipartFile[] files) {
        if(files == null || files.length == 0) {
            return ResultUtil.error("上传失败，请选择文件！");
        }
        StringBuilder fileNames = new StringBuilder();
        for(int i = 0; i < files.length; i++) {
            String fileName = FileNameUtil.getFileName(files[i].getOriginalFilename());
            fileNames.append(fileName).append(",");
            File dest = new File(qzUploadFilePath + "/" + fileName);
            if(!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try{
                files[i].transferTo(dest);
            }catch (Exception e){
                log.error("{}", e);
                return ResultUtil.error("程序错误，请重新上传！");
            }
        }
        return ResultUtil.data(fileNames.toString(), "文件上传成功！");
    }
}
