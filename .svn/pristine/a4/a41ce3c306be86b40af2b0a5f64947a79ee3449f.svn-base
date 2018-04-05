package org.opensaga.plugin.builder.meta.parser.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.HandlerTypeBasedJavaNamingStrategy;
import org.opensaga.plugin.builder.meta.parser.ModelContextDependentPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessMetaModelPostProcessor implements ModelContextDependentPostProcessor
{

    private static final Logger log = LoggerFactory.getLogger(DomainMetaModelPostProcessor.class);
    
    private final String domainPrefix;
    
    public ProcessMetaModelPostProcessor(String domainPrefix)
    {
        this.domainPrefix = domainPrefix;
    }


    @Override
    public int handleModel(HandlerContext handlerContext, JavaSourceGenerator sourceGenerator) throws MetaGenerationFailedException
    {
        final String DOMAIN_MODEL_TEMPLATE = "templates/java-models/process-domain-meta-model.vtl";
        final String fileName = domainPrefix + "Processes.java";
        
        List<MetaModel> processList = handlerContext.getContextForHandlerWithId(HandlerConstants.PROCESS_MODEL_HANDLER);
        
        final HandlerTypeBasedJavaNamingStrategy namingStrategy = new HandlerTypeBasedJavaNamingStrategy(getType());
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("className", domainPrefix + "Processes");
        context.put("processList", processList);
        context.put("namingStrategy", namingStrategy);
        
        sourceGenerator.generateJavaSourceFile(DOMAIN_MODEL_TEMPLATE, fileName, "process", context);
        log.info("Generated process domain class '" + fileName + "' which provides access to all process meta models.");
        
        return 1;
    }
    
    
    
    @Override
    public HandlerType getType()
    {
        return HandlerType.TEST_DOMAIN;
    }
}
