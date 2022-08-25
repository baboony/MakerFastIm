package com.maker.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 代码生成器
 */
public class ModelGenerateCode {

    public static void main(String[] args) {
        //代码生成器
        AutoGenerator mpg = new AutoGenerator();
        //全局配置
        GlobalConfig gc = new GlobalConfig();
        //获取当前目录
        String projectPath = System.getProperty("user.dir");
        //输出到当前目录下的/src/main/java
        gc.setOutputDir(projectPath + "/src/main/java");
        //设置作者
        gc.setAuthor("王俊程");
        //是否打开文件管理器
        gc.setOpen(false);
        //生成Swagger注解
        gc.setSwagger2(false);
        //是否覆盖
        gc.setFileOverride(true);
        //去除Service的I前缀
        gc.setServiceName("%sService");
        //id自动递增
        gc.setIdType(IdType.ASSIGN_ID);
        //设置日期类型
        gc.setDateType(DateType.ONLY_DATE);
        //装入配置
        mpg.setGlobalConfig(gc);

        /**
         * 配置数据源
         */

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/fast-im?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&nullNamePatternMatchesAll=true&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("admin");
        dsc.setDbType(DbType.MYSQL);
        //装入数据源配置
        mpg.setDataSource(dsc);

        /*
         * 包的配置
         * */
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("maker");
        pc.setParent("com");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);


        /*
         * 策略配置
         * */
        StrategyConfig strategy = new StrategyConfig();
        //需要生成的表名，可以多个
        strategy.setInclude("t_chat_message");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //自动生成lombok
        strategy.setEntityLombokModel(true);
        //逻辑字段删除
        strategy.setLogicDeleteFieldName("deleted");
        //乐观锁
        strategy.setVersionFieldName("version");
        //开启驼峰命名
        strategy.setRestControllerStyle(true);
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        mpg.setDataSource(dsc);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        //执行操作
        mpg.execute();

    }
}
