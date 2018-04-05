package org.opensaga.plugin.builder.meta.parser.types;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.model.DomainTypeMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.PropertyMetaModel;
import org.opensaga.plugin.builder.meta.parser.AbstractModelHandler;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.HandlerTypeBasedJavaNamingStrategy;
import org.opensaga.plugin.builder.meta.parser.ModelHandler;
import org.opensaga.plugin.builder.meta.parser.OpenSAGATypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.mycila.xmltool.CallBack;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLDocumentException;
import com.mycila.xmltool.XMLTag;

public class DomainTypeModelHandler
    extends AbstractModelHandler<DomainTypeMetaModel> implements ModelHandler<DomainTypeMetaModel>
{

    private static final Logger log = LoggerFactory.getLogger(DomainTypeModelHandler.class);

    private final List<String> excludedModels;


    public DomainTypeModelHandler(List<String> excludedModels)
    {
        this.excludedModels = excludedModels;
    }


    @Override
    protected List<MetaModel> parseModelImpl(File modelFile) throws XMLDocumentException
    {
        DomainTypeMetaModel domainTypeModel = new DomainTypeMetaModel();

        XMLTag domainType = XMLDoc.from(modelFile, true);

        domainTypeModel.setId(domainType.findAttribute("id"));
        domainTypeModel.setName(domainType.findAttribute("name"));
        domainTypeModel.setLocation(modelFile.getAbsolutePath());

        try
        {
            CollectPropertiesClosure collectProperties = new CollectPropertiesClosure(domainTypeModel);

            domainType.forEach(collectProperties, "//property-set/domain-type-property-set/domain-type-property");
            domainType.forEach(collectProperties, "//property-set/formula-property-set/formula-property");
            domainType.forEach(collectProperties, "//property-set/enum-property-set/enum-property");

        }
        catch (XMLDocumentException e)
        {
            log.error("Exception", e);
            // Do nothing here, we aren't sure about the underlying XML file
            // so we have to guess the structure and may fail.
        }

        return Arrays.asList((MetaModel) domainTypeModel);
    }

    protected final class CollectPropertiesClosure
        implements CallBack
    {

        private final DomainTypeMetaModel domainTypeModel;


        public CollectPropertiesClosure(DomainTypeMetaModel domainTypeModel)
        {
            this.domainTypeModel = domainTypeModel;
        }


        @Override
        public void execute(XMLTag property)
        {
            String tagName = property.getCurrentTag().getNodeName();

            PropertyMetaModel propertyModel = new PropertyMetaModel();

            propertyModel.setId(property.findAttribute("id"));
            propertyModel.setName(property.findAttribute("name"));
            propertyModel.setLocation(domainTypeModel.getLocation());

            if ("enum-property".equals(tagName))
            {
                propertyModel.setPropertyRef(property.findAttribute("property-ref"));
            }
            else
            {
                propertyModel.setType(OpenSAGATypeMapper.getType(property.findAttribute("type")));
            }

            domainTypeModel.addMetaPropertyModels(propertyModel);
        }
    }


    @Override
    public int handleModel(HandlerContext handlerContext, DomainTypeMetaModel metaModel, JavaSourceGenerator sourceGenerator)
        throws MetaGenerationFailedException
    {
        final String DOMAIN_TYPE_MODEL_TEMPLATE = "templates/java-models/domain-type-meta-model.vtl";

        final HandlerTypeBasedJavaNamingStrategy namingStrategy = new HandlerTypeBasedJavaNamingStrategy(getType());
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("superClassName", getSuperClass());
        context.put("namingStrategy", namingStrategy);

        String fileName = namingStrategy.getJavaClassNameForDomainTypeModel(metaModel) + JAVA_FILE_EXTENSION;
        sourceGenerator.generateJavaSourceFile(DOMAIN_TYPE_MODEL_TEMPLATE, fileName, "domain", context, metaModel);

        return 1;
    }


    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("domain-type", "virtual-domain-type", "constant-domain-type");
    }


    @Override
    protected List<String> getExcludedModels()
    {
        return excludedModels;
    }


    protected String getSuperClass()
    {
        return "AbstractMetaModelDomainType";
    }


    @Override
    public String getId()
    {
        return HandlerConstants.DOMAIN_TYPE_MODEL_HANDLER;
    }


    @Override
    public void mergeModel(MetaModel metaModel, MetaModel matchingMetaModel)
    {
        Assert.isInstanceOf(DomainTypeMetaModel.class, metaModel);
        Assert.isInstanceOf(DomainTypeMetaModel.class, matchingMetaModel);
        
        List<PropertyMetaModel> metaPropertyModels = ((DomainTypeMetaModel) matchingMetaModel).getMetaPropertyModels();

        for (PropertyMetaModel propertyMetaModel : metaPropertyModels)
        {
            ((DomainTypeMetaModel) metaModel).addMetaPropertyModels(propertyMetaModel);
        }
    }
    
    
    @Override
    public HandlerType getType()
    {
        return HandlerType.SOURCE_DOMAIN;
    }
}
