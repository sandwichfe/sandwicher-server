package com.lww.generator;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

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
public class MpGeneCode {
    private static String url = "jdbc:mysql://localhost/local_test";
    private static String username = "root";
    private static String password = "123456";
    private static String baseOutPutDir;
    private static String baseOutPutMapperDir ;
    private static final String tableNames = "your table name";
    static {
        try {
            // 获取当前类的路径
            URL location = MpGeneCode.class.getProtectionDomain().getCodeSource().getLocation();
            // 转换为绝对路径
            String parentPath = Paths.get(location.toURI()).toAbsolutePath().toString().replace("\\", "//").replace("//target//classes", "");
            baseOutPutDir = parentPath+"//src//main//java//";
            baseOutPutMapperDir = parentPath+"//src//main//resources//mapper//";
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder ->
                    builder.author("lww") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            .enableSpringdoc() // 文档为springdoc
                            .commentDate("yyyy-MM-dd HH:mm:ss")
                                                        // .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() // 生成完不弹出对应目录
                            .outputDir(baseOutPutDir) // 指定输出目录
                )
                // 包配置相关
                .packageConfig(builder ->
                    builder.parent("com.lww") // 设置父包名
                            .moduleName("generator")// 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, baseOutPutMapperDir)) // 设置mapperXml生成路径
                )
                // 策略配置
                .strategyConfig(builder ->
                    builder.addInclude(tableNames) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            // controller
                            .controllerBuilder().enableRestStyle()   // rest风格
                            // service
                            .serviceBuilder().formatServiceFileName("%sService") //service文件命名
                            .formatServiceImplFileName("%sServiceImpl")
                            // entity
                            .entityBuilder()     //实体类相关 builder
                            .enableLombok()    //开启lombok
                )
                //自定义模板
                .templateConfig(builder -> {
                    builder
                            //.disable(TemplateType.ENTITY)   //  暂时没发现有啥用
                            .controller("/templates/controller.java")
                            .service("/templates/service.java")
                            .serviceImpl("/templates/serviceImpl.java")
                            .entity("/templates/entity.java")
                            .mapper("/templates/mapper.java")
                            .xml("/templates/mapper.xml")
                            .build();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // 其他注入设置
                .injectionConfig(builder ->
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        //System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    }).build()
                ).execute();
        System.out.println("gene successful");
    }

}
