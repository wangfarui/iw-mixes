package com.itwray.iw.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.itwray.iw.web.model.entity.IdEntity;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Types;
import java.util.Collections;

/**
 * IW项目-代码生成器
 *
 * @author wray
 * @since 2024/11/5
 */
public class CodeGenerator {

    public static void generate(GlobalConfig config, String... tableNames) {
        if (tableNames == null || tableNames.length == 0) {
            System.out.println("数据表为空，跳过生成");
            return;
        }

        if (config == null) {
            throw new IllegalArgumentException("globalConfig must be not null");
        }

        System.out.println("开始生成文件...");

        FastAutoGenerator.create(config.getDbUrl(), config.getDbUsername(), config.getDbPassword())
                .globalConfig(builder -> {
                    builder.author(config.getAuthor()) // 设置作者
                            .disableOpenDir()
                            // 指定输出Java文件的目录
                            .outputDir(config.getJavaDir());
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT || typeCode == Types.TINYINT || typeCode == Types.BIT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent(config.getParentPackage()) // 设置父包名
                                // 设置entity生成路径
                                .entity("model.entity")
                                // 设置mapperXml生成路径
                                .pathInfo(Collections.singletonMap(OutputFile.xml, config.getMapperDir()))
                )
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames) // 设置需要生成的表名
                            // Entity配置
                            .entityBuilder()
                            .enableLombok() // 开启Lombok注解
                            .disableSerialVersionUID() // 禁用SerialVersionUID
                            .superClass(IdEntity.class) // 默认继承IdEntity
                            .formatFileName("%sEntity")
                            // Mapper配置
                            .mapperBuilder()
                            .mapperAnnotation(Mapper.class)
                            // Service 配置
                            .serviceBuilder()
                            .convertServiceFileName((entityName -> entityName + ConstVal.SERVICE)) // Service接口类命名
                            // Controller 配置
                            .controllerBuilder()
                            .enableRestStyle(); // 开启生成@RestController控制器
                    // 是否开启文件覆盖操作
                    if (config.isEnableFileOverride()) {
                        builder.entityBuilder().enableFileOverride()
                                .mapperBuilder().enableFileOverride()
                                .serviceBuilder().enableFileOverride()
                                .controllerBuilder().enableFileOverride();
                    }
                })
                .templateConfig(builder ->
                        builder.disable()
                                .entity("/templates/entity.java")
                                .mapper("/templates/mapper.java")
                                .xml("templates/mapper.xml")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .controller("/templates/controller.java")
                )
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, stringObjectMap) -> {
                        // 是否启用WebModule
                        stringObjectMap.put("enabledWebModule", config.isEnabledWebModule());
                        // 实际数据表name（去除服务前缀）
                        String tableName = tableInfo.getName();
                        String[] names = tableName.split(ConstVal.UNDERLINE);
                        String actualTableName;
                        if (names.length > 1) {
                            actualTableName = tableName.substring(tableName.indexOf("_") + 1);
                            actualTableName = toCamelCase(actualTableName);
                        } else {
                            actualTableName = tableName;
                        }
                        stringObjectMap.put("actualTableName", actualTableName);
                        // 实际EntityName和DaoName
                        String actualEntityName = formatEntityName(tableInfo.getEntityName());
                        stringObjectMap.put("actualEntityName", actualEntityName);
                        stringObjectMap.put("daoName", actualEntityName + "Dao");
                        stringObjectMap.put("addDtoName", actualEntityName + "AddDto");
                        stringObjectMap.put("updateDtoName", actualEntityName + "UpdateDto");
                        stringObjectMap.put("detailVoName", actualEntityName + "DetailVo");
                    });
                    CustomFile.Builder daoBuilder = new CustomFile.Builder()
                            .fileName("Dao.java")
                            .templatePath("/templates/dao.java.ftl")
                            .formatNameFunction(tableInfo -> formatEntityName(tableInfo.getEntityName()))
                            .packageName("dao");
                    if (config.isEnableFileOverride()) {
                        daoBuilder.enableFileOverride();
                    }
                    builder.customFile(daoBuilder.build());
                    if (config.isEnabledWebModule()) {
                        CustomFile.Builder addDtoBuilder = new CustomFile.Builder()
                                .fileName("AddDto.java")
                                .templatePath("/templates/addDto.java.ftl")
                                .formatNameFunction(tableInfo -> formatEntityName(tableInfo.getEntityName()))
                                .packageName("model.dto");
                        CustomFile.Builder updateDtoBuilder = new CustomFile.Builder()
                                .fileName("UpdateDto.java")
                                .templatePath("/templates/updateDto.java.ftl")
                                .formatNameFunction(tableInfo -> formatEntityName(tableInfo.getEntityName()))
                                .packageName("model.dto");
                        CustomFile.Builder detailVoBuilder = new CustomFile.Builder()
                                .fileName("DetailVo.java")
                                .templatePath("/templates/detailVo.java.ftl")
                                .formatNameFunction(tableInfo -> formatEntityName(tableInfo.getEntityName()))
                                .packageName("model.vo");
                        if (config.isEnableFileOverride()) {
                            addDtoBuilder.enableFileOverride();
                            updateDtoBuilder.enableFileOverride();
                            detailVoBuilder.enableFileOverride();
                        }
                        builder.customFile(addDtoBuilder.build())
                                .customFile(updateDtoBuilder.build())
                                .customFile(detailVoBuilder.build());
                    }
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

        System.out.println("所有文件生成完成!");
    }

    private static String formatEntityName(String entityName) {
        // Entity 固定6位
        return entityName.substring(0, entityName.length() - 6);
    }

    /**
     * _转小写驼峰式命名
     *
     * @param underscoreStr _格式字符串
     * @return 小写驼峰式命名
     */
    public static String toCamelCase(String underscoreStr) {
        // 将字符串全部转换为小写，然后按下划线分割成数组
        String[] parts = underscoreStr.toLowerCase().split(ConstVal.UNDERLINE);
        // 使用StringBuilder来构建最终的驼峰字符串
        StringBuilder camelCaseStr = new StringBuilder(parts[0]);  // 首部分不变

        // 处理后续部分，首字母大写
        for (int i = 1; i < parts.length; i++) {
            camelCaseStr.append(parts[i].substring(0, 1).toUpperCase()) // 首字母大写
                    .append(parts[i].substring(1)); // 拼接剩余部分
        }

        return camelCaseStr.toString();
    }
}
