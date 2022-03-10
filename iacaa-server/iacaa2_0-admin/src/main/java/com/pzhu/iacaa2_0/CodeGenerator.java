package com.pzhu.iacaa2_0;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * -mp代码生成工具类，使用freemarker引擎
 * -使用前可以进行的修改
 *      gc.setAuthor  设置作者，会出现在类前
 *      gc.setOutputDir 设置输出路径，默认为 D:\
 *      pc.setParent 设置输出的包名(也即文件夹名)
 *      DataSourceConfig 设置数据源
 * -实体类使用lombok,swagger2，驼峰命名，去除is_前缀
 * -controller 使用rest风格
 * -dao.xml生成resultMap和baseColumnList
 * @author gcl
 * @create 2019-09-17 14:51
 */
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //作者
        gc.setAuthor("ZhaoZezhong");
        //路径,默认为 D:\
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/iacaa-server/iacaa2_0-admin/src/main/java");
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML column List
        gc.setBaseColumnList(true);
        //使用swagger2
//        gc.setSwagger2(true);
        //设置主键生成策略
        gc.setIdType(IdType.AUTO);
        mpg.setGlobalConfig(gc);


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://localhost:3306/iacaa20?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //实体类使用lombok
        strategy.setEntityLombokModel(true);
        //rest风格controller
        strategy.setRestControllerStyle(true);
        //is_xxx 去除is_ 前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(false);
        //去掉表名前缀
        strategy.setTablePrefix("t_");

        //设置继承基类
        strategy.setSuperEntityClass("com.pzhu.iacaa2_0.base.BaseEntity");
        //字段注释
        strategy.setEntityTableFieldAnnotationEnable(true);
        //数据表命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据表列生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        mpg.setStrategy(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.pzhu.iacaa2_0");
        mpg.setPackageInfo(pc);
        // 执行生成
        mpg.execute();
    }

}
