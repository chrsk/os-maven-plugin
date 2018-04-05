package org.opensaga.plugin.builder.meta.parser.types;

import java.util.Arrays;
import java.util.List;

import org.opensaga.plugin.builder.meta.parser.HandlerConstants;

public class TestExternalDomainTypeModelHandler
    extends TestDomainTypeModelHandler
{

    public TestExternalDomainTypeModelHandler(List<String> excludedModels)
    {
        super(excludedModels);
    }
    
    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("external-relational-domain-type");
    }
    
    @Override
    public String getId()
    {
        return HandlerConstants.TEST_EXTERNAL_DOMAIN_TYPE_MODEL_HANDLER;
    }
   
}
