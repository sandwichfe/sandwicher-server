package com.lww.sandwich.gene;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author lww
 *   代码生成器
 * @date 2022/3/10 13:30
 */
public class MpGeneCode {
    private static String url = "jdbc:mysql://120.26.91.154:3306/sandwich";
    private static String username = "lww";
    private static String password = "123456";

    private static String tableNames = "t_view";

    public static void main(String[] args) {
        String baseOutPutDir = "F://sandwicher//sandwich-server//src//main//java//";
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("lww") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .commentDate("yyyy-MM-dd HH:mm:ss")
                            //                             .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() // 生成完不弹出对应目录
                            .outputDir(baseOutPutDir); // 指定输出目录
                })
                // 包配置相关
                .packageConfig(builder -> {
                    builder.parent("com.lww") // 设置父包名
                            .moduleName("sandwich")// 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "F://sandwicher//sandwich-server//src//main//resources//mapper//")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            // controller
                            .controllerBuilder().enableRestStyle()   // rest风格
                            // service
                            .serviceBuilder().formatServiceFileName("%sService") //service文件命名
                            .formatServiceImplFileName("%sServiceImpl")
                            // entity
                            .entityBuilder()     //实体类相关 builder
                            .enableLombok();     //开启lombok
                })
                //自定义模板
                .templateConfig(builder -> {
                    builder
                            //.disable(TemplateType.ENTITY)   //  暂时没发现有啥用
                            .controller("/templates/controller.java")    //  模板位置
                            .service("/templates/service.java")          //
                            .serviceImpl("/templates/serviceImpl.java")  //
                            .entity("/templates/entity.java")            //
                            .mapper("/templates/mapper.java")            //
                            .xml("/templates/mapper.xml")                //
                            .build();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // 其他注入设置
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        //System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    }).build();
                }).execute();
    }

}
