package com.lww.generator;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * 代码生成器
 *
 * @author lww
 * @since 2022/3/10 13:30
 */
@Slf4j
public class MpGeneCode {

    private static  String BASE_OUT_PUT_DIR;
    private static  String BASE_OUT_PUT_MAPPER_DIR;
    private static  String TABLE_NAMES = "your table name";
    static {
        try {
            // 获取当前类的路径
            URL location = MpGeneCode.class.getProtectionDomain().getCodeSource().getLocation();
            // 转换为绝对路径
            String parentPath = Paths.get(location.toURI()).toAbsolutePath().toString().replace("\\", "//").replace("//target//classes", "");
            BASE_OUT_PUT_DIR = parentPath+"//src//main//java//";
            BASE_OUT_PUT_MAPPER_DIR = parentPath+"//src//main//resources//mapper//";
        } catch (URISyntaxException e) {
            log.error("获取当前类路径失败", e);
        }
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost/local_test";
        String username = "root";
        String password = "123456";
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder ->
                        builder.author("lww") // 设置作者
                                // .enableSwagger() // 开启 swagger 模式
                                .enableSpringdoc() // 文档为springdoc
                                .commentDate("yyyy-MM-dd HH:mm:ss")
                                .disableOpenDir() // 生成完不弹出对应目录
                                .outputDir(BASE_OUT_PUT_DIR) // 指定输出目录
                )
                // 包配置相关
                .packageConfig(builder ->
                        builder.parent("com.lww") // 设置父包名
                                .moduleName("generator")// 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, BASE_OUT_PUT_MAPPER_DIR)) // 设置mapperXml生成路径
                )
                // 策略配置
                .strategyConfig(builder ->
                        builder.addInclude(TABLE_NAMES) // 设置需要生成的表名
                                .addTablePrefix("t_", "c_") // 设置过滤表前缀
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
                //自定义模板
                .templateConfig(builder ->
                        builder
                                //.disable(TemplateType.ENTITY)   //  暂时没发现有啥用
                                .controller("/templates/controller.java")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .entity("/templates/entity.java")
                                .mapper("/templates/mapper.java")
                                .xml("/templates/mapper.xml")
                                .build())
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // 其他注入设置
                .injectionConfig(builder ->
                        builder.beforeOutputFile((tableInfo, objectMap) -> {
                            //System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                        }).build()
                ).execute();
        log.info("gene successful");
    }

}
