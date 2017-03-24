package com.github.hyacinth.generator;

import com.github.hyacinth.dialect.Dialect;

import javax.sql.DataSource;
import java.util.List;

/**
 * 生成器
 * 1：生成时会强制覆盖 Base model、DataDictionary，建议不要修改三类文件，在数据库有变化重新生成一次便可
 * 2：生成  Model 不会覆盖已经存在的文件，Model 通常会被人为修改和维护
 * 3：Sqls.jar 文件在对sql markdown修改的时候重新生成一次即可，建议不要人为修改Sqls.java文件
 * 4：DataDictionary 文件默认不会生成。只有在设置 setGenerateDataDictionary(true)后，会在生成 Model文件的同时生成
 * 5：可以通过继承 BaseModelGenerator、ModelGenerator、MappingKitGenerator、DataDictionaryGenerator
 * 来创建自定义生成器，然后使用 Generator 的 setter 方法指定自定义生成器来生成
 * 6：生成模板文字属性全部为 protected 可见性，方便自定义 Generator 生成符合。。。。
 */
public class Generator {

    protected MetaBuilder metaBuilder;
    protected BaseModelGenerator baseModelGenerator;
    protected ModelGenerator modelGenerator;
    protected DataDictionaryGenerator dataDictionaryGenerator;
    protected SqlsGenerator sqlsGenerator;
    protected boolean generateDataDictionary = false;

    /**
     * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     *
     * @param dataSource           数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir   base mode 输出目录
     * @param modelPackageName     model 包名
     * @param modelOutputDir       model 输出目录
     */
    public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir), new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir));
    }

    /**
     * 构造 Generator，只生成 baseModel
     *
     * @param dataSource           数据源
     * @param baseModelPackageName base model 包名
     * @param baseModelOutputDir   base mode 输出目录
     */
    public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
        this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir));
    }

    public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        if (baseModelGenerator == null) {
            throw new IllegalArgumentException("baseModelGenerator can not be null.");
        }

        this.metaBuilder = new MetaBuilder(dataSource);
        this.baseModelGenerator = baseModelGenerator;
        this.modelGenerator = null;
        this.dataDictionaryGenerator = null;
    }

    /**
     * 使用指定 BaseModelGenerator、ModelGenerator 构造 Generator
     * 生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
     */
    public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        if (baseModelGenerator == null) {
            throw new IllegalArgumentException("baseModelGenerator can not be null.");
        }
        if (modelGenerator == null) {
            throw new IllegalArgumentException("modelGenerator can not be null.");
        }

        this.metaBuilder = new MetaBuilder(dataSource);
        this.baseModelGenerator = baseModelGenerator;
        this.modelGenerator = modelGenerator;
        this.dataDictionaryGenerator = new DataDictionaryGenerator(dataSource, modelGenerator.modelOutputDir);
    }

    /**
     * 构造Sqls类生成器
     *
     * @param sqlsPackageName Sqls类名
     * @param sqlsOutputDir   Sqls类输出路径
     * @param sqlsClassName   Sqls类自定义类名
     */
    public Generator(String sqlsPackageName, String sqlsOutputDir, String sqlsClassName) {
        this.sqlsGenerator = new SqlsGenerator(sqlsPackageName, sqlsOutputDir, sqlsClassName);
    }

    public Generator(String sqlsPackageName, String sqlsOutputDir) {
        this.sqlsGenerator = new SqlsGenerator(sqlsPackageName, sqlsOutputDir);
    }

    /**
     * 设置 MetaBuilder，便于扩展自定义 MetaBuilder
     */
    public void setMetaBuilder(MetaBuilder metaBuilder) {
        if (metaBuilder != null) {
            this.metaBuilder = metaBuilder;
        }
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        this.metaBuilder.setTypeMapping(typeMapping);
    }

    /**
     * 设置 DataDictionaryGenerator，便于扩展自定义 DataDictionaryGenerator
     */
    public void setDataDictionaryGenerator(DataDictionaryGenerator dataDictionaryGenerator) {
        if (dataDictionaryGenerator != null) {
            this.dataDictionaryGenerator = dataDictionaryGenerator;
        }
    }

    /**
     * 设置数据库方言，默认为 MysqlDialect
     */
    public void setDialect(Dialect dialect) {
        metaBuilder.setDialect(dialect);
    }

    /**
     * 设置 BaseMode 是否生成链式 setter 方法
     */
    public void setGenerateChainSetter(boolean generateChainSetter) {
        baseModelGenerator.setGenerateChainSetter(generateChainSetter);
    }

    /**
     * 设置需要被移除的表名前缀，仅用于生成 modelName 与  baseModelName
     * 例如表名  "osc_account"，移除前缀 "osc_" 后变为 "account"
     */
    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
    }

    /**
     * 添加不需要处理的数据表
     */
    public void addExcludedTable(String... excludedTables) {
        metaBuilder.addExcludedTable(excludedTables);
    }

    /**
     * 设置是否在 Model 中生成 dao 对象，默认生成
     */
    public void setGenerateDaoInModel(boolean generateDaoInModel) {
        if (modelGenerator != null) {
            modelGenerator.setGenerateDaoInModel(generateDaoInModel);
        }
    }

    /**
     * 设置是否生成数据字典 Dictionary 文件，默认不生成
     */
    public void setGenerateDataDictionary(boolean generateDataDictionary) {
        this.generateDataDictionary = generateDataDictionary;
    }


    /**
     * 设置数据字典 DataDictionary 文件输出目录，默认与 modelOutputDir 相同
     */
    public void setDataDictionaryOutputDir(String dataDictionaryOutputDir) {
        if (this.dataDictionaryGenerator != null) {
            this.dataDictionaryGenerator.setDataDictionaryOutputDir(dataDictionaryOutputDir);
        }
    }

    /**
     * 设置数据字典 DataDictionary 文件输出目录，默认值为 "_DataDictionary.txt"
     */
    public void setDataDictionaryFileName(String dataDictionaryFileName) {
        if (dataDictionaryGenerator != null) {
            dataDictionaryGenerator.setDataDictionaryFileName(dataDictionaryFileName);
        }
    }

    public void modelGenerate() {
        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        metaBuilder.buildColumnMetasDetail(tableMetas);
        if (tableMetas.size() == 0) {
            System.out.println("TableMeta 数量为 0，不生成任何文件");
            return;
        }

        baseModelGenerator.generate(tableMetas);

        if (modelGenerator != null) {
            modelGenerator.generate(tableMetas);
        }

        if (dataDictionaryGenerator != null && generateDataDictionary) {
            dataDictionaryGenerator.generate(tableMetas);
        }

        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Model Generate complete in " + usedTime + " seconds.");
    }

    public void sqlsGenerate() {
        long start = System.currentTimeMillis();
        if(sqlsGenerator != null){
            sqlsGenerator.generate();
        }
        long usedTime = System.currentTimeMillis();
        System.out.println("Sqls Generate complete in " + usedTime + " seconds.");
    }
}



