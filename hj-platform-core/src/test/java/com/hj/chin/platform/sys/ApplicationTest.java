package com.hj.chin.platform.sys;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/4/27  10:56 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/27    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ApplicationTest {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("/Users/lijinku/Documents/projects/hj-platform-parent"); // 项目根目录
        config.setProjectName("环测项目"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath("/Users/lijinku/Documents/projects/hj-platform-parent/api"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }
}