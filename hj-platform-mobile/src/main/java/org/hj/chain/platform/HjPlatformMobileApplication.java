package org.hj.chain.platform;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan({"org.hj.chain.platform.*.mapper", "org.hj.chain.platform.mapper"})
public class HjPlatformMobileApplication {

    public static void main(String[] args) {
        //DocsConfig config = new DocsConfig();
        //config.setProjectPath("D:\\idea-work\\hj-platform-parent\\hj-platform-mobile"); // 项目根目录
        //config.setProjectName("环测项目"); // 项目名称
        //config.setApiVersion("V1.0");       // 声明该API的版本
        //config.setDocsPath("D:\\idea-work\\hj-platform-parent\\hj-platform-mobile\\api"); // 生成API 文档所在目录
        //config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        //Docs.buildHtmlDocs(config); // 执行生成文档
        SpringApplication.run(HjPlatformMobileApplication.class, args);
    }

}
