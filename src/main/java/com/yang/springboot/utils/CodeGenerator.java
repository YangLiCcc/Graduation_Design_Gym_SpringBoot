package com.yang.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        fastAutoCodeGenerator();
    }

    public static void fastAutoCodeGenerator() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/gym_system",
                        "root",
                        "Ylc0612.")
                .globalConfig(builder -> {
                    builder.author("LambCcc") // 作者
                            .disableOpenDir() // 关闭自动打开目录
                            .outputDir("D:\\Desktop\\GraduationDesign\\version1\\springboot\\src\\main\\java"); // 输出路径
                })
                .packageConfig(builder -> {
                    builder.parent("com.yang.springboot") //设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\Desktop\\GraduationDesign\\version1\\springboot\\src\\main\\resources\\mapper")); // 设置mapper.xml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder() // 实体类策略配置
                            .enableLombok() // 开启lombok模型
                            .enableTableFieldAnnotation() // 开启生成实体类时 生成字段注解
                            .build();
                    builder.mapperBuilder() // Mapper策略配置
                            .enableMapperAnnotation() // 开启@Mapper注解
                            .formatMapperFileName("%sDao")
                            .build();
                    builder.controllerBuilder() // Controller策略配置
                            .enableHyphenStyle() // 开启驼峰转连字符
                            .enableRestStyle() // 开启生成@RestController控制器
                            .build();
                    builder.serviceBuilder() // Service策略配置
                            .formatServiceFileName("%sService") // 格式化service接口文件名称 XxService
                            .formatServiceImplFileName("%sServiceImpl") // 格式化service实现类文件名称 XxServiceImpl
                            .build();
                    builder.addInclude("sys_menu") // 增加表匹配
                            .addTablePrefix("sys_", "t_"); // 增加过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用FreeMarker引擎模板
                .execute();
    }

}
