package org.opensaga.plugin.builder.meta.generator;

import java.util.Map;

import org.opensaga.plugin.builder.meta.generator.model.MetaModel;

/**
 * Provides templating functionality which is optimized for Java source
 * generation. The {@code JavaSourceGenerator} creates a java source file in the
 * configured main package for the given meta model and specific context.
 * 
 * @author cklewes
 */
public interface JavaSourceGenerator
{

    /**
     * Creates one java source file in the passed sub package with the given
     * output file name. The given template file name will be used to create the
     * Java source file for the passed meta models. The java source file and the
     * sub package will be automatically created in the defined main package.
     * 
     * @param templateFileName The location of the template to use.
     * @param outputFileName The name of the java source output file. 
     * @param subPackage The sub package for this java class e.g. "domain"
     * @param specificContext The specific context when necessary.
     * @param metaModel The meta model which is the mandatory information for the the java source file.
     * 
     * @throws MetaGenerationFailedException
     */
    void generateJavaSourceFile(String templateFileName, String outputFileName, String subPackage,
        Map<String, Object> specificContext, MetaModel... metaModel)
        throws MetaGenerationFailedException;
}
