package com.like;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @ProjectName: blog
 * @Author: LiuJiawei
 * @CreateDate: 2019/5/15 12:21
 * @UpdateDate: 2019/5/15 12:21
 */
@Slf4j
public class CodeGenerator {

    public static String url = "jdbc:mysql://localhost:3306/like_boot?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    public static String username = "root";
    public static String password = "123456";
    private static String packageName = "com.like";
    //代码生成路径
    private static String outDir = "";

    public static void main(String[] args) {
        try {
            outDir = initProjectRootPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //user -> UserService, 设置成true: user -> IUserService
        boolean serviceNameStartWithI = false;
        //生成代码
        generateByTables(serviceNameStartWithI, packageName, "",
                "sys_role,sys_role_dept,sys_role_menu,sys_user,sys_user_role");
    }
    //基础包名
    private static String controller = "rest";
    private static String entity = "entity";
    private static String service = "service";
    private static String impl = "service.impl";
    private static String mapper = "mapper";
    private static String xml = "mapper.xml";
    private static String vo = "vo";
    //是否覆盖包
    private static boolean isOverController = true;
    private static boolean isOverEntity = true;
    private static boolean isOverService = true;
    private static boolean isOverServiceImpl = true;
    private static boolean isOverMapper = true;
    private static boolean isOverXml = true;
    //分割符
    private static String separator = "/";
    //模块模板
    private static String controllerVM = "/templates/controller.java.vm";
    private static String entityVM = "/templates/entity.java.vm";
    private static String serviceVM = "/templates/service.java.vm";
    private static String serviceImplVM = "/templates/serviceImpl.java.vm";
    private static String mapperVM = "/templates/mapper.java.vm";
    private static String xmlVM = "/templates/mapper.xml.vm";

    //包所在文件夹名称
    private static String[] baseDir = {entity, mapper, service, impl, controller, vo};


    public static String initProjectRootPath() throws IOException {
        // 获取工程路径
        File projectPath = new DefaultResourceLoader().getResource("").getFile();
        while (!new File(projectPath.getPath() + separator + "src" + separator + "main").exists()) {
            projectPath = projectPath.getParentFile();
        }
        log.info("Project Path: " + projectPath);
        // Java文件路径
        String javaPath = projectPath + "/src/main/java";

        log.info("Java Path: " + javaPath);
        return javaPath;
    }

    private static void generateByTables(boolean serviceNameStartWithI, String packageName, String tablePrefix, String... tableNames) {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setTypeConvert(null)
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername(username)
                .setPassword(password)
                .setUrl(url);

        //策略设置
        StrategyConfig strategyConfig = new StrategyConfig();

        strategyConfig.setCapitalMode(false)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
//                .entityTableFieldAnnotationEnable(false)
                .setNaming(NamingStrategy.underline_to_camel)
                //设置controller超类
                .setSuperControllerClass("com.like.common.basic.controller.BaseController")
                .setSuperServiceClass("com.like.common.basic.service.IBaseBusinessService")
                .setSuperServiceImplClass("com.like.common.basic.service.impl.BaseBusinessService")
                .setSuperEntityClass("com.like.common.entity.BaseEntity")
                .setSuperEntityColumns("id", "create_time", "update_time", "creator", "updater", "sort", "del_flag")
                //设置entity超类
                //修改替换成需要的表名，多个表名传数组
                .setInclude(tableNames)
                //去除表前缀
                .setTablePrefix(tablePrefix);

        //全局设置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)
                .setAuthor("like")
                .setOutputDir(outDir)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setFileOverride(true)
                .setEnableCache(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                //开启swagger
                .setSwagger2(true)
        ;

        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }

        //跟包相关的配置项
        PackageConfig pcf = initPackage();
        TemplateConfig tc = initTemplateConfig(packageName);

//        log.info("Project tc: " + tc);
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(pcf)
                .setTemplate(tc)
                .setCfg(customerConfig());
                autoGenerator.execute();

    }
    /**
     * 初始化包目录配置
     *
     * @return
     */
    private static PackageConfig initPackage() {
        PackageConfig customPackageConfig = new PackageConfig();


        customPackageConfig.setParent(packageName)
                .setController(controller)
                .setEntity(entity)
                .setService(service)
                .setServiceImpl(impl)
                .setMapper(mapper)
                .setXml(xml);
        return customPackageConfig;

    }
    /**
     * 根据需要，修改哪些包下面的 要覆盖还是不覆盖
     *
     * @param packageName
     */

    private static TemplateConfig initTemplateConfig(String packageName) {
        //模板路径配置项
        TemplateConfig tc = new TemplateConfig();
        //初始化所有的模板
        initVM(tc);
        //遍历每个包下面的文件
        for (String tmp : baseDir) {
            //找到文件
            File file = new File(Paths.get(outDir, String.join("/", packageName.split("\\.")), tmp).toString());
            //文件列表
            String[] list = file.list();
            //如果文件存在，读取配置是否覆盖
            if (list != null && list.length > 0) {
                //Controller
                if (!isOverController) {
                    tc.setController(null);
                }
                //Entity
                if (!isOverEntity) {
                    tc.setEntity(null);
                }
                //Service
                if (!isOverService) {
                    tc.setService(null);
                }
                //ServiceImpl
                if (!isOverServiceImpl) {
                    tc.setServiceImpl(null);
                }
                //Mapper
                if (!isOverMapper) {
                    tc.setMapper(null);
                }
                //Xml
                if (!isOverXml) {
                    tc.setXml(null);
                }
            }
        }
        return tc;
    }
    /**
     * 自定义生成文件
     */
    private static InjectionConfig customerConfig() {
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
                Map map=new HashMap();
                map.put("packageName",packageName);
                this.setMap(map);
            }
        }; List<FileOutConfig> files = new ArrayList<>();
        files.add(new FileOutConfig("/templates/entityVO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String dir=String.join("/", packageName.split("\\."));
                String expand = outDir + "/" +dir+ "/vo";
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getEntityName() + "VO");
                return entityFile;
            }
        });

        config.setFileOutConfigList(files);
        return config;
    }
    /**
     * 初始化模板
     *
     * @param tc
     */
    private static void initVM(TemplateConfig tc) {
        if (stringIsNotNull(controllerVM)) {
            tc.setController(controllerVM);
        }

        if (stringIsNotNull(entityVM)) {
            tc.setEntity(entityVM);
        }

        if (stringIsNotNull(serviceVM)) {
            tc.setService(serviceVM);
        }

        if (stringIsNotNull(serviceImplVM)) {
            tc.setServiceImpl(serviceImplVM);
        }

        if (stringIsNotNull(mapperVM)) {
            tc.setMapper(mapperVM);
        }

        if (stringIsNotNull(xmlVM)) {
            tc.setXml(xmlVM);
        }

    }
    /**
     * 简单判断字符串是不是为空
     *
     * @param s
     * @return
     */

    private static boolean stringIsNotNull(String s) {
        if (null != s && !s.equals("") && s.length() > 0 && s.trim().length() > 0) {
            return true;
        }
        return false;
    }



}