package org.opensaga.plugin.builder.meta.parser.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.RelationMetaModel;
import org.opensaga.plugin.builder.meta.parser.AbstractModelHandler;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.xmltool.CallBack;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLDocumentException;
import com.mycila.xmltool.XMLTag;

public class RelationModelHandler
    extends AbstractModelHandler<RelationMetaModel>
{

    private static final Logger log = LoggerFactory.getLogger(RelationModelHandler.class);

    private final List<String> excludedModels;


    public RelationModelHandler(List<String> excludedModels)
    {
        this.excludedModels = excludedModels;

    }

    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("relation-set");
    }


    @Override
    protected List<MetaModel> parseModelImpl(File modelFile) throws XMLDocumentException
    {
        List<MetaModel> relationMetaModels = new ArrayList<MetaModel>();
        CreateRelationMetaModelClosure createRelationMetaModel = new CreateRelationMetaModelClosure(modelFile, relationMetaModels);

        try
        {
            XMLTag relationModel = XMLDoc.from(modelFile, true);
            relationModel.forEach(createRelationMetaModel, "//relation");
        }
        catch (XMLDocumentException e)
        {
            log.error("Exception", e);
            // Do nothing here, we aren't sure about the underlying XML file
            // so we have to guess the structure and may fail.
        }

        return relationMetaModels;
    }
    
    private static class CreateRelationMetaModelClosure
        implements CallBack
    {
        private final List<MetaModel> relationMetaModels;
        
        private final File modelFile;


        public CreateRelationMetaModelClosure(File modelFile, List<MetaModel> relationMetaModels)
        {
            this.modelFile = modelFile;
            this.relationMetaModels = relationMetaModels;
        }


        @Override
        public void execute(XMLTag relationModel)
        {

            RelationMetaModel relationMetaModel = new RelationMetaModel();
            relationMetaModel.setId(relationModel.findAttribute("id"));
            relationMetaModel.setLocation(modelFile.getAbsolutePath());
            relationMetaModel.setName(relationModel.findAttribute("name"));

            relationMetaModels.add(relationMetaModel);
        }
    }


    @Override
    protected List<String> getExcludedModels()
    {
        return excludedModels;
    }


    @Override
    public String getId()
    {
        return HandlerConstants.RELATION_MODEL_HANDLER;
    }

    @Override
    public void mergeModel(MetaModel metaModel, MetaModel matchingMetaModel)
    {
        throw new NotImplementedException("Please implement the meta model merge for relation models");
    }
}
