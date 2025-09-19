package com.lww.generator;


import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成器
 *
 * @author lww
 * @since 2022/3/10 13:30
 */
@Slf4j
public class MpGeneCode {

    /** java文件生成目录 */
    private static  String baseOutPutDir;
    /** mapper文件生成目录 */
    private static  String baseOutPutMapperDir;

    /** 数据库连接信息 */
    private static final String URL = "jdbc:mysql://localhost/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    /** 要生成的表 */
    private static final String TABLE_NAMES = "test";

    static {
        try {
            // 获取当前类的路径
            URL location = MpGeneCode.class.getProtectionDomain().getCodeSource().getLocation();
            // 转换为绝对路径
            Path parentPath = Paths.get(location.toURI()).toAbsolutePath();

            // 处理目标路径(移除target/classes部分)
            if (parentPath.endsWith("target/classes") || parentPath.endsWith("target\\classes")) {
                parentPath = parentPath.getParent().getParent();
            }

            // 构建最终路径src/main/java/，使用Path.resolve确保兼容性
            baseOutPutDir = parentPath
                    .resolve("src").resolve("main").resolve("java")
                    + File.separator;

            // src/main/resources/mapper/
            baseOutPutMapperDir = parentPath
                    .resolve("src").resolve("main").resolve("resources").resolve("mapper")
                    + File.separator;
        } catch (URISyntaxException e) {
            log.error("获取当前类路径失败", e);
        }
    }

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder ->
                        builder.author("lww")
                                // 文档为springdoc
                                .enableSpringdoc()
                                .commentDate("yyyy-MM-dd HH:mm:ss")
                                // 生成完不弹出对应目录
                                .disableOpenDir()
                                // 指定输出目录
                                .outputDir(baseOutPutDir)
                )
                // 包配置相关
                .packageConfig(builder ->

                        builder
                                // 设置父包名
                                .parent("com.lww")
                                // 设置父包模块名
                                .moduleName("generator")
                                // 设置mapperXml生成路径
                                .pathInfo(Collections.singletonMap(OutputFile.xml, baseOutPutMapperDir))
                )
                // 策略配置
                .strategyConfig(builder ->
                        builder
                                // 设置需要生成的表名
                                .addInclude(TABLE_NAMES)
                                // 设置过滤表前缀
                                .addTablePrefix("t_", "c_")
                                // controller
                                .controllerBuilder()
                                // rest风格
                                .enableRestStyle()
                                // service
                                .serviceBuilder()
                                // service文件命名
                                .formatServiceFileName("%sService")
                                .formatServiceImplFileName("%sServiceImpl")
                                // entity
                                .entityBuilder()
                                .enableLombok()
                                // 覆盖文件
                                .enableFileOverride()
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        log.info("gene successful");
    }

}
