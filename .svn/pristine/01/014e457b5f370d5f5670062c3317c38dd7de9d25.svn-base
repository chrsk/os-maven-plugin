package org.opensaga.plugin.builder.meta.parser.types;

import java.util.Arrays;
import java.util.List;

import org.opensaga.plugin.builder.meta.parser.HandlerConstants;

public class ExternalDomainTypeModelHandler extends DomainTypeModelHandler
{

    public ExternalDomainTypeModelHandler(List<String> excludedModels)
    {
        super(excludedModels);
    }
    
    
    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("external-relational-domain-type");
    }
    
    @Override
    protected String getSuperClass()
    {
        return "AbstractExternalMetaModelDomainType";
    }
    
    @Override
    public String getId()
    {
        return HandlerConstants.EXTERNAL_DOMAIN_TYPE_MODEL_HANDLER;
    }

}
