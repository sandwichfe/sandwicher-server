package com.lww.sandwich.gene;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author lww
 * @description: 代码生成器
 * @date 2022/3/10 13:30
 */
public class MpGeneCode {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "123456";
        String baseOutPutDir = "F://sandwicher//sandwich-server//src//main//java//";
        FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
                    builder.author("lww") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(baseOutPutDir); // 指定输出目录
                }).packageConfig(builder -> {
                    builder.parent("com.lww.sandwich") // 设置父包名
                            .moduleName("tt")// 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    "F://sandwicher//sandwich-server//src//main//resources//mapper//")); // 设置mapperXml生成路径
                }).strategyConfig(builder -> {
                    builder.addInclude("t_simple") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
