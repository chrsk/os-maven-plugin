package org.opensaga.plugin.builder.meta.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.generator.model.EditableMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.xmltool.XMLDocumentException;

public abstract class AbstractModelHandler<M extends MetaModel>
    implements TypeBasedModelParser
{

    private static final Logger log = LoggerFactory.getLogger(AbstractModelHandler.class);

    /**
     * Defines the file extension for the generated Java files.
     */
    protected static final String JAVA_FILE_EXTENSION = ".java";

    private Map<String, Object> configuration = new HashMap<String, Object>();


    /**
     * Filters the list of meta models. Models can be excluded from the
     * generation. The passed list is not modified, a new list will be returned.
     * 
     * @param metaModelList A list of meta models to filter.
     * @see #isModelExcludedById(MetaModel, List)
     * @return A new filtered list of the given list.
     */
    protected List<MetaModel> filterModelList(List<MetaModel> metaModelList)
    {
        List<MetaModel> filteredList = new ArrayList<MetaModel>();

        for (MetaModel modelInfo : metaModelList)
        {
            if (!isModelExcludedById(modelInfo, getExcludedModels()))
            {
                filteredList.add(modelInfo);
            }
            else
            {
                log.info("Excluded model with id '{}'", modelInfo.getId());
            }
        }

        return filteredList;
    }


    protected List<String> getExcludedModels()
    {
        return Collections.emptyList();
    }


    /**
     * Verifies if the given meta model was excluded by ID. The list of IDs is
     * retrieved from the given excluded IDs list.
     * 
     * @param modelInfo The meta model to check for exclusion
     * @param excludedIds The list of IDs which should be excluded
     * 
     * @return Either {@code true} when the model is excluded, or {@code false}.
     */
    private boolean isModelExcludedById(MetaModel modelInfo, List<String> excludedIds)
    {
        for (String excludedId : excludedIds)
        {
            if (excludedId.equals(modelInfo.getId()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Defines the effective implementation of the specific parsing.
     * 
     * @param modelFile The XML file of the model.
     * @return  A list of parsed meta models.
     * 
     * @throws XMLDocumentException
     */
    protected abstract List<MetaModel> parseModelImpl(File modelFile) throws XMLDocumentException;


    @Override
    public final List<MetaModel> parseModel(File modelFile) throws XmlParserFailureException
    {
        try
        {
            List<MetaModel> modelInfos = parseModelImpl(modelFile);
            List<MetaModel> filteredModels = filterModelList(modelInfos);
            
            for (MetaModel metaModel : filteredModels)
            {
                if(metaModel instanceof EditableMetaModel)
                {
                    ((EditableMetaModel) metaModel).setParser(this);
                }
            }
       
            return filteredModels;
        }
        catch (XMLDocumentException e)
        {
            throw new XmlParserFailureException(e);
        }
    }


    @Override
    public boolean supportsType(String type)
    {
        List<String> supportedTypes = getSupportedTypes();

        for (String supportedType : supportedTypes)
        {
            if (supportedType.equals(type))
            {
                return true;
            }
        }

        return false;
    }


    @Override
    public void setConfiguration(Map<String, Object> configuration)
    {
        this.configuration = configuration;
    }


    protected Map<String, Object> getConfiguration()
    {
        return configuration;
    }

}
