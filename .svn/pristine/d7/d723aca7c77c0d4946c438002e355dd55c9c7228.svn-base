package org.opensaga.plugin.builder.meta.parser;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;

public interface ModelContextDependentPostProcessor
{

    int handleModel(HandlerContext context, JavaSourceGenerator sourceGenerator) throws MetaGenerationFailedException;
    
    HandlerType getType();
   
}
