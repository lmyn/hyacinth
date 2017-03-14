package com.github.hyacinth;

import com.github.hyacinth.dialect.MysqlDialect;
import com.github.hyacinth.generator.DataDictionaryGenerator;
import com.github.hyacinth.generator.Generator;
import com.github.hyacinth.monitor.FileAlterationListener;
import com.github.hyacinth.monitor.FileAlterationMonitor;
import com.github.hyacinth.monitor.FileAlterationObserver;
import com.github.hyacinth.tools.ClassTools;
import com.github.hyacinth.tools.PathTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/28
 * Time: 11:06
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-common.xml"})
public class HyacinthTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test(){
        System.out.println(dataSource.toString());
    }

    @Test
    public void test1() {
        String packageName = "com.github";
        // List<String> classNames = getClassName(packageName);
        Set<String> classNames = ClassTools.getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                System.out.println(className);
            }
        }
    }

    @Test
    public void test2(){
        // base model 所使用的包名
        String baseModelPackageName = "cn.wisestar.crm.commons.model.base";
        // base model 文件保存路径
        System.out.println(PathTools.getWebRootPath());
        String baseModelOutputDir = PathTools.getWebRootPath() + "/src/main/java/cn/wisestar/crm/commons/model/base";
        System.out.println(baseModelOutputDir);

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "cn.wisestar.crm.commons.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";

        // 创建生成器
        Generator gernerator = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 设置数据库方言
        gernerator.setDialect(new MysqlDialect());
        // 添加不需要生成的表名
        gernerator.addExcludedTable("");
        // 设置是否在 Model 中生成 dao 对象
        gernerator.setGenerateDaoInModel(true);
        // 设置是否生成字典文件
        gernerator.setGenerateDataDictionary(true);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        gernerator.setRemovedTableNamePrefixes("sys_","cus_");
        // 生成
        gernerator.generate();
    }
}
