package org.hj.chain.platform;

import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
@Slf4j
public class FileUtil {

    /**
     * 多文件或目录压缩：将`srcPath`目录以及其目录下的所有文件目录打包到`zipPath`+`suffixFileName`文件中 【采用hutool工具类进行打包文件】
     *
     * @param srcPath:        需打包的源目录
     * @param zipPath:        打包后的路径+文件后缀名
     * @param isWithSrcDir:   是否带目录显示 （true:表示带目录显示）
     * @param isDeleteSrcZip: 是否删除源目录
     * @return: java.lang.String
     */
    public static File zip(String srcPath, String zipPath, boolean isWithSrcDir, boolean isDeleteSrcZip) {
        log.debug("【压缩文件】 源目录路径: 【{}】 打包后的路径+文件后缀名: 【{}】", srcPath, zipPath);
        File zipFile = ZipUtil.zip(srcPath, zipPath, isWithSrcDir);
        // 删除目录 -> 保证下次生成文件的时候不会累计上次留下的文件
        if (isDeleteSrcZip) {
            FileUtil.deleteFileOrFolder(srcPath);
        }
        return zipFile;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param fullFileOrDirPath: 要删除的目录或文件
     * @return: 删除成功返回 true，否则返回 false
     */
    public static boolean deleteFileOrFolder(String fullFileOrDirPath) {
        return cn.hutool.core.io.FileUtil.del(fullFileOrDirPath);
    }

    /**
     * 根据路径创建文件
     *
     * @param fullFilePath: 文件生成路径
     * @return: 文件信息
     */
    public static File touch(String fullFilePath) {
        return cn.hutool.core.io.FileUtil.touch(fullFilePath);
    }

//    /**
//     * 解压
//     *
//     * @param inputStream:
//     *            流
//     * @param zipFilePath:
//     *            zip文件路径
//     * @param outFileDir:
//     *            解压后的目录路径
//     * @param isDeleteZip:
//     *            是否删除源zip文件
//     * @return: 解压后的文件File信息
//     */
//    @SneakyThrows(Exception.class)
//    public static File unzip(InputStream inputStream, String zipFilePath, String outFileDir, boolean isDeleteZip) {
//        log.debug("【解压文件】 zip文件路径: 【{}】 解压后的目录路径: 【{}】", zipFilePath, outFileDir);
//        // zip压缩文件
//        File zipFile = cn.hutool.core.io.FileUtil.newFile(zipFilePath);
//        // 写入文件
//        FileUtil.copyInputStreamToFile(inputStream, zipFile);
//        // 编码方式 "UTF-8" 、"GBK" 【注： gbk编码才能解决报错: java.lang.IllegalArgumentException: MALFORMED】
//        File outFile = ZipUtil.unzip(zipFilePath, outFileDir, Charset.forName("GBK"));
//        // 删除zip -> 保证下次解压后的文件数据不会累计上次解压留下的文件
//        if (isDeleteZip) {
//            MyFileUtil.deleteFileOrFolder(zipFilePath);
//        }
//        return outFile;
//    }

    /**
     * 读取文件内容
     *
     * @param file: 文件数据
     * @return: 文件内容
     * @author : zhengqing
     * @date : 2020/9/5 23:00
     */
    public static String readFileContent(File file) {
        return cn.hutool.core.io.FileUtil.readUtf8String(file);
    }

    /**
     * 读取文件内容
     *
     * @param filePath: 文件路径
     * @return: 文件内容
     * @author : zhengqing
     * @date : 2020/9/5 23:00
     */
    public static String readFileContent(String filePath) {
        return cn.hutool.core.io.FileUtil.readUtf8String(filePath);
    }

    /**
     * 读取文件数据
     *
     * @param filePath: 文件路径
     * @return: 文件字节码
     * @author : zhengqing
     * @date : 2020/9/5 23:00
     */
    public static byte[] readBytes(String filePath) {
        return cn.hutool.core.io.FileUtil.readBytes(filePath);
    }

    /**
     * 写入文件内容
     *
     * @param fileContent: 文件内容
     * @param filePath:    文件路径
     * @return: 文件信息
     * @author : zhengqing
     * @date : 2020/11/17 21:38
     */
    @SneakyThrows(Exception.class)
    public static File writeFileContent(String fileContent, String filePath) {
        return cn.hutool.core.io.FileUtil.writeUtf8String(fileContent, filePath);
    }

    /**
     * 字节码写入文件
     *
     * @param data:     字节码
     * @param filePath: 文件路径
     * @return: 文件信息
     * @author : zhengqing
     * @date : 2020/11/24 14:36
     */
    @SneakyThrows(Exception.class)
    public static File writeFileContent(byte[] data, String filePath) {
        return cn.hutool.core.io.FileUtil.writeBytes(data, filePath);
    }

    public static String fileUpload(MultipartFile file, String uploadFilePath) {
        String fileName = FileNameUtil.getFileName(file.getOriginalFilename());
        File dest = new File(uploadFilePath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
        return fileName;
    }

    /**
     * TODO 带文件名报告上传
     *
     * @param file
     * @param fileName
     * @param uploadFilePath
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/27 11:20 上午
     */
    public static String fileReportUploadByFileName(MultipartFile file, String fileName, String uploadFilePath) {
        fileName = fileName + "_upload" + FileNameUtil.getSuffix(file.getOriginalFilename());
        File dest = new File(uploadFilePath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            log.error("{}", e);
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

    /**
     * TODO 带文件名上传
     *
     * @param file
     * @param fileName
     * @param uploadFilePath
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/27 11:20 上午
     */
    public static String fileUploadByFileName(MultipartFile file, String fileName, String uploadFilePath) {
        fileName = fileName + FileNameUtil.getSuffix(file.getOriginalFilename());
        File dest = new File(uploadFilePath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
        return fileName;
    }


}
