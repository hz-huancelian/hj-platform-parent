package org.hj.chain.platform;

import com.aspose.words.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * TODO `aspose-words`授权处理
 *
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2021/5/22 8:11 下午
 */
@Slf4j
public class MatchLicense {

    public static void init() {
        try {
            log.info("实现`aspose-words`授权 -> 去掉头部水印");
            /*
              实现匹配文件授权 -> 去掉头部水印 `Evaluation Only. Created with Aspose.Words. Copyright 2003-2018 Aspose Pty Ltd.` |
                                          `Evaluation Only. Created with Aspose.Cells for Java. Copyright 2003 - 2020 Aspose Pty Ltd.`
             */
            InputStream is = new ClassPathResource("license.xml").getInputStream();
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            log.error("《`aspose-words`授权》 失败： {}", e.getMessage());
        }
    }

}
