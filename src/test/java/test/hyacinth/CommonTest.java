package test.hyacinth;

import com.github.hyacinth.Db;
import com.github.hyacinth.generator.Generator;
import com.github.hyacinth.tools.PathTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.hyacinth.sql.Sqls;

import java.io.File;

/**
 * Created by LY on 6/27/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-common.xml"})
public class CommonTest {



    @Test
    public void gernerator(){
        // base model 所使用的包名
        String sqlsPackageName = "test.hyacinth.sql";
        // base model 文件保存路径
        String outputDir = new File(PathTools.getWebRootPath()).getAbsolutePath();
        System.out.println(outputDir);
        String sqlsOutputDir = outputDir + "/src/test/java/test/hyacinth/sql";
        Generator generator = new Generator(sqlsPackageName, sqlsOutputDir);
        generator.sqlsGenerate();
    }

    @Test
    public void test(){

        long end, start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Db.find(Sqls.User_selectUser);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(Db.find(Sqls.User_selectUser).size());
    }
}
