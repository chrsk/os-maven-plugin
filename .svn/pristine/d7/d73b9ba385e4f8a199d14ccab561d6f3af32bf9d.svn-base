package org.opensaga.plugin.builder.meta.parser.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.VelocityBasedMethodGenerator;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewComponentMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewMetaModel;
import org.opensaga.plugin.builder.meta.parser.AbstractModelHandler;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.HandlerTypeBasedJavaNamingStrategy;
import org.opensaga.plugin.builder.meta.parser.ModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.view.ButtonQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.CategorySelectQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.CheckBoxQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.ComponentIdentifier;
import org.opensaga.plugin.builder.meta.parser.types.view.DataGridQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.ErrorMessageListQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.ExportButtonQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.HelpButtonQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.LinkQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.ListIteratorQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.MultiConnectQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.PlainTextPropertyQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.Query;
import org.opensaga.plugin.builder.meta.parser.types.view.RadioButtonsQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.RefreshButtonQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.SelectFieldQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.TabSetQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.TextAreaQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.TextFieldQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.xmltool.CallBack;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLDocumentException;
import com.mycila.xmltool.XMLTag;

public class ViewModelHandler
    extends AbstractModelHandler<ViewMetaModel> implements ModelHandler<ViewMetaModel>
{

    private static final Logger log = LoggerFactory.getLogger(ViewModelHandler.class);

    private List<String> uniqueViewNamingRegistry = new ArrayList<String>();
    
    private static final List<? extends Query> QUERIES = Arrays.asList(new ButtonQuery(), new TextFieldQuery(),
        new CheckBoxQuery(), new TextAreaQuery(), new MultiConnectQuery(), new LinkQuery(), new CategorySelectQuery(),
        new TabSetQuery(), new DataGridQuery(), new PlainTextPropertyQuery(), new SelectFieldQuery(), new HelpButtonQuery(),
        new ListIteratorQuery(), new ErrorMessageListQuery(), new RadioButtonsQuery(), new ExportButtonQuery(), new RefreshButtonQuery());

    private VelocityBasedMethodGenerator methodGenerator = new VelocityBasedMethodGenerator();


    @Override
    protected List<MetaModel> parseModelImpl(File modelFile) throws XMLDocumentException
    {
        final ViewMetaModel viewMetaModel = new ViewMetaModel();
        viewMetaModel.setLocation(modelFile.getAbsolutePath());

        XMLTag viewModelType = XMLDoc.from(modelFile, true);

        viewMetaModel.setId(viewModelType.findAttribute("id"));
        viewMetaModel.setName(viewModelType.findAttribute("name"));

        try
        {
            viewModelType.forEach(new CallBack()
            {
                @Override
                public void execute(XMLTag component)
                {
                    String tagName = component.getCurrentTag().getNodeName();

                    ViewComponentMetaModel componentMetaModel = new ViewComponentMetaModel();
                    componentMetaModel.setId(component.findAttribute("id"));
                    componentMetaModel.setName(component.findAttribute("name"));
                    componentMetaModel.setTagName(tagName);
                    componentMetaModel.setQuery(resolveMatchingQuery(tagName));
                    componentMetaModel.setLocation(viewMetaModel.getLocation());

                    resolveAndSetIdentifierForComponent(component, componentMetaModel);

                    if (componentMetaModel.isIdentifiable())
                    {
                        if (viewMetaModel.getComponentMetaModels().contains(componentMetaModel))
                        {
                            log.info("Found duplicate element with identifier '{}' of type '{}' in file '{}'",
                                new Object[] { componentMetaModel.getIdentifier(), componentMetaModel.getTagName(),
                                    componentMetaModel.getLocation() });

                            // We remove both occurances of this duplicate.
                            viewMetaModel.getComponentMetaModels().remove(componentMetaModel);
                            return;
                        }

                        viewMetaModel.getComponentMetaModels().add(componentMetaModel);
                    }
                }
            }, getQueriesAsXPathExpression());
        }
        catch (XMLDocumentException e)
        {
            log.error("Exception", e);
            // Do nothing here, we aren't sure about the underlying XML file
            // so we have to guess the structure and may fail.
        }

        Collections.sort(viewMetaModel.getComponentMetaModels(), new LexicographicalMetaModelComparator());
        return Arrays.asList((MetaModel) viewMetaModel);
    }


    @Override
    public int handleModel(HandlerContext handlerContext, ViewMetaModel metaModel, JavaSourceGenerator sourceGenerator)
        throws MetaGenerationFailedException
    {
        final String DOMAIN_TYPE_MODEL_TEMPLATE = "templates/java-models/view-meta-model.vtl";
        final HandlerTypeBasedJavaNamingStrategy namingStrategy = new HandlerTypeBasedJavaNamingStrategy(getType());

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("namingStrategy", namingStrategy);
        context.put("methodGenerator", methodGenerator);
        context.put("extendsClass", false);

        if (getConfiguration().containsKey(HandlerConstants.CONFIG_KEY_EXTENDS_CLASS))
        {
            context.put("extendingClass", getConfiguration().get(HandlerConstants.CONFIG_KEY_EXTENDS_CLASS));
            context.put("extendsClass", true);
        }

        verifyAndRegisterViewName(metaModel, namingStrategy);
        String fileName = namingStrategy.getJavaClassNameForViewModel(metaModel) + JAVA_FILE_EXTENSION;
        sourceGenerator.generateJavaSourceFile(DOMAIN_TYPE_MODEL_TEMPLATE, fileName, "view", context, metaModel);

        return 1;
    }


    protected void verifyAndRegisterViewName(ViewMetaModel metaModel, final HandlerTypeBasedJavaNamingStrategy namingStrategy)
        throws MetaGenerationFailedException
    {
        final String viewName = namingStrategy.getJavaClassNameForViewModel(metaModel);
        if (uniqueViewNamingRegistry.contains(viewName))
        {
            throw new MetaGenerationFailedException("The view name '" + viewName + "' for view with ID '" + metaModel.getId() + "' is a duplicate, please adjust it.");
        }
        else
        {
            uniqueViewNamingRegistry.add(viewName);
        }
    }


    private static String getQueriesAsXPathExpression()
    {
        List<String> queryXPathExpressions = new ArrayList<String>();

        for (Query query : QUERIES)
        {
            queryXPathExpressions.add("//" + query.getTag());
        }

        return StringUtils.join(queryXPathExpressions.toArray(), " | ");
    }


    private Query resolveMatchingQuery(String tagName)
    {
        Query matchingQuery = null;

        for (Query query : QUERIES)
        {
            if (query.getTag().equals(tagName))
            {
                matchingQuery = query;
            }
        }
        return matchingQuery;
    }


    private void resolveAndSetIdentifierForComponent(XMLTag component, ViewComponentMetaModel componentMetaModel)
    {
        String[] attributeNames = component.getAttributeNames();

        for (String attributeName : attributeNames)
        {
            ComponentIdentifier identifier = ComponentIdentifier.getIdentifierForAttribute(attributeName);

            if (identifier != null && componentMetaModel.getQuery().getModelElementQuery(identifier) != null)
            {
                String attributeValue = component.findAttribute(attributeName);

                if (StringUtils.isNotEmpty(attributeValue))
                {
                    componentMetaModel.setIdentifier(identifier, attributeValue);
                    break;
                }
            }
        }
    }


    @Override
    public List<String> getSupportedTypes()
    {
        return Arrays.asList("view");
    }


    @Override
    public String getId()
    {
        return HandlerConstants.VIEW_MODEL_HANDLER;
    }


    @Override
    public void mergeModel(MetaModel metaModel, MetaModel matchingMetaModel)
    {
        throw new NotImplementedException();
    }


    @Override
    public HandlerType getType()
    {
        return HandlerType.TEST_DOMAIN;
    }

    private static class LexicographicalMetaModelComparator
        implements Comparator<ViewComponentMetaModel>
    {

        @Override
        public int compare(ViewComponentMetaModel info1, ViewComponentMetaModel info2)
        {
            return info1.getIdentifier().compareTo(info2.getIdentifier());
        }

    }

}
