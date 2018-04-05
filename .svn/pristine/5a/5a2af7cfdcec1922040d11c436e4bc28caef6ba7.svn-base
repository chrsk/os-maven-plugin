package org.opensaga.plugin.builder.meta.parser;

import org.opensaga.plugin.builder.meta.generator.model.DomainTypeMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ProcessMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.StartStateMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewStateMetaModel;

/**
 * Defines a global naming strategies optimized for Java conventions. The naming
 * strategy is capable to generate optimized namings for the given meta models.
 * 
 * @author cklewes
 */
public interface ModelNamingStrategy
{

    String getJavaConstantNameForMetaModel(MetaModel propertyMetaModel);


    String getJavaClassNameForDomainTypeModel(DomainTypeMetaModel domainTypeMetaModel);


    String getJavaConstantNameForDomainTypeModel(DomainTypeMetaModel domainTypeMetaModel);


    String getJavaClassNameForProcessModel(ProcessMetaModel processMetaModel);


    String getJavaConstantNameForProcessModel(ProcessMetaModel processMetaModel);


    String getJavaConstantNameForStartState(StartStateMetaModel startStateMetaModel);


    String getJavaClassNameForViewModel(ViewMetaModel metaModel);


    String getJavaConstantNameForViewState(ViewStateMetaModel metaModel);


    String getJavaClassNameForViewState(ViewStateMetaModel metaModel);


    String getJavaClassNameForViewWithProcess(ViewMetaModel viewMetaModel, ProcessMetaModel processMetaModel);
}
