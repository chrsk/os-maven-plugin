package org.opensaga.plugin.builder.meta.parser;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;

public interface ModelHandler<M extends MetaModel>
{

    int handleModel(HandlerContext context, M model, JavaSourceGenerator sourceGenerator)
        throws MetaGenerationFailedException;

    HandlerType getType();

}
