package org.opensaga.plugin.builder.meta.parser.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ProcessMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.StartStateMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewStateMetaModel;
import org.opensaga.plugin.builder.meta.parser.AbstractModelHandler;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.HandlerTypeBasedJavaNamingStrategy;
import org.opensaga.plugin.builder.meta.parser.ModelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.mycila.xmltool.CallBack;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLDocumentException;
import com.mycila.xmltool.XMLTag;

public class ProcessModelHandler
    extends AbstractModelHandler<ProcessMetaModel> implements ModelHandler<ProcessMetaModel>
{

    private static final Logger log = LoggerFactory.getLogger(ProcessModelHandler.class);


    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("process", "subprocess");
    }


    @Override
    public String getId()
    {
        return HandlerConstants.PROCESS_MODEL_HANDLER;
    }


    @Override
    public void mergeModel(MetaModel metaModel, MetaModel matchingMetaModel)
    {
        Assert.isInstanceOf(ProcessMetaModel.class, metaModel);
        Assert.isInstanceOf(ProcessMetaModel.class, matchingMetaModel);

        ProcessMetaModel processMetaModel = (ProcessMetaModel) metaModel;
        ProcessMetaModel matchingProcessMetaModel = (ProcessMetaModel) matchingMetaModel;
        
        for (StartStateMetaModel startStateMetaModel : matchingProcessMetaModel.getStartStateMetaModels())
        {
            if (!processMetaModel.containsStartState(startStateMetaModel.getId()))
            {
                processMetaModel.getStartStateMetaModels().add(startStateMetaModel);
            }
        }

        for (ViewStateMetaModel viewStateMetaModel : matchingProcessMetaModel.getViewStateMetaModels())
        {
            if (!processMetaModel.containsViewState(viewStateMetaModel.getId()))
            {
                processMetaModel.getViewStateMetaModels().add(viewStateMetaModel);
            }
        }
    }


    @Override
    public int handleModel(HandlerContext handlerContext, ProcessMetaModel metaModel, JavaSourceGenerator sourceGenerator)
        throws MetaGenerationFailedException
    {
        final String DOMAIN_TYPE_MODEL_TEMPLATE = "templates/java-models/process-meta-model.vtl";
        final HandlerTypeBasedJavaNamingStrategy namingStrategy = new HandlerTypeBasedJavaNamingStrategy(getType());
        
        filterViewStatesForNotExistentViewModels(handlerContext, metaModel);
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("namingStrategy", namingStrategy);

        String fileName = namingStrategy.getJavaClassNameForProcessModel(metaModel) + JAVA_FILE_EXTENSION;
        sourceGenerator.generateJavaSourceFile(DOMAIN_TYPE_MODEL_TEMPLATE, fileName, "process", context, metaModel);

        return 1;
    }


    protected void filterViewStatesForNotExistentViewModels(HandlerContext handlerContext, ProcessMetaModel metaModel)
    {
        final List<MetaModel> viewModelContext = handlerContext.getContextForHandlerWithId(HandlerConstants.VIEW_MODEL_HANDLER);
        final List<ViewStateMetaModel> obsoleteViewStateMetaModels = new ArrayList<ViewStateMetaModel>();
        final List<ViewStateMetaModel> viewStateMetaModels = metaModel.getViewStateMetaModels();
        
        for (ViewStateMetaModel viewStateMetaModel : viewStateMetaModels)
        {
            boolean foundMatchingView = false;
            final String viewReference = viewStateMetaModel.getViewReference();
            
            for (MetaModel otherMetaModel : viewModelContext)
            {
                ViewMetaModel viewMetaModel = (ViewMetaModel) otherMetaModel;
                final String viewId = viewMetaModel.getId();
                
                if(viewId.equals(viewReference))
                {
                    foundMatchingView = true;
                }
            }
            
            if(!foundMatchingView)
            {
                log.debug("Found view state model '{}' with not existent view model", viewStateMetaModel.getId());
                obsoleteViewStateMetaModels.add(viewStateMetaModel);
            }
        }
        
        viewStateMetaModels.removeAll(obsoleteViewStateMetaModels);
    }


    @Override
    public HandlerType getType()
    {
        return HandlerType.TEST_DOMAIN;
    }


    @Override
    protected List<MetaModel> parseModelImpl(File modelFile) throws XMLDocumentException
    {
        final ProcessMetaModel processMetaModel = new ProcessMetaModel();
        processMetaModel.setLocation(modelFile.getAbsolutePath());

        XMLTag processMetaModelType = XMLDoc.from(modelFile, true);

        processMetaModel.setId(processMetaModelType.findAttribute("id"));
        processMetaModel.setName(processMetaModelType.findAttribute("name"));

        try
        {
            processMetaModelType.forEach(new CallBack()
            {
                @Override
                public void execute(XMLTag startState)
                {
                    StartStateMetaModel startStateMetaModel = new StartStateMetaModel();
                    startStateMetaModel.setId(startState.findAttribute("id"));
                    startStateMetaModel.setName(startState.findAttribute("name"));
                    startStateMetaModel.setLocation(processMetaModel.getLocation());

                    processMetaModel.getStartStateMetaModels().add(startStateMetaModel);
                }
            }, "//process-state-set/start-state");

            processMetaModelType.forEach(new CallBack()
            {
                @Override
                public void execute(XMLTag startState)
                {
                    ViewStateMetaModel viewStateMetaModel = new ViewStateMetaModel();
                    viewStateMetaModel.setId(startState.findAttribute("id"));
                    viewStateMetaModel.setName(startState.findAttribute("name"));
                    viewStateMetaModel.setViewReference(startState.findAttribute("view-ref"));
                    viewStateMetaModel.setLocation(processMetaModel.getLocation());
                    
                    processMetaModel.getViewStateMetaModels().add(viewStateMetaModel);
                }
            }, "//process-state-set/view-state");

        }
        catch (XMLDocumentException e)
        {
            log.error("Exception", e);
            // Do nothing here, we aren't sure about the underlying XML file
            // so we have to guess the structure and may fail.
        }

        return Arrays.asList((MetaModel) processMetaModel);
    }
}
