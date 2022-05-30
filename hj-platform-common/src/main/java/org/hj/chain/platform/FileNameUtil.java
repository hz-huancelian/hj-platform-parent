package org.hj.chain.platform;

import cn.hutool.core.util.StrUtil;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-07
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-07
 */
public class FileNameUtil {

    /**
      * TODO 获取文件后缀
      * @Author chh
      * @param fileName
      * @Date 2021-05-07 19:01
      * @Iteration 1.0
      */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
      * TODO 生成新的文件名
      * @Author chh
      * @param fileOriginName 源文件名
      * @Date 2021-05-07 19:01
      * @Iteration 1.0
      */
    public static String getFileName(String fileOriginName){
        return StrUtil.uuid().replace("-", "") + FileNameUtil.getSuffix(fileOriginName);
    }
}
