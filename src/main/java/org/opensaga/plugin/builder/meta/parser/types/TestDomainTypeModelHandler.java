package org.opensaga.plugin.builder.meta.parser.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.model.DomainTypeMetaModel;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.HandlerTypeBasedJavaNamingStrategy;

public class TestDomainTypeModelHandler
    extends DomainTypeModelHandler
{

    public TestDomainTypeModelHandler(List<String> excludedModels)
    {
        super(excludedModels);
    }
    
    @Override
    public int handleModel(HandlerContext handlerContext, DomainTypeMetaModel metaModel, JavaSourceGenerator sourceGenerator)
        throws MetaGenerationFailedException
    {
        final String DOMAIN_TYPE_MODEL_TEMPLATE = "templates/java-models/test-domain-type-meta-model.vtl";

        final HandlerTypeBasedJavaNamingStrategy namingStrategy = new HandlerTypeBasedJavaNamingStrategy(getType());
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("superClassName", getSuperClass());
        context.put("namingStrategy", namingStrategy);

        String fileName = namingStrategy.getJavaClassNameForDomainTypeModel(metaModel) + JAVA_FILE_EXTENSION;
        sourceGenerator.generateJavaSourceFile(DOMAIN_TYPE_MODEL_TEMPLATE, fileName, "domain", context, metaModel);

        return 1;
    }
    
    @Override
    protected String getSuperClass()
    {
        return "AbstractDomainTypeTestMetaModel";
    }
    
    @Override
    public HandlerType getType()
    {
        return HandlerType.TEST_DOMAIN;
    }
    
    @Override
    public String getId()
    {
        return HandlerConstants.TEST_DOMAIN_TYPE_MODEL_HANDLER;
    }

}
