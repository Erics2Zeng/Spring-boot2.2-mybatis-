import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.DatabaseIntrospector;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Generate {

    //生成service ,controller
    private static final String AUTHOR = "CodeGenerator";//@author
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date
    public static final String PROJECT_PATH = System.getProperty("user.dir")+"/";//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/template";//模板位置
    private static String JAVA_PATH = ""; //java文件路径
//            "com.tongjie.accessgate";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）
    public static  String CODE_BASE_PACKAGE = "";
//            CODE_BASE_PACKAGE + ".service";//生成的Service所在包
    public static  String CODE_SERVICE_PACKAGE = "";
//            CODE_BASE_PACKAGE + ".common.entity";
    public static  String CODE_COMMON_REBO_PACKAGE = "";
//            SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static  String CODE_SERVICE_IMPL_PACKAGE = "";
//            CODE_BASE_PACKAGE + ".web";//生成的Controller所在包
    public static  String CODE_CONTROLLER_PACKAGE = "";




    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream configFile =
                Generate.class.getResourceAsStream("/geConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        Context context = config.getContext("MySqlContext");
        //生成service，controller
        JDBCConnectionConfiguration connectionConfig = context.getJdbcConnectionConfiguration();
        Connection connection = getConnection(context,connectionConfig);

        JavaTypeResolver javaTypeResolver = ObjectFactory
                .createJavaTypeResolver(context, warnings);
        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                context, connection.getMetaData(), javaTypeResolver, warnings);
        List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
        List<IntrospectedTable> introspectedTables = new ArrayList<>();
        if (tableConfigurations.size() == 1){
            introspectedTables.addAll(databaseIntrospector.introspectTables(tableConfigurations.get(0)));
        }

        List<String> tableNames = introspectedTables.stream().map(table -> table.getFullyQualifiedTable().getIntrospectedTableName())
                .collect(Collectors.toList());
        //处理生成路径
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
        String javaModelPackage = javaModelGeneratorConfiguration.getTargetPackage();
        CODE_BASE_PACKAGE = javaModelPackage.substring(0,javaModelPackage.lastIndexOf("."));
        JAVA_PATH = javaModelGeneratorConfiguration.getTargetProject() + File.separator;
        CODE_SERVICE_PACKAGE = CODE_BASE_PACKAGE+".service";
        CODE_COMMON_REBO_PACKAGE = CODE_BASE_PACKAGE+".common.utils";
        CODE_SERVICE_IMPL_PACKAGE = CODE_BASE_PACKAGE+".service.impl";
        CODE_CONTROLLER_PACKAGE = CODE_BASE_PACKAGE +".web";
        genCode(tableNames);

    }
    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     * @param tableNames 数据表名称...
     */
    public static void genCode(List<String> tableNames) throws SQLException, InterruptedException {
        for (String tableName : tableNames) {
//            genCodeByCustomModelName(tableName,lineToHump(tableName.replace("tb_","")));
            genCodeByCustomModelName(tableName,lineToHump(tableName));
        }
    }



    private static Connection getConnection(Context context,JDBCConnectionConfiguration jdbcConnectionConfiguration) throws SQLException {
        ConnectionFactory connectionFactory;
        if (jdbcConnectionConfiguration != null) {
            connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
        } else {
            connectionFactory = ObjectFactory.createConnectionFactory(context);
        }
        return connectionFactory.getConnection();
    }


    /**下划线转驼峰*/
    public static String lineToHump(String str){
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return  sb.substring(0,1).toUpperCase() + sb.substring(1);
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public static void genCodeByCustomModelName(String tableName, String modelName) throws InterruptedException, SQLException {
//        genModelAndMapper(tableName, modelName);
        genResponseBo();
        genService(tableName, modelName);
        genController(tableName, modelName);
    }

    public static void genResponseBo() {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("currentPackage", CODE_BASE_PACKAGE);

            File file = new File(PROJECT_PATH + JAVA_PATH +packageConvertPath(CODE_COMMON_REBO_PACKAGE)
                    +"ResponseBo.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("responsebo.ftl").process(data,
                    new FileWriter(file));
            System.out.println("ResponseBo.java 生成成功");

        } catch (Exception e) {
            throw new RuntimeException("生成ResponseBo.java 失败", e);
        }
    }

    public static void genService(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
//            data.put("basePackage", CODE_BASE_PACKAGE);
            data.put("currentPackage", CODE_BASE_PACKAGE);

            File file = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(CODE_SERVICE_PACKAGE)
                    //modelNameUpperCamel
                    + "PageService.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("page-service.ftl").process(data,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(CODE_SERVICE_IMPL_PACKAGE) + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public static void genController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
//            data.put("basePackage",CODE_BASE_PACKAGE);
            data.put("currentPackage", CODE_BASE_PACKAGE);

            File file = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(CODE_CONTROLLER_PACKAGE) + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
            cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}